package org.apache.geode.main

import org.apache.geode.module.service.ModuleService
import org.apache.geode.module.service.impl.JBossModuleServiceImpl
import org.apache.geode.service.SampleService

class MainApp(private val moduleService: ModuleService = JBossModuleServiceImpl()) {
    private fun loadImplementationFromServiceLoader(clazz: Class<SampleService>) {
        try {
            val sampleServices = moduleService.loadService(clazz)
            sampleServices.forEach { println(it.value) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val SPRING_MODULE_PATH = "/Users/ukohlmeyer/projects/geodemodules/spring-submodule/target/spring-submodule-1.0-SNAPSHOT.jar"

        @JvmStatic
        fun main(args: Array<String>) {
            val mainApp = MainApp()

            mainApp.registerModuleFromJar(SPRING_MODULE_PATH, "springModule")

            mainApp.loadImplementationFromServiceLoader(SampleService::class.java)
            mainApp.unloadServices()
        }
    }

    private fun unloadServices() {
        moduleService.unloadServices()
    }

    private fun registerModuleFromJar(modulePath: String, name: String, vararg dependentModules: String) {
        moduleService.registerModuleFromJar(modulePath, name, *dependentModules)
    }
}


