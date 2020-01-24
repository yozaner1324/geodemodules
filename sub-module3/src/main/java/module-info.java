module org.apache.geode.submodule.three {
  requires core.module;
  requires spring.core;

  provides org.apache.geode.service.SampleService with
      org.apache.geode.service.mod3.impl.SampleServiceImpl;
}
