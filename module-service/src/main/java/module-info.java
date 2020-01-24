module org.apache.geode.module_service {
  exports org.apache.geode.module.service;

  requires kotlin.stdlib.jdk8;
  requires kotlin.stdlib.common;
  requires kotlin.stdlib.jdk7;
  requires kotlin.stdlib;
  requires java.base;
  requires java.compiler;

  requires core.module;

  provides org.apache.geode.module.service.ModuleService with
      org.apache.geode.module.service.impl.JavaModuleServiceImpl;
}
