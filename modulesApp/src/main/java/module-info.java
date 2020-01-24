module org.apache.geode.modulesApp {
  requires core.module;
  requires org.apache.geode.module_service;

  requires org.apache.geode.submodule.one;
  requires org.apache.geode.submodule.two;
  requires org.apache.geode.submodule.three;
  requires org.apache.geode.submodule.four;
  requires org.apache.geode.submodule.five;

  uses org.apache.geode.service.SampleService;
}
