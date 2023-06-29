package com.lingyu.dts.subscribe;

import com.aliyun.dts.subscribe.clients.common.RecordListener;
import com.aliyun.dts.subscribe.clients.record.UserRecord;

public interface LingYuRecordListener extends RecordListener {

    boolean support(UserRecord record);

}
