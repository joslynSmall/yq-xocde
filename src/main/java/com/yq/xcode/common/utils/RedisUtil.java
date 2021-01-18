package com.yq.xcode.common.utils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.yq.xcode.common.base.XBaseView;

public class RedisUtil {
	private  RedisTemplate<String, XBaseView> redisTemplate;
	private StringRedisTemplate stringRedisTemplate;
	private String namespace;

	public void setRedisTemplate(RedisTemplate<String, XBaseView> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public void setNamespace(String namespace) {
		if (namespace != null)
			this.namespace = namespace + ":";
	}
	//私有方法,补全key 
	private void put2String(String fullKey, String v, long time) {
		stringRedisTemplate.opsForValue().set(fullKey, v);
		if (time > 0)
			stringRedisTemplate.expire(fullKey, time, TimeUnit.SECONDS);
	}
	//私有方法,补全key 
	private void put2View(RedisTemplate<String, XBaseView> template,String fullKey, XBaseView v, long time) {
		template.opsForValue().set(fullKey, v);
		if (time > 0)
			template.expire(fullKey, time, TimeUnit.SECONDS);
	}
	private String getFullKey(String classTypeId, String key) {
		return namespace + classTypeId + "_" + key;
	}
	/**
	 * 
	 * 同过类别和业务key放入redis.
	 * @param classTypeId 类别id 
     * @param key 对象id 
     * @param v 对象
	 */
	public <T extends XBaseView>  void put(RedisTemplate<String, XBaseView> template,
			String classTypeId, String key, T v) {
		put2View(template,getFullKey(classTypeId, key), v, -1);
	}
	/**
	 * 
	 * 同过类别和业务key放入redis.
	 * @param classTypeId 类别id 
     * @param key 对象id 
     * @param v 对象
	 */
	public <T extends XBaseView>  void put(String classTypeId, String key, T v) {
		put2View(redisTemplate,getFullKey(classTypeId, key), v, -1);
	}
	/**
	 * 同过类别和业务key放入redis.
	 * @param classTypeId 类别id 
     * @param key 对象id 
     * @param v 对象
	 */
	public <T extends XBaseView> void put(String classTypeId, String key, T v, long time) {
		put2View(redisTemplate,getFullKey(classTypeId, key), v, time);
	}
	public <T extends XBaseView> void put(RedisTemplate<String, XBaseView> template,
			String classTypeId, String key, T v, long time) {
		put2View(template,getFullKey(classTypeId, key), v, time);
	}
	/**
	 * 同过类别和业务key放入redis.
	 * @param classTypeId 类别id 
     * @param key 对象id 
     * @param v 对象
	 */
	public void putString(String classTypeId, String key, String v, long time) {
		put2String(getFullKey(classTypeId, key), v, time);
	}
	/**
	 * 同过类别和业务key放入redis.
	 * @param classTypeId 类别id 
     * @param key 对象id 
     * @param v 对象
	 */
	public void putString(String classTypeId, String key, String v) {
		put2String(getFullKey(classTypeId, key), v, -1);
	}
	/**
	 * 同过类别和业务key判断redis里面是否存在.
	 * @param classTypeId 类别id 
     * @param key 对象id 
	 * @return boolean
	 */
	public boolean contains(String classTypeId, String key) {
		return contains(redisTemplate,classTypeId, key);
	}
	public boolean contains(RedisTemplate<String, XBaseView> template,
			String classTypeId, String key) {
		return template.hasKey(getFullKey(classTypeId, key));
	}
	/**
	 * 同过类别和业务key 获取redis的值.
	 * @param classTypeId 类别id 
     * @param key 对象id 
	 * @return String
	 */
	public String getString(String classTypeId, String key) {
		if (stringRedisTemplate != null) {
			return stringRedisTemplate.opsForValue().get(getFullKey(classTypeId, key));
		} else {
			return redisTemplate.opsForValue().get(getFullKey(classTypeId, key)).toString();
		}
	}
	/**
	 * 同过类别和业务key 获取redis的值.
	 * @param classTypeId 类别id 
     * @param key 对象id 
	 * @return XBaseView
	 */
	public <T extends XBaseView> T get(String classTypeId, String key) {
		return  get(redisTemplate,classTypeId,key);
	}
	@SuppressWarnings("unchecked")
	public <T extends XBaseView> T get(RedisTemplate<String, XBaseView> template,
			String classTypeId, String key) {
		ValueOperations<String, XBaseView> valueOps = template.opsForValue();
		return  (T) valueOps.get(getFullKey(classTypeId, key));
	}
	/**
	 * 同过key获取redis的值.
	 * @param key
	 * @return XBaseView
	 */
	public <T extends XBaseView> T get(String key) {
		return  get(redisTemplate,key);
	}
	@SuppressWarnings("unchecked")
	public <T extends XBaseView> T get(RedisTemplate<String, XBaseView> template, String key) {
		ValueOperations<String, XBaseView> valueOps = template.opsForValue();
		return  (T) valueOps.get(key);
	}
	/**
	 * 通过类别和业务key,删除redis的值.
	 * @param classTypeId 类别id 
     * @param key 对象id 
	 */
	public void remove(String classTypeId, String key) {
		remove(redisTemplate,classTypeId, key);
	}
	public void remove(RedisTemplate<String, XBaseView> template,
			String classTypeId, String key) {
		template.delete(getFullKey(classTypeId, key));
	}
	/**
	 * 通过key删除redis的值.
	 * @param key
	 */
	public void remove(String key) {
		remove(redisTemplate, key);
	}
	public void remove(RedisTemplate<String, XBaseView> template,String key) {
		template.delete(key);
	}
	public long getExpire(String classTypeId, String key) {
		return getExpire(redisTemplate,classTypeId, key);
	}
	public long getExpire(RedisTemplate<String, XBaseView> template,
			String classTypeId, String key) {
		return template.getExpire(getFullKey(classTypeId, key));
	}

	/**
	 * 通过类别获取keys
	 * @param classTypeId
	 * @return
	 */
	public Set<String> keys(String classTypeId) {
		return keys(redisTemplate,classTypeId);
	}
	public Set<String> keys(RedisTemplate<String, XBaseView> template,
			String classTypeId) {
		String pattern = namespace + classTypeId + "_*";
		return stringRedisTemplate.keys(pattern);
	}
	public Long increment(String classTypeId, String key, long delta) {
		return increment(redisTemplate,classTypeId, key, delta);
	}
	public Long increment(RedisTemplate<String, XBaseView> template,
			String classTypeId, String key, long delta) {
		return template.opsForValue().increment(getFullKey(classTypeId, key), delta);
	}
	public Double increment(String classTypeId, String key, double delta) {
		return increment(redisTemplate,classTypeId, key, delta);
	}
	public Double increment(RedisTemplate<String, XBaseView> template,
			String classTypeId, String key, double delta) {
		return template.opsForValue().increment(getFullKey(classTypeId, key), delta);
	}

}