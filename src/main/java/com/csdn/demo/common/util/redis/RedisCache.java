package com.csdn.demo.common.util.redis;

import com.csdn.demo.common.util.json.JsonHelper;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
* 类描述：
* @auther linzf
* @create 2017/12/11 0011
*/
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 功能描述：获取集合的数据
     * @param key
     * @param o
     * @return
     */
    public <T> List<T> getList(String key,Object o){
        String value = getString(key);
        return JSONArray.toList(JSONArray.fromObject(value),o,new JsonConfig());
    }

    /**
     * 功能描述：保存list集合到redis中
     * @param key
     * @param list
     */
    public <T> void setList(String key,List<T> list){
        String value = JsonHelper.list2json(list);
        setString(key,value);
    }

    /**
     * 功能描述：设置键值对数据
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, value);
    }

    /**
     * 功能描述：获取键值对数据
     * @param key
     * @return
     */
    public String getString(String key){
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        return opsForValue.get(key);
    }


    /**
     * 功能描述：设置过期时间
     * @param key
     * @param expire
     * @return
     */
    public boolean expire(String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }


}
