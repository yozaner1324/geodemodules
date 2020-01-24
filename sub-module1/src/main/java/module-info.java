module org.apache.geode.submodule.one {

  requires core.module;
  requires com.google.common;

  provides org.apache.geode.service.SampleService with
      org.apache.geode.service.mod1.impl.SampleServiceImpl;
}
