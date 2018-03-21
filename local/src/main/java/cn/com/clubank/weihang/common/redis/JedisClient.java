package cn.com.clubank.weihang.common.redis;

import java.util.List;

/**
 * jedis接口
 * 
 * @author LeiQY
 *
 */
public interface JedisClient {

	String set(String key, String value);

	String get(String key);

	Boolean exists(String key);

	Long expire(String key, int seconds);

	Long ttl(String key);

	Long incr(String key);

	Long hset(String key, String field, String value);

	String hget(String key, String field);

	Long hdel(String key, String... field);

	Boolean hexists(String key, String field);

	List<String> hvals(String key);

	Long del(String key);
}
