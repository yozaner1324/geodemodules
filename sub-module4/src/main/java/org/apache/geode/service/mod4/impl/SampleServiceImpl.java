package org.apache.geode.service.mod4.impl;

import java.util.ServiceLoader;

import org.apache.geode.service.SampleService;
import org.apache.geode.subService.SampleSubService;

public class SampleServiceImpl implements SampleService {

  @Override
  public void init(Object... initObjects) {
  }

  @Override
  public String getValue() {
    try {
      for (SampleService sampleService : ServiceLoader.load(SampleSubService.class)) {
        SampleSubService sampleSubService = (SampleSubService) sampleService;
        return sampleSubService.getSubServiceValue();
      }
    } catch (Exception e) {
    }
    return "No Implementation found";
  }
}
