package thirdparty.redis.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dell on 2016/2/22.
 */

//todo 为防止并发稍后加入对数据库锁的支持
public class RedisOperator {
    private static final Logger logger = LoggerFactory.getLogger(RedisOperator.class);

    //单个数据的读取和写入目前可以直接调用hset和hget
}
