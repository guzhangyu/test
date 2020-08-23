package com.raycloud.dmj.config;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

public class DemoTableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        for(String each: collection){
            if(each.endsWith(preciseShardingValue.getValue()% 2+ "")){
                return each;
            }
        }
        throw new IllegalArgumentException();
    }
}