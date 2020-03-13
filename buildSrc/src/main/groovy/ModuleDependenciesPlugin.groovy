import org.gradle.api.Plugin
import org.gradle.api.Project

class ModuleDependenciesPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task('recordDeps') {

            File file = new File("moduleDeps.txt")
            file.write ""

            // TODO: Replace with a better schema probably
            file << "org.apache.geode\t" << "sub-module1\t" << "1.0-SNAPSHOT\t" << "submodule1" << "\n"
            file << "org.apache.geode\t" << "sub-module2\t" << "1.0-SNAPSHOT\t" << "submodule2" << "\n"
            file << "org.apache.geode\t" << "sub-module3\t" << "1.0-SNAPSHOT\t" << "submodule3" << "\n"
            file << "org.apache.geode\t" << "sub-module4\t" << "1.0-SNAPSHOT\t" << "combined\t" << "submodule1\t" << "submodule2\t" << "submodule3" << "\n"
            file << "org.apache.geode\t" << "sub-module5\t" << "1.0-SNAPSHOT\t" << "submodule5\t" << "combined" << "\n"
        }
    }
}