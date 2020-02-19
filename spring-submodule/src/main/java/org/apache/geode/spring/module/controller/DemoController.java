package org.apache.geode.spring.module.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @GetMapping("/ping")
  @ResponseStatus(HttpStatus.OK)
  public String index() {
    return "Pong!";
  }
}
