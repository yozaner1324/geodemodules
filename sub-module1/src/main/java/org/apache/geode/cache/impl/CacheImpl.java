package org.apache.geode.cache.impl;

import org.apache.geode.cache.Cache;

public class CacheImpl implements Cache {
	@Override
	public String sayHello() {
		return "Hello!";
	}
}
