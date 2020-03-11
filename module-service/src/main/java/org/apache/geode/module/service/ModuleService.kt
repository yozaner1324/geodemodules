package org.apache.geode.module.service

import org.apache.geode.service.SampleService
import org.jboss.modules.Module
import org.jboss.modules.maven.ArtifactCoordinates

interface ModuleService {
    fun loadClass(className: String): Class<*>?
    fun loadService(clazz: Class<out SampleService>): List<SampleService>
    fun registerModuleFromJar(coordinates: ArtifactCoordinates, moduleName: String, vararg dependentComponents: String)
    fun loadModule(moduleName: String): Module
}
