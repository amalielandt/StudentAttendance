package dk.cb.dls.studentattendance.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

public class JedisClient {
    private static JedisClient instance;
    private Jedis jedis;

    public static JedisClient getJedisClient() {
        if(instance == null) {
            instance = new JedisClient();
        }
        return instance;
    }
    private JedisClient() {
        this.jedis = new Jedis("localhost", 6379);
    }

    public boolean setWithExpire(String key, String value, long expire) {
        try (Transaction transaction = jedis.multi()) {
            transaction.set(key, value);
            Response<Long> success = transaction.expire(key, expire);
            transaction.exec();

            return success.get() == 1;
        }
    }

    public boolean set(String key, String value) {
        String success = jedis.set(key, value);
        return success.equals("OK");
    }

    public String get(String key) {
        return jedis.get(key);
    }
}
