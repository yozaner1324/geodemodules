package org.apachee.geode.management.service.impl;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.impl.CacheImpl;
import org.apache.geode.module.service.ModuleService;
import org.apachee.geode.management.service.ManagementService;

public class JBossManagementService implements ManagementService {

	private ModuleService moduleService;

	public JBossManagementService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	@Override
	public Cache createCache() {
		return new CacheImpl();
	}
}
