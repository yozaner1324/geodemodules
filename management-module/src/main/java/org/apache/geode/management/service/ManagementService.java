package org.apache.geode.management.service;

import org.apache.geode.cache.Cache;

public interface ManagementService {
	Cache createCache() throws Exception;
}