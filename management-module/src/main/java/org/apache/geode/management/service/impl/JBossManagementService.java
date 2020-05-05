package org.apache.geode.management.service.impl;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.impl.CacheImpl;
import org.apache.geode.module.service.ModuleService;
import org.apache.geode.management.service.ManagementService;
import org.jboss.modules.Module;

public class JBossManagementService implements ManagementService {

	private ModuleService moduleService;

	public JBossManagementService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	@Override
	public Cache createCache() throws Exception {
		Module module = moduleService.loadModule("sub-module1");
		Class<?> clazz = moduleService.loadClass("org.apache.geode.cache.impl.CacheImpl");
		return (Cache) clazz.newInstance();
//		return new CacheImpl();
	}
}
