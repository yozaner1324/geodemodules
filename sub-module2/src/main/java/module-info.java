import org.apache.geode.service.mod2.impl.SampleServiceImpl;

module org.apache.geode.submodule.two {
  requires core.module;
  requires org.apache.commons.lang3;

  provides org.apache.geode.service.SampleService with
      org.apache.geode.service.mod2.impl.SampleServiceImpl;
}
