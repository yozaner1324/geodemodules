package org.apache.geode.service.mod3.impl;

import org.springframework.util.StringUtils;

import org.apache.geode.service.SampleService;

public class SampleServiceImpl implements SampleService {

	public String getValue() {
		return StringUtils.capitalize("sPrInG utiLS".toLowerCase());
	}
}
