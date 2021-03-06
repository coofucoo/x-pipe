package com.ctrip.xpipe.redis.keeper;

import java.io.Closeable;

import com.ctrip.xpipe.api.lifecycle.Releasable;
import com.ctrip.xpipe.api.observer.Observable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * @author wenchao.meng
 *
 * 2016年4月22日 上午11:25:07
 */
public interface RedisClient extends Observable, Infoable, Closeable, RedisRole, Releasable{
	
	public static enum CLIENT_ROLE{
		NORMAL,
		SLAVE
	}
	
	public static enum CAPA{
		
		EOF;
		
		public static CAPA of(String capaString){
			
			if("eof".equalsIgnoreCase(capaString)){
				return EOF;
			}
			throw new IllegalArgumentException("unsupported capa type:" + capaString);
		}
	}
	RedisSlave becomeSlave();
	
	RedisKeeperServer getRedisKeeperServer();

	void setSlaveListeningPort(int port);

	int getSlaveListeningPort();

	void capa(CAPA capa);
	
	String []readCommands(ByteBuf byteBuf);

	String info();
	
	Channel channel();

	void sendMessage(ByteBuf byteBuf);
	
	void sendMessage(byte[] bytes);
	
	void addChannelCloseReleaseResources(Releasable releasable);

	void processCommandSequentially(Runnable runnable);

}
