package org.apache.geode.service.impl;

import org.apache.geode.module.service.ModuleService;
import org.apache.geode.service.SampleService;
import org.apache.geode.subService.SampleSubService;

public class SampleServiceImpl implements SampleService {
  private ModuleService moduleService;

  @Override
  public void init(Object... initObjects) {
    moduleService = (ModuleService) initObjects[0];
  }

  @Override
  public String getValue() {
    try {
      for (SampleService sampleService : moduleService.loadService(SampleSubService.class)) {
        SampleSubService sampleSubService = (SampleSubService) sampleService;
        return sampleSubService.getSubServiceValue();
      }
    } catch (Exception e) {
    }
    return "No Implementation found";
  }
}
