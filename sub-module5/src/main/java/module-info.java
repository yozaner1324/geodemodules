module org.apache.geode.submodule.five {
  requires core.module;
  requires org.apache.geode.submodule.four;

  provides org.apache.geode.subService.SampleSubService with
      org.apache.geode.subService.impl.SampleSubServiceImpl;
}
