package org.jboss.modules

import java.io.File
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarFile

class MainApp {
    private fun loadImplementationFromServiceLoader() {
//        val load = ServiceLoader.load(SampleService::class.java)
//        load.forEach { sampleService: SampleService -> println(sampleService.value) }
    }

    private fun loadModuleFromClasspath() {

    }

    private fun loadModuleFromJar(jarPath: String, moduleName: String, moduleLoader: TestModuleLoader, vararg moduleDependencies: String) {
//        var basePath: Path? = Paths.get(ClassLoader.getSystemResource("coremodule.jar").toURI())


        val jarFile = JarFile(File(jarPath), true)
        // xsd:all
        val jarResourceLoader = ResourceLoaders.createJarResourceLoader(moduleName, jarFile)
        val resourceLoaderSpec = ResourceLoaderSpec.createResourceLoaderSpec(jarResourceLoader)

        val builder: ModuleSpec.Builder = ModuleSpec.build(ModuleIdentifier.fromString(moduleName))
        builder.addDependency(
                ModuleDependencySpecBuilder()
                        .setName(moduleName)
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

        moduleDependencies.forEach {
            builder.addDependency(ModuleDependencySpecBuilder().setName(it).build())
        }


        builder.addResourceRoot(resourceLoaderSpec)
        val moduleSpec = builder.create()
        moduleLoader.addModuleSpec(moduleSpec)
    }

    private fun loadModuleFromMaven() {}

    companion object {
        //        val CORE_MODULE_PATH = "/tamara2/users/ukohlmeyer/projects/geodemodules/core-module/target/core-module-1.0-SNAPSHOT.jar"
        val CORE_MODULE_PATH = "/Users/ukohlmeyer/projects/geodemodules/core-module/target/core-module-1.0-SNAPSHOT.jar"
        val SUB_MODULE_1_PATH = "/Users/ukohlmeyer/projects/geodemodules/sub-module1/target/submodule-1-1.0-SNAPSHOT.jar"
        @JvmStatic
        fun main(args: Array<String>) {
            val mainApp = MainApp()
            val rootModuleLoader = TestModuleLoader(ModuleLoader.NO_FINDERS)
            FileSystemClassPathModuleFinder(rootModuleLoader)
            val rootPath = Paths.get("").toAbsolutePath()
            val moduleName: String
            val className: String
            Module.initBootModuleLoader(rootModuleLoader)
            mainApp.loadModuleFromJar(CORE_MODULE_PATH, "coremodule", rootModuleLoader)
            mainApp.loadModuleFromJar(SUB_MODULE_1_PATH, "submodule1", rootModuleLoader, "coremodule")

            val coreModule = rootModuleLoader.loadModule("coremodule")
            val subModule = rootModuleLoader.loadModule("submodule1")
            try {
                val mainModuleInterface_Class = coreModule.classLoader.loadClass("org.apache.geode.service.SampleService");
                val mainModule_Class = coreModule.classLoader.loadClass("org.apache.geode.service.impl.SampleServiceImpl");
            } catch (e: Exception) {
            }
            try {
                val subModule_Class = subModule.classLoader.loadClass("org.apache.geode.service.impl.SampleServiceImpl");
                val mainModuleInterface_Class = subModule.classLoader.loadClass("org.apache.geode.service.SampleService");
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
