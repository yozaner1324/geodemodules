module org.apache.geode.submodule.four {
  exports org.apache.geode.subService;

  requires core.module;
  requires org.apache.geode.module_service;

  provides org.apache.geode.service.SampleService with
      org.apache.geode.service.mod4.impl.SampleServiceImpl;

  uses org.apache.geode.subService.SampleSubService;
}
