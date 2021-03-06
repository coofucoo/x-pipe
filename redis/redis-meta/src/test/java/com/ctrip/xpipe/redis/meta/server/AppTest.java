package com.ctrip.xpipe.redis.meta.server;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.ctrip.xpipe.lifecycle.SpringComponentRegistry;
import com.ctrip.xpipe.redis.core.config.AbstractCoreConfig;
import com.ctrip.xpipe.redis.core.foundation.IdcUtil;
import com.ctrip.xpipe.redis.meta.server.cluster.impl.ArrangeTaskExecutor;
import com.ctrip.xpipe.redis.meta.server.meta.impl.DefaultDcMetaCache;
import com.ctrip.xpipe.spring.AbstractProfile;
import com.ctrip.xpipe.zk.impl.TestZkClient;

/**
 * @author shyin
 *
 * Jul 28, 2016
 */

public class AppTest extends AbstractMetaServerContextTest{
	
	private int zkPort = IdcUtil.JQ_ZK_PORT;
	private int serverPort = IdcUtil.JQ_METASERVER_PORT;
	
	public AppTest(){
		
	}
	
	@Before
	public void beforeAppTest(){
		System.setProperty(AbstractProfile.PROFILE_KEY, AbstractProfile.PROFILE_NAME_PRODUCTION);
		System.setProperty(ArrangeTaskExecutor.ARRANGE_TASK_EXECUTOR_START, "true");
	}

	
	@Test
	public void startZk(){
		startZk(zkPort);
	}
	
	@Test
	public void start9747() throws Exception {
		
		System.setProperty(DefaultDcMetaCache.MEMORY_META_SERVER_DAO_KEY, "metaserver--jq.xml");
        System.setProperty(AbstractCoreConfig.KEY_ZK_NAMESPACE, "xpipe_dc1");		
		start();
	}

	
	@Test
	public void start9748() throws Exception {
		
		this.serverPort = IdcUtil.OY_METASERVER_PORT;
        System.setProperty(AbstractCoreConfig.KEY_ZK_NAMESPACE, "xpipe_dc2");		
		System.setProperty(DefaultDcMetaCache.MEMORY_META_SERVER_DAO_KEY, "metaserver--oy.xml");
		IdcUtil.setToOY();
		start();
	}
	
	
	public void start() throws Exception{
		
		System.setProperty("server.port", String.valueOf(serverPort));
		
		SpringComponentRegistry registry = SpringApplication.run(MetaServerApplication.class, new String[]{}).getBean(SpringComponentRegistry.class);
		TestZkClient testZkClient = registry.getComponent(TestZkClient.class);
		testZkClient.setZkAddress(String.format("localhost:%d", zkPort));
		registry.initialize();
		registry.start();
		add(registry);
		
	}

	
	@Override
	protected boolean isStartZk() {
		return false;
	}
	
	@Override
	protected ApplicationContext createSpringContext() {
		return null;
	}
	
	@After
	public void afterAppTest() throws IOException{
		waitForAnyKeyToExit();
	}
}
