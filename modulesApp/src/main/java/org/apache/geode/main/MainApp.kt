package org.apache.geode.main

import org.apache.geode.module.service.ModuleService
import org.apache.geode.module.service.impl.JBossModuleServiceImpl
import org.apache.geode.service.SampleService
import org.jboss.modules.Module

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
        private const val SUB_MODULE_1_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module1/target/sub-module1-1.0-SNAPSHOT.jar"
        private const val SUB_MODULE_2_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module2/target/sub-module2-1.0-SNAPSHOT.jar"
        private const val SUB_MODULE_3_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module3/target/sub-module3-1.0-SNAPSHOT.jar"
        private const val SUB_MODULE_4_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module4/target/sub-module4-1.0-SNAPSHOT.jar"
        private const val SUB_MODULE_5_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module5/target/sub-module5-1.0-SNAPSHOT.jar"
        private const val SPRING_MODULE_PATH = "/Users/ukohlmeyer/projects/geodemodules/spring-submodule/target/spring-submodule-1.0-SNAPSHOT.jar"
        private const val SPRING_MODULE_2_PATH = "/Users/ukohlmeyer/projects/geodemodules/spring-submodule2/target/spring-submodule2-1.0-SNAPSHOT.jar"

        @JvmStatic
        fun main(args: Array<String>) {
            val mainApp = MainApp()

            mainApp.registerModuleFromJar(SUB_MODULE_1_PATH, "submodule1")
            mainApp.registerModuleFromJar(SUB_MODULE_2_PATH, "submodule2")
            mainApp.registerModuleFromJar(SUB_MODULE_3_PATH, "submodule3")
            mainApp.registerModuleFromJar(SUB_MODULE_4_PATH, "submodule4")
            mainApp.registerModuleFromJar(SUB_MODULE_5_PATH, "submodule5", "submodule4")
            mainApp.registerModuleFromJar(SPRING_MODULE_PATH, "springModule")
            mainApp.registerModuleFromJar(SPRING_MODULE_2_PATH, "springModule2")

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


