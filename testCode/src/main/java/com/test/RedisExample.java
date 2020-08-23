package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class RedisExample {


    @Autowired
    RedisScript<Boolean> script;

    @Autowired
    RedisTemplate redisTemplate;

    public Boolean checkAndSet(String expectedValue,String newValue){
        return (Boolean) redisTemplate.execute(script, Collections.singletonList("key"),expectedValue,newValue);
    }

    private static final long DEFAULT_LOCK_TIME_OUT=3000;
    private static final long DEFAULT_TRY_LOCK_TIME_OUT=0;
    private static final String LUA_SCRIPT_LOCK ="return redis.call('SET',KEYS[1],ARGV[1],'NX','PX',ARGV[2])";
    private static RedisScript<String> scriptLock=new DefaultRedisScript<>(LUA_SCRIPT_LOCK,String.class);

    private static final String LUA_SCRIPT_UNLOCK ="if(redis.call('GET',KEYS[1])==ARGV[1]) then " +
            "return redis.call('DEL',KEYS[1]) " +
            "else return 0 end";
    private static RedisScript<String> scriptUnLock=new DefaultRedisScript<>(LUA_SCRIPT_UNLOCK,String.class);

    public String unLock(int dbIndex,RedisLock lock){
        return  redisTemplate.execute(scriptUnLock,redisTemplate.getStringSerializer(),redisTemplate.getStringSerializer(),
                Collections.singletonList(lock.getKey()),lock.getUuid().toString())+"";
    }

    public RedisLock lock(int dbIndex,String key,long lockTimeout,long tryLockTimeout){
        long timestamp=System.currentTimeMillis();
        //try{
            key=key+".lock";
            UUID uuid=UUID.randomUUID();
            int tryCount=0;
            while(tryLockTimeout==0 || (System.currentTimeMillis()-timestamp)<tryLockTimeout){
                String result= (String) redisTemplate.execute(scriptLock,redisTemplate.getStringSerializer(),
                        redisTemplate.getStringSerializer(),Collections.singletonList(key),
                        uuid.toString(),String.valueOf(lockTimeout));
                tryCount++;

                if(result!=null && result.equals("OK")){
                    return new RedisLock(key,uuid,lockTimeout,timestamp,System.currentTimeMillis(),tryCount);
                }else{
                    try{
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            System.out.println("Fail to get lock key");
       // }
        return null;
    }

    public List testWatch(){
        return (List) redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.watch(Arrays.asList("a","b"));
                operations.multi();
                ValueOperations valueOperations=operations.opsForValue();
                valueOperations.set("a",5);
                valueOperations.set("b",6);
                Object o= (Object) operations.exec();
                return o;
            }
        });
    }
}
