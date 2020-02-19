package org.apache.geode.spring.module.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.apache.geode.spring.module.controller.DemoController;

@Configuration
@ComponentScan(basePackageClasses = DemoController.class)
@EnableWebMvc
public class DemoAppConfig {
}
