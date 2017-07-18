package com.czh.util;

/**
 * Created by Chen on 2017/5/8.
 */
public class RedisKeyUtil {
    private static final String SPILT = ":";
    private static final String BIZ_LIKE = "LIKE";
    private static final String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPILT + String.valueOf(entityType) + SPILT + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPILT + String.valueOf(entityType) + SPILT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }
}
