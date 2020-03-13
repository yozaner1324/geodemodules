import org.gradle.api.Plugin
import org.gradle.api.Project

class ModuleDependenciesPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task('hello') {
            println 'Hello  World'
        }
    }
}