package org.apache.geode.service.impl;

import org.apache.commons.lang3.StringUtils;

import org.apache.geode.service.SampleService;

public class Misc{//} implements SampleService {

	public String getValue() {
		return StringUtils.prependIfMissing("sub2","42");
	}
}
