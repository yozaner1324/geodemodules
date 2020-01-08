package org.apache.geode.subService;

import org.apache.geode.service.SampleService;

public interface SampleSubService extends SampleService {

  default String getSubServiceValue() {
    return "You should never see this";
  }
}
