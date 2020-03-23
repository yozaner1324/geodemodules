import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.internal.artifacts.DefaultProjectComponentIdentifier
import org.gradle.api.internal.artifacts.DefaultResolvedArtifact

class ModuleDependenciesPlugin implements Plugin<Project> {

    static String pathForName(Project project, String dependency) {
        String path = null
        project.subprojects.forEach({sub ->
            if(dependency == sub.name) {
                path = sub.buildDir.path
            }
        })
        return path
    }
    void apply(Project project) {
        project.task('recordDeps') {

            doLast {
                File modulesFile = new File("modules.txt")
                project.subprojects.forEach({ sub ->
                    modulesFile << sub.name << "\n"
                    File infoFile = new File(sub.name + "/" + sub.name + "-info.txt")
                    infoFile.write("root\t" + pathForName(project, sub.name))

                    sub.configurations.default.resolvedConfiguration.firstLevelModuleDependencies.forEach({ dep ->

                        ComponentIdentifier artifactId = (dep.moduleArtifacts[0] as DefaultResolvedArtifact).id.componentIdentifier

                        if(artifactId instanceof DefaultProjectComponentIdentifier) {
                           infoFile << "\nproject\t" << dep.moduleName << "\t" << pathForName(project, dep.moduleName)
                        } else {
                            infoFile << "\nartifact\t" << dep.moduleGroup << "\t" << dep.moduleName << "\t" << dep.moduleVersion
                        }
                    })
                })
            }
        }
    }
}