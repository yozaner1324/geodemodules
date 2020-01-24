package org.apache.geode.service.mod1.impl;

import com.google.common.base.Strings;

import org.apache.geode.service.SampleService;

public class SampleServiceImpl implements SampleService {

  public String getValue() {
    return Strings.repeat("99 ", 7);
  }
}
