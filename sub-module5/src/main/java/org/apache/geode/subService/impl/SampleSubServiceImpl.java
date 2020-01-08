package org.apache.geode.subService.impl;

import org.apache.geode.subService.SampleSubService;

public class SampleSubServiceImpl implements SampleSubService {
  @Override
  public String getSubServiceValue() {
    return "Sub Service Implementation invoked";
  }
}
