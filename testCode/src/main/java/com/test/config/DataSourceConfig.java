package com.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mongodb.Mongo;
import com.mongodb.MongoCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

@EnableMongoRepositories(basePackages = "com.test.db")
@EnableAspectJAutoProxy
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.initial-size}")
    private int initialSize;
    @Value("${spring.datasource.min-idle}")
    private int minIdle;
    @Value("${spring.datasource.max-active}")
    private int maxActive;
    @Value("${spring.datasource.max-wait}")
    private int maxWait;
    @Value("${spring.datasource.time-between-eviction-runs-millis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.min-evictable-idle-time-millis}")
    private int minEvictableIdleTimeMillis;
    @Value("${spring.datasource.validation-query}")
    private String validationQuery;
    @Value("${spring.datasource.test-while-idle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.test-on-borrow}")
    private boolean testOnBorrow;
//    @Value("${spring.datasource.test-on-return}")
//    private boolean testOnReturn;
//    @Value("${spring.datasource.poolPreparedStatements}")
//    private boolean poolPreparedStatements;
//    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
//    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.filters}")
    private String filters;
    @Value("${spring.datasource.connectionProperties}")
    private String connectionProperties;

    @Value("${spring.datasource.useGlobalDataSourceStat}")
    private boolean useGlobalDataSourceStat;

//    @Bean
//    public RedisConnectionFactory redisCF(){
//        return new JedisConnectionFactory();
//    }
//
//    @Bean
//    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory cf){
//        RedisTemplate<String,String> redis=new RedisTemplate<>();
//        redis.setConnectionFactory(cf);
//        return redis;
//    }

//    @Bean
//    public MongoClientFactoryBean mongo(){
//        MongoClientFactoryBean mongo=new MongoClientFactoryBean();
//        mongo.setHost("localhost");
//        mongo.setCredentials(new MongoCredential[]{MongoCredential.createMongoCRCredential("admin","OrdersDB","gzy".toCharArray())});
//        return mongo;
//    }
//
//    @Bean
//    public MongoOperations mongoTemplate(MongoClientFactoryBean mongo) throws Exception{
//        return new MongoTemplate(mongo.getObject(),"OrdersDB");
//    }

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
      //  datasource.setTestOnReturn(testOnReturn);
//        datasource.setPoolPreparedStatements(poolPreparedStatements);
//        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: "+ e);
        }
        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

}
