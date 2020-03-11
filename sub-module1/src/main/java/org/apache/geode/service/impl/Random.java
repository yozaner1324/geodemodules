package org.apache.geode.service.impl;

import com.google.common.base.Strings;

import org.apache.geode.service.SampleService;

public class Random {//implements SampleService {

  public String getValue() {
    return Strings.repeat("99 ", 7);
  }
}
