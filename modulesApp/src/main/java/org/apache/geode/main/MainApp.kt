package org.apache.geode.main

import org.apache.geode.module.service.ModuleService
import org.apache.geode.module.service.impl.JBossModuleServiceImpl
import org.apache.geode.service.SampleService
import org.jboss.modules.Module
import org.jboss.modules.maven.ArtifactCoordinates
import java.lang.reflect.Method

class MainApp(private val moduleService: ModuleService = JBossModuleServiceImpl()) {
    private fun loadImplementationFromServiceLoader(clazz: Class<SampleService>) {
        println("****SampleService Implementations****")
        try {
            val sampleServices = moduleService.loadService(clazz)
            sampleServices.forEach { println(it.value) }
        } catch (e: Exception) {
        }
        println("*************************************")
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val mainApp = MainApp()

            mainApp.registerModuleForName("sub-module1")
            mainApp.registerModuleForName("sub-module2")
            mainApp.registerModuleForName("sub-module3")
            mainApp.registerModuleForName("sub-module4")
            mainApp.registerModuleForName("sub-module5")

            val subModule1 = mainApp.loadModule("sub-module1")
            val subModule2 = mainApp.loadModule("sub-module2")
            val subModule3 = mainApp.loadModule("sub-module3")
            val subModule4 = mainApp.loadModule("sub-module4")
            val subModule5 = mainApp.loadModule("sub-module5")

            testClassLeakage(subModule1)
            testClassLeakage(subModule2)
            testClassLeakage(subModule3)
            testClassLeakage(subModule4)
            testClassLeakage(subModule5)

            mainApp.loadImplementationFromServiceLoader(SampleService::class.java)

            val o: Any = mainApp.loadClass("org.apache.geode.subService.impl.DomainObject")!!.newInstance()
            val method: Method = o.javaClass.getMethod("getValue")
            println(method.invoke(o))
        }

        private fun testClassLeakage(module: Module) {
            checkClassAndLogError(module, "org.apache.geode.service.impl.SampleServiceImpl")
            checkClassAndLogError(module, "org.apache.geode.service.SampleService")
            checkClassAndLogError(module, "org.apache.geode.subService.SampleSubService")
            checkClassAndLogError(module, "org.apache.geode.subService.impl.SampleSubServiceImpl")
            checkClassAndLogError(module, "org.springframework.util.StringUtils")
            checkClassAndLogError(module, "org.apache.commons.lang3.StringUtils")
            checkClassAndLogError(module, "com.google.common.base.Strings")
            checkClassAndLogError(module, "org.apache.geode.subService.impl.DomainObject")

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

    private fun loadModule(moduleName: String): Module = moduleService.loadModule(moduleName)

    private fun loadClass(className: String): Class<*>? = moduleService.loadClass(className)

    private fun registerModuleFromJar(coordinates: ArtifactCoordinates, name: String, vararg dependentModules: String) {
        moduleService.registerModuleFromJar(coordinates, name, *dependentModules)
    }

    private fun registerModuleForName(name: String) {
        moduleService.registerModuleForName(name)
    }
}