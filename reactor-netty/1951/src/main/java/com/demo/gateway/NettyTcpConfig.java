package com.demo.gateway;

import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyTcpConfig {

    private int selectCount = 1;

    
    private int workerCount = 4;

    
    private int connectTimeoutMillis = 10000;

    
    private int writeBufferHighWaterMark = 65536;

    
    private int writeBufferLowWaterMark = 32768;

    
    private boolean soKeepalive = true;

    
    private boolean soReuseaddr = false;

    
    private int soLinger = -1;

    
    private int soBacklog = 128;

    
    private boolean tcpNodelay = true;


    
    private int clientThreadCount = 2;

    
    private int serverThreadCount = 2;

    
    private int clientQueueCount = 10000;

    
    private int serverQueueCount = 10000;

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public void setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        this.writeBufferHighWaterMark = writeBufferHighWaterMark;
    }

    public void setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        this.writeBufferLowWaterMark = writeBufferLowWaterMark;
    }

    public void setSoKeepalive(boolean soKeepalive) {
        this.soKeepalive = soKeepalive;
    }

    public void setSoReuseaddr(boolean soReuseaddr) {
        this.soReuseaddr = soReuseaddr;
    }

    public void setSoLinger(int soLinger) {
        this.soLinger = soLinger;
    }

    public void setSoBacklog(int soBacklog) {
        this.soBacklog = soBacklog;
    }

    public void setTcpNodelay(boolean tcpNodelay) {
        this.tcpNodelay = tcpNodelay;
    }

    public void setClientThreadCount(int clientThreadCount) {
        this.clientThreadCount = clientThreadCount;
    }

    public void setServerThreadCount(int serverThreadCount) {
        this.serverThreadCount = serverThreadCount;
    }

    public void setClientQueueCount(int clientQueueCount) {
        this.clientQueueCount = clientQueueCount;
    }

    public void setServerQueueCount(int serverQueueCount) {
        this.serverQueueCount = serverQueueCount;
    }

    public int getClientThreadCount() {
        return clientThreadCount;
    }

    public int getServerThreadCount() {
        return serverThreadCount;
    }

    public int getClientQueueCount() {
        return clientQueueCount;
    }

    public int getServerQueueCount() {
        return serverQueueCount;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public int getWriteBufferHighWaterMark() {
        return writeBufferHighWaterMark;
    }

    public int getWriteBufferLowWaterMark() {
        return writeBufferLowWaterMark;
    }

    public boolean isSoKeepalive() {
        return soKeepalive;
    }

    public boolean isSoReuseaddr() {
        return soReuseaddr;
    }

    public int getSoLinger() {
        return soLinger;
    }

    public int getSoBacklog() {
        return soBacklog;
    }

    public boolean isTcpNodelay() {
        return tcpNodelay;
    }
}
