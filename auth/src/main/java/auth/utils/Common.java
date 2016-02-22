package auth.utils;

import auth.starter.AuthStarter;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;
import thirdparty.redis.utils.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Dell on 2016/2/22.
 */
public class Common {
    AtomicLong userId = new AtomicLong(0);
    public static final Long GMId = 1L;

//    public static Integer generateUserId() {
//        Jedis jedis = AuthStarter._redisPoolManager.getJedis();
//        byte[] userIdBytes = jedis.hget(GMId, UserUtils.userFileds.LastUserId.field);
//        jedis.hget()
//    }
}
