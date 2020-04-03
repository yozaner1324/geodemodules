package org.apache.geode.module.service.impl

import org.apache.geode.module.service.ModuleService
import org.apache.geode.service.SampleService
import org.jboss.modules.*
import org.jboss.modules.filter.PathFilters
import org.jboss.modules.maven.ArtifactCoordinates
import org.jboss.modules.maven.MavenArtifactUtil
import java.io.File
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarFile

class JBossModuleServiceImpl(private val moduleLoader: TestModuleLoader = TestModuleLoader()) : ModuleService {
    private val moduleMap: MutableMap<String, Module> = mutableMapOf()
    private val modulesList: MutableList<String> = mutableListOf();

    override fun loadClass(className: String): Class<*> {
        var clazz: Class<*>
        for (module in moduleMap.values) {
            try {
                clazz = module.classLoader.loadClass(className)
                return clazz
            } catch (e: Exception) {
            }
        }
        throw Exception("No class named $className found")
    }

    override fun loadService(clazz: Class<out SampleService>): List<SampleService> {
        val returnList = mutableListOf<SampleService>()
        if (moduleMap.isEmpty()) {
            modulesList.forEach { moduleName -> moduleMap[moduleName] = loadModule(moduleName) }
        }
        moduleMap.values.forEach { module ->
                ServiceLoader.load(clazz, module.classLoader)
                    .forEach {
                        it.init(this)
                        returnList.add(it)
                    }
        }
        return returnList
    }

    override fun registerModuleFromJar(coordinates: ArtifactCoordinates, moduleName: String, vararg dependentComponents: String) {

        if(modulesList.contains(moduleName)) return

        val builder: ModuleSpec.Builder = ModuleSpec.build(moduleName)

        // Add the module's own content
        builder.addDependency(LocalDependencySpecBuilder()
                .setExportFilter(PathFilters.isOrIsChildOf("org/apache/geode"))
                .setImportServices(true)
                .setExport(true)
                .build())

        dependentComponents.forEach {
            builder.addDependency(
                    ModuleDependencySpecBuilder()
                            .setName(it)
                            .build())
        }

        builder.addDependency(DependencySpec
                .createSystemDependencySpec(PathUtils.getPathSet(null)))

        val jarFile = JarFile(MavenArtifactUtil.resolveArtifact(coordinates, "jar"))
        builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createJarResourceLoader(jarFile)))
        val moduleSpec = builder.create()
        moduleLoader.addModuleSpec(moduleSpec)

        modulesList.add(moduleName)
    }

    override fun registerModuleFromPath(path: String, moduleName: String, vararg dependentComponents: String) {

        if(modulesList.contains(moduleName)) return

        val builder: ModuleSpec.Builder = ModuleSpec.build(moduleName)

// Add the module's own content
        builder.addDependency(LocalDependencySpecBuilder()
                .setExportFilter(PathFilters.isOrIsChildOf("org/apache/geode"))
                .setImportServices(true)
                .setExport(true)
                .build())

        dependentComponents.forEach {
            builder.addDependency(
                    ModuleDependencySpecBuilder()
                            .setName(it)
                            .build())
        }

        builder.addDependency(DependencySpec
                .createSystemDependencySpec(PathUtils.getPathSet(null)))

        builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createPathResourceLoader(Paths.get(path))))
        val moduleSpec = builder.create()
        moduleLoader.addModuleSpec(moduleSpec)

        modulesList.add(moduleName)
    }

    override fun registerModuleForName(moduleName: String) {
        if(modulesList.contains(moduleName)) return

        val builder: ModuleSpec.Builder = ModuleSpec.build(moduleName)

        // Add the module's own content
        builder.addDependency(LocalDependencySpecBuilder()
                .setExportFilter(PathFilters.isOrIsChildOf("org/apache/geode"))
                .setImportServices(true)
                .setExport(true)
                .build())

        builder.addDependency(DependencySpec.createSystemDependencySpec(PathUtils.getPathSet(null)))

        val file = File("build/modules-info/$moduleName-info.txt")
        file.readLines().forEach { line ->
            val fields = line.split("\t")
            if(fields[0] == "root") {
                builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createPathResourceLoader(Paths.get(fields[1] +"/classes/java/main"))))
                builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(ResourceLoaders.createPathResourceLoader(Paths.get(fields[1] +"/resources/main"))))
            } else if(fields[0] == "project") {
                builder.addDependency(ModuleDependencySpecBuilder().setName(createModuleIfNotExists(fields[1])).build())
            } else if(fields[0] == "artifact") {
                val name = fields[1] + fields[2] + fields[3]
                if(!modulesList.contains(name)) {
                    registerModuleFromJar(ArtifactCoordinates(fields[1], fields[2], fields[3]), name)
                }
                builder.addDependency(ModuleDependencySpecBuilder().setName(name).build())
            }
        }

        val moduleSpec = builder.create()
        moduleLoader.addModuleSpec(moduleSpec)

        modulesList.add(moduleName)
    }

    private fun createModuleIfNotExists(name: String): String {
        if(!modulesList.contains(name)) {
            registerModuleForName(name)
        }
        return name
    }

    override fun loadModule(moduleName: String): Module = moduleLoader.loadModule(moduleName)

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
