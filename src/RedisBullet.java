

import java.net.URI;
import java.net.URISyntaxException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisBullet {
	public static Jedis getPool() {
		  URI redisURI = null;
		try {
			redisURI = new URI("");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Jedis jedis = new Jedis(redisURI);
		  return jedis;

		}

		// In your multithreaded code this is where you'd checkout a connection
		// and then return it to the pool
/*		try (Jedis jedis = pool.getResource()){
		  jedis.set("foo", "bar");
		}*/
}
