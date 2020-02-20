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

            mainApp.unloadModule("submodule1")
            mainApp.unloadModule("submodule2")
            mainApp.unloadModule("submodule3")
            mainApp.unloadModule("submodule4")
            mainApp.unloadModule("submodule5")
            mainApp.unloadModule("springModule")
            mainApp.unloadModule("springModule2")

        }


        private fun testClassLeakage(module: Module) {
            checkClassAndLogError(module, "org.apache.geode.service.impl.SampleServiceImpl")
            checkClassAndLogError(module, "org.apache.geode.service.SampleService")
            checkClassAndLogError(module, "org.apache.geode.subService.SampleSubService")
            checkClassAndLogError(module, "org.apache.geode.subService.impl.SampleSubServiceImpl")
            checkClassAndLogError(module, "org.springframework.util.StringUtils")
            checkClassAndLogError(module, "org.apache.commons.lang3.StringUtils")
            checkClassAndLogError(module, "com.google.common.base.Strings")
            println("<< ------------------------ >>")
        }

        private fun checkClassAndLogError(module: Module, classString: String) {
            try {
                module.classLoader.loadClass(classString)
            } catch (e: Exception) {
                println("${module.name} cannot find $classString")
            }
        }
    }

    private fun unloadServices() {
        moduleService.unloadServices()
    }

    private fun unloadModule(moduleName: String) {
        moduleService.unloadModule(moduleName)
    }

    private fun loadModule(moduleName: String): Module = moduleService.loadModule(moduleName)


    private fun registerModuleFromJar(modulePath: String, name: String, vararg dependentModules: String) {
        moduleService.registerModuleFromJar(modulePath, name, *dependentModules)
    }
}


