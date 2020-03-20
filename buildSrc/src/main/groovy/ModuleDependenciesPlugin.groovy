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

                project.subprojects.forEach({ sub ->

                    File file = new File(sub.name + "/" + sub.name + "-info.txt")
                    file.write("root\t" + pathForName(project, sub.name))

                    sub.configurations.default.resolvedConfiguration.firstLevelModuleDependencies.forEach({ dep ->

                        ComponentIdentifier artifactId = (dep.moduleArtifacts[0] as DefaultResolvedArtifact).id.componentIdentifier

                        if(artifactId instanceof DefaultProjectComponentIdentifier) {
                           file << "\nproject\t" << dep.moduleName << "\t" << pathForName(project, dep.moduleName)
                        } else {
                            file << "\nartifact\t" << dep.moduleGroup << "\t" << dep.moduleName << "\t" << dep.moduleVersion
                        }
                    })
                })
            }
        }
    }
}