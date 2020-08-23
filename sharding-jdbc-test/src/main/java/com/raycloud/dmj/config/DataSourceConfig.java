package com.raycloud.dmj.config;

import com.mysql.jdbc.Driver;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingjdbc.core.jdbc.core.datasource.ShardingDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.raycloud.dmj.mapper",sqlSessionTemplateRef = "testSqlSessionTemplate")
public class DataSourceConfig {

    @Bean(name="shardingDataSource")
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig=new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getUserTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("user");
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("city_id",DemoDatabaseShardingAlgorithm.class.getName()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id",DemoTableShardingAlgorithm.class.getName()));
        return new ShardingDataSource(shardingRuleConfig.build(createDataSourceMap()));
    }

    @Bean
    TableRuleConfiguration getUserTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("user");
        orderTableRuleConfig.setActualDataNodes("user_${0..1}.user_${0..1}");
        orderTableRuleConfig.setKeyGeneratorColumnName("user_id");
        return orderTableRuleConfig;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource shardingDataSource){
        return new DataSourceTransactionManager(shardingDataSource);
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setDataSource(shardingDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return bean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    private Map<String,DataSource> createDataSourceMap(){
        Map<String,DataSource> result=new HashMap<>();
        result.put("user_0",createDataSource("user_0"));
        result.put("user_1",createDataSource("user_1"));
        return result;
    }

    private DataSource createDataSource(final String dataSourceName){
        BasicDataSource result=new BasicDataSource();
        result.setDriverClassName(Driver.class.getName());
        result.setUrl(String.format("jdbc:mysql://localhost:3306/%s",dataSourceName));
        result.setUsername("root");
        result.setPassword("root");
        return result;
    }
}
