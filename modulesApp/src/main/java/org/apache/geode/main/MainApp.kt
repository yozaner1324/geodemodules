package org.apache.geode.main

import org.apache.geode.module.service.ModuleService
import org.apache.geode.module.service.impl.JBossModuleServiceImpl
import org.apache.geode.service.SampleService
import org.jboss.modules.Module
import org.jboss.modules.maven.ArtifactCoordinates
import java.io.File
import java.lang.reflect.Method
import java.util.*

class MainApp(private val moduleService: ModuleService = JBossModuleServiceImpl()) {
    private fun loadImplementationFromServiceLoader(clazz: Class<SampleService>) {
        try {
            val sampleServices = moduleService.loadService(clazz)
            sampleServices.forEach { println(it.value) }
            println("Found ${sampleServices.size} implementations of ${clazz.name}")
        } catch (e: Exception) {
        }
    }

    companion object {
//        private const val SUB_MODULE_1_PATH = "/Users/patrickjohnson/Documents/GitHub/geodemodules/sub-module1/target/sub-module1-1.0-SNAPSHOT.jar"
//        private const val SUB_MODULE_2_PATH = "/Users/patrickjohnson/Documents/GitHub/geodemodules/sub-module2/target/sub-module2-1.0-SNAPSHOT.jar"
//        private const val SUB_MODULE_3_PATH = "/Users/patrickjohnson/Documents/GitHub/geodemodules/sub-module3/target/sub-module3-1.0-SNAPSHOT.jar"
//        private const val SUB_MODULE_4_PATH = "/Users/patrickjohnson/Documents/GitHub/geodemodules/sub-module4/target/sub-module4-1.0-SNAPSHOT.jar"
//        private const val SUB_MODULE_5_PATH = "/Users/patrickjohnson/Documents/GitHub/geodemodules/sub-module5/target/sub-module5-1.0-SNAPSHOT.jar"

        @JvmStatic
        fun main(args: Array<String>) {
            val mainApp = MainApp()

//            val dir = File(File("").absolutePath)
//            dir.listFiles().forEach { file ->
//                if(file.name.endsWith("-info.txt")) {
//                    val moduleName = file.name.substringBefore("-info.txt")
//                    var deps = LinkedList<String>()
//                    file.readLines().forEach { line ->
//                        val fields = line.split("\t")
//                        if(fields[0] == "project") {
//                            deps.add(fields[1])
//                        }
//                    }
//
//                }
//            }
//            File("moduleDeps.txt").readLines().forEach { line ->
//                val fields = line.split("\t")
//                if(fields[0] == "project") {
//                    mainApp.registerModuleFromJar(ArtifactCoordinates(fields[0], fields[1], fields[2]), fields[3])
//                } else if(fields.size > 4) {
//                    mainApp.registerModuleFromJar(ArtifactCoordinates(fields[0], fields[1], fields[2]), fields[3], *fields.subList(4, fields.size).toTypedArray())
//                }
//            }

            mainApp.registerModuleFromName("sub-module1")
            mainApp.registerModuleFromName("sub-module2")
            mainApp.registerModuleFromName("sub-module3")
            mainApp.registerModuleFromName("sub-module4")
            mainApp.registerModuleFromName("sub-module5")

//            mainApp.registerModuleFromJar(ArtifactCoordinates("org.apache.geode", "sub-module2", "1.0-SNAPSHOT"), "submodule2")
//            mainApp.registerModuleFromJar(ArtifactCoordinates("org.apache.geode", "sub-module3", "1.0-SNAPSHOT"), "submodule3")
//          //  mainApp.registerModuleFromJar(SUB_MODULE_4_PATH, "submodule4")
//            mainApp.registerModuleFromJar(ArtifactCoordinates("org.apache.geode", "sub-module4", "1.0-SNAPSHOT"), "combined", "submodule1", "submodule2", "submodule3")
//            mainApp.registerModuleFromJar(ArtifactCoordinates("org.apache.geode", "sub-module5", "1.0-SNAPSHOT"), "submodule5", "combined")

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

    private fun registerModuleFromName(name: String) {
        moduleService.registerModuleFromName(name)
    }
}


