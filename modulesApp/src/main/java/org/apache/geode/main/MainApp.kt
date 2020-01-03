package org.apache.geode.main

import org.apache.geode.service.SampleService
import org.jboss.modules.*
import org.jboss.modules.filter.PathFilters
import java.io.File
import java.util.*
import java.util.jar.JarFile

class MainApp {
    private fun loadImplementationFromServiceLoader(module: Module) {
        try {
            println("Module name:${module.name}")
            val load = ServiceLoader.load(SampleService::class.java, module.classLoader)
            load.forEach { sampleService:SampleService -> println(sampleService.value) }
        } catch (e: Exception) {
        }
    }

    private fun loadModuleFromClasspath() {

    }

    private fun loadModuleFromJar(jarPath: String, moduleName: String, moduleLoader: TestModuleLoader, vararg moduleDependencies: String) {
        val builder: ModuleSpec.Builder = ModuleSpec.build(moduleName)
        moduleDependencies.forEach {
            builder.addDependency(
                    ModuleDependencySpecBuilder()
                            .setName(it)
//                            .setImportFilter(PathFilters.acceptAll())
//                            .setExportFilter(PathFilters.acceptAll())
//                            .setExport(true)
//                            .setImportServices(true)
                            .build())
        }


        builder.addDependency(
                ModuleDependencySpecBuilder()
                        .setName(moduleName)
//                        .setImportFilter(PathFilters.acceptAll())
//                        .setImportServices(true)
                        .build()
        )
        builder.addDependency(
                DependencySpec.createSystemDependencySpec(
                        PathUtils.getPathSet(null)
                )
        )
        // Add the module's own content
        builder.addDependency(DependencySpec.OWN_DEPENDENCY)
        // Add my own module as a dependency

//        builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createJarResourceLoader("coremodule", JarFile(File(CORE_MODULE_PATH), true))))
        builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createJarResourceLoader(moduleName, JarFile(File(jarPath), true))))
        val moduleSpec = builder.create()
        moduleLoader.addModuleSpec(moduleSpec)
    }

    private fun loadCoreModuleFromJar(jarPath: String, moduleLoader: TestModuleLoader, vararg moduleDependencies: String) {
        val builder: ModuleSpec.Builder = ModuleSpec.build("coremodule")

        builder.addDependency(
                ModuleDependencySpecBuilder()
                        .setName("coremodule")
                        .setImportFilter(PathFilters.rejectAll())
//                        .setExportFilter(PathFilters.acceptAll())
//                        .setExport(true)
//                        .setImportServices(true)
                        .build()
        )
        // Add the module's own content
        builder.addDependency(DependencySpec.OWN_DEPENDENCY)
        // Add my own module as a dependency

        builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createJarResourceLoader("coremodule", JarFile(File(CORE_MODULE_PATH), true))))

        val moduleSpec = builder.create()
        moduleLoader.addModuleSpec(moduleSpec)
    }

    private fun loadModuleFromMaven() {}

    companion object {
        //        val CORE_MODULE_PATH = "/tamara2/users/ukohlmeyer/projects/geodemodules/core-module/target/core-module-1.0-SNAPSHOT.jar"
        val CORE_MODULE_PATH = "/Users/ukohlmeyer/projects/geodemodules/core-module/target/core-module-1.0-SNAPSHOT.jar"
        val SUB_MODULE_1_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module1/target/submodule-1-1.0-SNAPSHOT.jar"
        val SUB_MODULE_2_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module2/target/sub-module2-1.0-SNAPSHOT.jar"
        val SUB_MODULE_3_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module3/target/sub-module3-1.0-SNAPSHOT.jar"
        @JvmStatic
        fun main(args: Array<String>) {
            val mainApp = MainApp()
            val rootModuleLoader = TestModuleLoader(ModuleLoader.NO_FINDERS)
            FileSystemClassPathModuleFinder(rootModuleLoader)

            mainApp.loadModuleFromJar(SUB_MODULE_2_PATH, "submodule2", rootModuleLoader)
            mainApp.loadModuleFromJar(SUB_MODULE_1_PATH, "submodule1", rootModuleLoader)
            mainApp.loadModuleFromJar(SUB_MODULE_3_PATH, "submodule3", rootModuleLoader)
            mainApp.loadCoreModuleFromJar(CORE_MODULE_PATH, rootModuleLoader)

            val coreModule = rootModuleLoader.loadModule("coremodule")
            val subModule1 = rootModuleLoader.loadModule("submodule1")
            val subModule2 = rootModuleLoader.loadModule("submodule2")
            val subModule3 = rootModuleLoader.loadModule("submodule3")

            try {
                val mainModuleInterface_Class = coreModule.classLoader.loadClass("org.apache.geode.service.SampleService")
                val mainModule_Class = coreModule.classLoader.loadClass("org.apache.geode.service.impl.SampleServiceImpl")
                val spring_Class = coreModule.classLoader.loadClass("org.springframework.util.StringUtils")
            } catch (e: Exception) {
            }
            try {
                val mainModule_Class = subModule1.classLoader.loadClass("org.apache.geode.service.impl.SampleServiceImpl")
                val mainModuleInterface_Class = subModule1.classLoader.loadClass("org.apache.geode.service.SampleService")
                val spring_Class = subModule1.classLoader.loadClass("org.springframework.util.StringUtils")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val mainModule_Class = subModule2.classLoader.loadClass("org.apache.geode.service.impl.SampleServiceImpl")
                val mainModuleInterface_Class = subModule2.classLoader.loadClass("org.apache.geode.service.SampleService")
                val spring_Class = subModule2.classLoader.loadClass("org.springframework.util.StringUtils")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val mainModule_Class = subModule3.classLoader.loadClass("org.apache.geode.service.impl.SampleServiceImpl")
                val mainModuleInterface_Class = subModule3.classLoader.loadClass("org.apache.geode.service.SampleService")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                mainApp.loadImplementationFromServiceLoader(coreModule)
                mainApp.loadImplementationFromServiceLoader(subModule1)
                mainApp.loadImplementationFromServiceLoader(subModule2)
                mainApp.loadImplementationFromServiceLoader(subModule3)
            } catch (e: Exception) {
            }
        }
    }
}

class TestModuleLoader(moduleFinder: Array<ModuleFinder>) : DelegatingModuleLoader(Module.getSystemModuleLoader(), moduleFinder) {
    private val moduleSpecs: MutableMap<String, ModuleSpec> = HashMap()
    override fun findModule(name: String): ModuleSpec {
        return moduleSpecs[name]!!
    }

    fun addModuleSpec(moduleSpec: ModuleSpec) {
        moduleSpecs[moduleSpec.name] = moduleSpec
    }

    override fun toString(): String {
        return "test@" + System.identityHashCode(this)
    }
}
