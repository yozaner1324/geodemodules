package org.jboss.modules

import org.apache.geode.service.SampleService
import java.io.File
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarFile

class MainApp {
    private fun loadImplementationFromServiceLoader() {
        val load = ServiceLoader.load(SampleService::class.java)
        load.forEach { sampleService: SampleService -> println(sampleService.value) }
    }

    private fun loadModuleFromClasspath() {

    }

    private fun loadModuleFromJar(moduleLoader: TestModuleLoader) {
//        var basePath: Path? = Paths.get(ClassLoader.getSystemResource("coremodule.jar").toURI())


        val jarFile = JarFile(File(CORE_MODULE_PATH), true)
        val jarResourceLoader = ResourceLoaders.createJarResourceLoader("coremodule", jarFile)
        val resourceLoaderSpec = ResourceLoaderSpec.createResourceLoaderSpec(jarResourceLoader)
        val builder: ModuleSpec.Builder = ModuleSpec.build(ModuleIdentifier.fromString("coremodule"))
        builder.addResourceRoot(resourceLoaderSpec)
        builder.addDependency(DependencySpec.createLocalDependencySpec())
        val moduleSpec = builder.create()
        moduleLoader.addModuleSpec(moduleSpec)
    }

    private fun loadModuleFromMaven() {}

    companion object {
        val CORE_MODULE_PATH = "/tamara2/users/ukohlmeyer/projects/geodemodules/core-module/target/core-module-1.0-SNAPSHOT.jar"
        @JvmStatic
        fun main(args: Array<String>) {
            val mainApp = MainApp()
            val environmentLoader: ModuleLoader = DefaultBootModuleLoaderHolder.INSTANCE
            val moduleLoader = TestModuleLoader(FileSystemClassPathModuleFinder(environmentLoader))
//            val moduleLoader = ModuleLoader(FileSystemClassPathModuleFinder(environmentLoader))
            val rootPath = Paths.get("").toAbsolutePath()
            val moduleName: String
            val className: String
            Module.initBootModuleLoader(environmentLoader)
            mainApp.loadModuleFromJar(moduleLoader)

            val coreModule = moduleLoader.loadModule("coremodule")
            coreModule.classLoader.getResource(SampleService::class.java.name);
            val serviceLoader = ServiceLoader.load(SampleService::class.java, coreModule.classLoader)
            serviceLoader.iterator().forEach { t: SampleService -> print(t.value) }
        }
    }
}

class TestModuleLoader(moduleFinder: ModuleFinder) : DelegatingModuleLoader(Module.getSystemModuleLoader(), moduleFinder) {
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