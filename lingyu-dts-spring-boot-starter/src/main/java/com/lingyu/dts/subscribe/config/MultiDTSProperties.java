package com.lingyu.dts.subscribe.config;

import cn.hutool.core.date.DatePattern;
import com.lingyu.common.util.DateTimeUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "lingyu.aliyun.dts.subscribe")
@Data
public class MultiDTSProperties {

    private List<DTSProperty> list;

    @Data
    public static class DTSProperty {

        private String name;

        private String brokerUrl;

        private String topic;

        private String sid;

        private String userName;

        private String password;

        /**
         * 消息位点保存
         * eg:mysql
         */
//        private String metaStore;

        /**
         * 模式
         * eg:assign, subscribe,
         */
        private String subscribeMode;

        /**
         * yyyyMMddHHmmss格式
         */
        private String startTime;

        private String initCheckpoint;

        public void setStartTime(String startTime) {
            this.startTime = startTime;
            this.initCheckpoint = DateTimeUtil.date2UnixTimeStamp(startTime, DatePattern.PURE_DATETIME_PATTERN).toString();
        }

    }
}
