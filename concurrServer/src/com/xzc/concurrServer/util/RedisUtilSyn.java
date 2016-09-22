package com.xzc.concurrServer.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtilSyn {

	//Redis服务器IP
	private static String ADDR = "59.33.250.67";

	//Redis的端口号
	//    private static int PORT = 8010;
	private static int PORT = 6379;

	//访问密码
	private static String AUTH = "admin";

	//可用连接实例的最大数目，默认值为8；
	//如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 80;//平时弄为1024

	//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 30;//平时我设为200

	//等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 10000;

	private static int TIMEOUT = 10000;

	//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	private static JedisPool jedisPool = null;

	/**
	 * 初始化Redis连接池
	 */
	private static void initialPool(){
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
			config.setBlockWhenExhausted(true);

			//设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");

			//是否启用pool的jmx管理功能, 默认true
			config.setJmxEnabled(true);

			//MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
			config.setJmxNamePrefix("pool");

			//是否启用后进先出, 默认true
			config.setLifo(true);
			//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
			//config.setMinEvictableIdleTimeMillis(1800000);

			//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
			config.setNumTestsPerEvictionRun(20);

			//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)   
			config.setSoftMinEvictableIdleTimeMillis(1800000);
			//在空闲时检查有效性, 默认false
			config.setTestWhileIdle(true);

			//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
			config.setTimeBetweenEvictionRunsMillis(3600000);


			/*jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);*/
			jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (jedisPool == null) {  
			initialPool();
		}
	}

	//    public static void main(String args[]){
	//    	System.out.println("RedisServer start");
	//    	Jedis jedis = getJedis(0);
	//    	jedis.set("why", "桂泰");
	//    	System.out.println(jedis.get("why"));
	//    }

	/**
	 * 获取Jedis实例
	 * @return
	 */
	//明天验证一下是不是因为锁，因为获取jedis阻塞住没查过有这东西，但是这锁也没问题啊
	public synchronized static Jedis getJedis(int flag) {
		try {
			if (jedisPool == null) {
				System.out.println("initialJedisPool");
				poolInit();
			}
			if (jedisPool != null) {
				//            	if(flag == 1) System.out.println("getJedisResource");
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 返回一个非空的jedis对象
	 * @return 一个非空的jedis对象
	 */
	public synchronized static Jedis getJedis() {
		while (true) {
			if (jedisPool == null) {
				System.out.println("initialJedisPool");
				poolInit();
			}
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				if (resource != null) {
					return resource;
				}
			}
		}

	}




	/**
	 * 释放jedis资源
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/*while(true){
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            return jedisAction.action(jedis); //模板方法
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

	 *//**
	 * Handle jedisException, write log and return whether the connection is broken.
	 *//*
    protected boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            logger.error("Redis connection " + jedisPool.getAddress() + " lost.", jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
                logger.error("Redis connection " + jedisPool.getAddress() + " are read-only slave.", jedisException);
            } else {
                // dataException, isBroken=false
                return false;
            }
        } else {
            logger.error("Jedis exception happen.", jedisException);
        }
        return true;
    }
	  *//**
	  * Return jedis connection to the pool, call different return methods depends on the conectionBroken status.
	  *//*
    protected void closeResource(Jedis jedis, boolean conectionBroken) {
        try {
            if (conectionBroken) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }
        } catch (Exception e) {
            logger.error("return back jedis failed, will fore close the jedis.", e);
            JedisUtils.destroyJedis(jedis);
        }
    }*/
}
