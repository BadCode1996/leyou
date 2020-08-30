package com.leyou.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Srd
 * @date 2020/8/30  1:58
 */
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {
    private long workerId;// 当前机器id

    private long datacenterId;// 序列号

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }
}
