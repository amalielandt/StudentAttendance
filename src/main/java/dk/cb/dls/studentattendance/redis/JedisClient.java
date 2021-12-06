package dk.cb.dls.studentattendance.redis;

import com.sun.istack.NotNull;
import dk.cb.dls.studentattendance.errorhandling.JedisClientException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;

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

    public void setWithExpire(String key, String value, long expire) throws JedisClientException {
        try (Transaction transaction = jedis.multi()) {
            transaction.set(key, value);
            transaction.expire(key, expire);
            List<Object> response = transaction.exec();
            transaction.close();

            if(!response.get(0).equals("OK")) {
                throw new Exception("Key " + key + " could not be set in Redis");
            }
            else if((Long) response.get(1) == 0) {
                throw new Exception("Expire: " + expire +  " could not be set on key " + key);
            }
        } catch (Exception e ) {
            throw new JedisClientException(e.getMessage());
        }
    }

    public void setMultiWithExpire(Map<String, String> inputs, long expire) throws JedisClientException {
        try (Transaction transaction = jedis.multi()) {
            for (Map.Entry<String, String> entry : inputs.entrySet()) {
                transaction.set(entry.getKey(), entry.getValue());
                transaction.expire(entry.getKey(), expire);
            }
            List<Object> responses = transaction.exec();
            transaction.close();

            for (int i = 0; i < responses.size(); i++)
            {
                if(i % 2 == 0) {
                    if(!responses.get(i).equals("OK")) {
                        throw new Exception("Key could not be set in Redis");
                    }
                }
                else if( i % 2 == 1 ) {
                    if((Long) responses.get(i) == 0) {
                        throw new Exception("Expire " + expire + " could not be on key ");
                    }
                }
            }
        } catch (Exception e ) {
            throw new JedisClientException(e.getMessage());
        }
    }

    public void set(String key, String value) throws JedisClientException {
        try {
            String success = jedis.set(key, value);
            if (!success.equals("OK")) {
                throw new Exception("Key " + key + " could not be set in Redis");
            }
        } catch (Exception e) {
            throw new JedisClientException(e.getMessage());
        }
    }

    public String get(String key) throws JedisClientException {
        try {
            return jedis.get(key);
        }catch (Exception e) {
            throw new JedisClientException(e.getMessage());
        }
    }
}
