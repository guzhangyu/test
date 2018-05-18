package interview.redis;

import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;

public class JedisTest {

    static JedisPoolConfig config=new JedisPoolConfig();
    static{
        config.setMaxIdle(5);
        config.setMaxTotal(25);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
    }

    static JedisPool jedisPool=new JedisPool(config,"47.100.114.140",6379,1000,"85861367Li");

    public static void testPipeline() throws IOException {
        Jedis jedis=jedisPool.getResource();
        Pipeline pipeline=jedis.pipelined();
        jedis.watch("1");
        pipeline.multi();
        pipeline.get("1");
        pipeline.incr("2");
        pipeline.incr("1");


        Jedis jedis1=jedisPool.getResource();
        jedis1.incr("1");

        pipeline.exec();
        System.out.println(pipeline.syncAndReturnAll());//.get(3)==null
        jedis.unwatch();
        pipeline.close();
        System.out.println(jedis.get("2"));
    }

    public static void main(String[] args) throws IOException {
        testPipeline();
    }
}
