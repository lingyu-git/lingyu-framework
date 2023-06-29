package com.lingyu.dts.subscribe;

import com.aliyun.dts.subscribe.clients.ConsumerContext;
import com.aliyun.dts.subscribe.clients.DTSConsumer;
import com.aliyun.dts.subscribe.clients.DefaultDTSConsumer;
import com.aliyun.dts.subscribe.clients.common.RecordListener;
import com.aliyun.dts.subscribe.clients.metastore.AbstractUserMetaStore;
import com.aliyun.dts.subscribe.clients.record.OperationType;
import com.aliyun.dts.subscribe.clients.recordprocessor.DbType;
import com.aliyun.dts.subscribe.clients.recordprocessor.DefaultRecordPrintListener;
import com.lingyu.common.util.DateTimeUtil;
import com.lingyu.dts.subscribe.config.MultiDTSProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LingYuDTSConsumer implements InitializingBean {

    @Autowired
    private List<LingYuRecordListener> recordListeners;

    @Autowired
    private AbstractUserMetaStore userMetaStore;

    private MultiDTSProperties.DTSProperty property;

    private DTSConsumer delegate;

    public LingYuDTSConsumer(MultiDTSProperties.DTSProperty property) {
        this.property = property;
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println(property.getName() + " starting...");
        String initCheckpoint = property.getInitCheckpoint();
        ConsumerContext.ConsumerSubscribeMode subscribeMode = ConsumerContext.ConsumerSubscribeMode.ASSIGN.name().equalsIgnoreCase(property.getSubscribeMode()) ?
                ConsumerContext.ConsumerSubscribeMode.ASSIGN : ConsumerContext.ConsumerSubscribeMode.SUBSCRIBE.name().equalsIgnoreCase(property.getSubscribeMode()) ?
                ConsumerContext.ConsumerSubscribeMode.SUBSCRIBE : ConsumerContext.ConsumerSubscribeMode.UNKNOWN;
        ConsumerContext consumerContext = new ConsumerContext(property.getBrokerUrl(), property.getTopic(), property.getSid(),
                property.getUserName(), property.getPassword(), initCheckpoint, subscribeMode);
        consumerContext.setForceUseCheckpoint(false);
        consumerContext.setUserRegisteredStore(userMetaStore);
        delegate = new DefaultDTSConsumer(consumerContext);
        delegate.addRecordListeners(buildRecordListener());
        delegate.start();
        System.out.println(property.getName() + " started...");
    }


    private Map<String, RecordListener> buildRecordListener() {

        // user can impl their own listener
        RecordListener mysqlRecordListener = record -> {
            OperationType operationType = record.getOperationType();
            if(operationType.equals(OperationType.INSERT)
                    || operationType.equals(OperationType.UPDATE)
                    || operationType.equals(OperationType.DELETE)
                    || operationType.equals(OperationType.DDL)
                    || operationType.equals(OperationType.HEARTBEAT)) {

                // consume record
                // 日志打印
                RecordListener recordPrintListener = new DefaultRecordPrintListener(DbType.MySQL);
                recordPrintListener.consume(record);

                for (LingYuRecordListener recordListener : recordListeners) {
                    if (recordListener.support(record)) {
                        recordListener.consume(record);
                    }
                }

                //commit method push the checkpoint update
                record.commit(DateTimeUtil.date2UnixTimeStamp(new Date()).toString());
            }
        };
        return Collections.singletonMap("mysqlRecordPrinter", mysqlRecordListener);
    }

    public void close() {
        this.delegate.close();
    }

}
