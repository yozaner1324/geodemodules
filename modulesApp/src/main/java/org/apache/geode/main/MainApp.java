package org.apache.geode.main;

import java.util.ServiceLoader;

import org.apache.geode.service.SampleService;

public class MainApp {
  public static void main(String[] args) {

    var mainApp = new MainApp();

    mainApp.loadImplementationFromServiceLoader(SampleService.class);
    mainApp.testClassLeakage();
  }

  private void loadImplementationFromServiceLoader(Class<SampleService> clazz) {
    ServiceLoader.load(clazz)
        .forEach(sampleService -> System.out.println(sampleService.getValue()));
  }

  private void testClassLeakage() {
    checkClassAndLogError("org.apache.geode.service.mod1.impl.SampleServiceImpl");
    checkClassAndLogError("org.apache.geode.service.mod2.impl.SampleServiceImpl");
    checkClassAndLogError("org.apache.geode.service.mod3.impl.SampleServiceImpl");
    checkClassAndLogError("org.apache.geode.service.mod4.impl.SampleServiceImpl");
    checkClassAndLogError("org.apache.geode.service.SampleService");
    checkClassAndLogError("org.apache.geode.subService.SampleSubService");
    checkClassAndLogError("org.apache.geode.subService.impl.SampleSubServiceImpl");
    checkClassAndLogError("org.springframework.util.StringUtils");
    checkClassAndLogError("org.apache.commons.lang3.StringUtils");
    checkClassAndLogError("com.google.common.base.Strings");
    System.out.println("<< ------------------------ >>");
  }

  private void checkClassAndLogError(String classString) {
    try {
      this.getClass().getClassLoader().loadClass(classString);
      System.out.println("Found and loaded class: " + classString);
    } catch (Exception e) {
      System.err.println("Cannot find: " + classString);
    }
  }
}
