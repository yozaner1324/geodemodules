package org.apache.geode.service;

public interface SampleService {
	default String getValue() { return "No value set"; }
	default void init(Object... initObjects){};
}
