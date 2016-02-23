package auth.utils;

/**
 * Created by Dell on 2016/2/22.
 */
public class Common {
//    static AtomicLong userId;
//    public static final Long GMId = 0L;
//
//    public static long generateUserId() throws TException {
//        Jedis jedis = AuthStarter._redisPoolManager.getJedis();
//        byte[] userIdBytes = jedis.hget(UserUtils.genDBKey(GMId), UserUtils.userFileds.LastUserId.field);
//        LastUserId last;
//        if(userIdBytes == null) {
//            last = new LastUserId();
//            last.setLast(1);
//        } else {
//            last = DBOperator.Deserialize(new LastUserId(), userIdBytes);
//        }
//
//        userId = new AtomicLong(last.getLast());
//        long gen = userId.incrementAndGet();
//        last.setLast(gen);
//        //todo 这里写数据库，之后要加锁处理
//        jedis.hset(UserUtils.genDBKey(GMId), UserUtils.userFileds.LastUserId.field, DBOperator.Serialize(last));
//
//        return gen;
//    }
}
