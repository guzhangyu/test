package mycache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

public class Main {

    public static void main(String[] args) {
        CachingProvider cachingProvider=Caching.getCachingProvider();
        System.out.println(cachingProvider.getClass());
        CacheManager cacheManager=cachingProvider.getCacheManager();
        Cache<String,String> cache=cacheManager.getCache("haha",String.class,String.class);
        cache.put("a","b");
        System.out.println(cache.get("a"));
    }
}
