package org.apache.geode.module.service;

import java.util.Iterator;

import org.apache.geode.service.SampleService;

public interface ModuleService {
  public Class<?> loadClass(String className);

  public Iterator<? extends SampleService> loadService(Class<? extends SampleService> clazz);

  public void registerModuleFromJar(String moduleName, String jarPath,
                                    String... dependentComponents);
}
