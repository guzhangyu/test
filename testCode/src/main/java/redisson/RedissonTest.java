//package redisson;
//
//import org.redisson.Redisson;
//import org.redisson.api.RBucket;
//import org.redisson.api.RedissonClient;
//import org.redisson.client.codec.StringCodec;
//import org.redisson.config.Config;
//import org.redisson.config.SingleServerConfig;
//
//public class RedissonTest {
//    public static void main(String[] args) {
//        Config config=new Config();
//        config.setCodec(new StringCodec());
//        SingleServerConfig singleServerConfig=config.useSingleServer().setAddress("redis://172.31.0.219:6379");
//        singleServerConfig.setConnectionPoolSize(500);
//        singleServerConfig.setIdleConnectionTimeout(10000);
//        singleServerConfig.setConnectTimeout(30000);
//        singleServerConfig.setTimeout(3000);
//        singleServerConfig.setPingTimeout(30000);
//        singleServerConfig.setReconnectionTimeout(3000);
//        singleServerConfig.setPassword("unitymob");
//
//        RedissonClient redisson= Redisson.create(config);
//
//        RBucket<String> keyObject=redisson.getBucket("key");
//        keyObject.set("value");
//        redisson.shutdown();
//    }
//}
