package org.apache.geode.service.impl;

import org.springframework.util.StringUtils;

import org.apache.geode.service.SampleService;

public class Blooper {//implements SampleService {

	public String getValue() {
		return StringUtils.capitalize("sPrInG utiLS".toLowerCase());
	}
}
