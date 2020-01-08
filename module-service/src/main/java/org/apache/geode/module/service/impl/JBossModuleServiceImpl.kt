package org.apache.geode.module.service.impl

import org.apache.geode.module.service.ModuleService
import org.apache.geode.service.SampleService
import org.jboss.modules.*
import org.jboss.modules.filter.PathFilters
import java.io.File
import java.util.*
import java.util.jar.JarFile

class JBossModuleServiceImpl(private val moduleLoader: TestModuleLoader = TestModuleLoader()) : ModuleService {
    private val moduleMap: MutableMap<String, Module> = mutableMapOf()
    private val modulesList: MutableList<String> = mutableListOf();

    override fun loadClass(className: String): Class<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadService(clazz: Class<out SampleService>): List<SampleService> {
        val returnList = mutableListOf<SampleService>()
        if (moduleMap.isEmpty()) {
            modulesList.forEach { moduleName -> moduleMap[moduleName] = loadModule(moduleName) }
        }
        moduleMap.values.forEach { module ->
            println("Module name: ${module.name}")
            ServiceLoader.load(clazz, module.classLoader)
                    .forEach {
                        it.init(this)
                        returnList.add(it)
                    }
        }
        return returnList
    }

    override fun registerModuleFromJar(jarPath: String, moduleName: String, vararg dependentComponents: String) {
        val builder: ModuleSpec.Builder = ModuleSpec.build(moduleName)

        // Add the module's own content
        builder.addDependency(LocalDependencySpecBuilder()
                .setExportFilter(PathFilters.isOrIsChildOf("org/apache/geode"))
//                .setImportFilter(PathFilters.isOrIsChildOf("org/apache/geode"))
                .setImportServices(true)
                .setExport(true)
                .build())

        dependentComponents.forEach {
            builder.addDependency(
                    ModuleDependencySpecBuilder()
//                            .setImportFilter(PathFilters.isOrIsChildOf("org/apache/geode"))
//                            .setImportServices(true)
                            .setName(it)
                            .build())
        }

        builder.addDependency(DependencySpec
                .createSystemDependencySpec(PathUtils.getPathSet(null)))

        builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createJarResourceLoader(moduleName, JarFile(File(jarPath), true))))
        val moduleSpec = builder.create()
        moduleLoader.addModuleSpec(moduleSpec)

        modulesList.add(moduleName);
    }

    override fun loadModule(moduleName: String): Module = moduleLoader.loadModule(moduleName)

    private fun loadImplementationFromServiceLoader(module: Module) {

    }
}

class TestModuleLoader(moduleFinder: Array<ModuleFinder> = ModuleLoader.NO_FINDERS) : DelegatingModuleLoader(Module.getSystemModuleLoader(), moduleFinder) {
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
