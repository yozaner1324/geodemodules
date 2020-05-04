package org.apachee.geode.management.service.impl;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.impl.CacheImpl;
import org.apachee.geode.management.service.ManagementService;

public class JBossManagementService implements ManagementService {
	@Override
	public Cache createCache() {
		return new CacheImpl();
	}
}
