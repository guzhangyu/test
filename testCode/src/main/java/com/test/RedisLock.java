package com.test;

import java.util.UUID;

public class RedisLock {

    private String key;

    private final UUID uuid;

    private long lockTimeout;

    private long startLockTimeMillis;

    private long getLockTimeMillis;

    private int tryCount;

    public RedisLock(String key, UUID uuid, long lockTimeout, long startLockTimeMillis, long getLockTimeMillis, int tryCount) {
        this.key = key;
        this.uuid = uuid;
        this.lockTimeout = lockTimeout;
        this.startLockTimeMillis = startLockTimeMillis;
        this.getLockTimeMillis = getLockTimeMillis;
        this.tryCount = tryCount;
    }

    public String getKey() {
        return key;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getLockTimeout() {
        return lockTimeout;
    }

    public long getStartLockTimeMillis() {
        return startLockTimeMillis;
    }

    public long getGetLockTimeMillis() {
        return getLockTimeMillis;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLockTimeout(long lockTimeout) {
        this.lockTimeout = lockTimeout;
    }

    public void setStartLockTimeMillis(long startLockTimeMillis) {
        this.startLockTimeMillis = startLockTimeMillis;
    }

    public void setGetLockTimeMillis(long getLockTimeMillis) {
        this.getLockTimeMillis = getLockTimeMillis;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    @Override
    public String toString() {
        return "RedisLock{" +
                "key='" + key + '\'' +
                ", uuid=" + uuid +
                ", lockTimeout=" + lockTimeout +
                ", startLockTimeMillis=" + startLockTimeMillis +
                ", getLockTimeMillis=" + getLockTimeMillis +
                ", tryCount=" + tryCount +
                '}';
    }
}
