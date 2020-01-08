package org.apache.geode.module.service

import org.apache.geode.service.SampleService
import org.jboss.modules.Module

interface ModuleService {
    fun loadClass(className: String): Class<*>?
    fun loadService(clazz: Class<out SampleService>): List<SampleService>
    fun registerModuleFromJar(moduleName: String, jarPath: String, vararg dependentComponents: String)
    fun loadModule(moduleName: String): Module
}
