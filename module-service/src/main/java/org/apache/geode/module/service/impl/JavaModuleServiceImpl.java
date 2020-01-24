package org.apache.geode.module.service.impl;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.apache.geode.module.service.ModuleService;
import org.apache.geode.service.SampleService;

public class JavaModuleServiceImpl implements ModuleService {
  @Override
  public Class<?> loadClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Iterator<? extends SampleService> loadService(Class<? extends SampleService> clazz) {
    return ServiceLoader.load(clazz).iterator();
  }

  @Override
  public void registerModuleFromJar(String moduleName, String jarPath,
                                    String... dependentComponents) {

  }
}
