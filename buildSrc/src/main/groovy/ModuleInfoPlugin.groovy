/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.internal.artifacts.DefaultProjectComponentIdentifier

class ModuleInfoPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.task('generateModuleInfo') {

            doLast {
                new File(project.buildDir.path + "/modules-info").mkdir()
                project.subprojects.forEach({ sub ->
                    if(sub.buildDir.exists()) {
                        Map<String, Boolean> entries = new HashMap<>()

                        sub.configurations.forEach({ x->
                            if(x.isCanBeResolved() && (x.name == "compileClasspath" || x.name == "runtimeClasspath")) {
                                x.resolvedConfiguration.firstLevelModuleDependencies.forEach({ dep ->
                                    if (!dep.moduleArtifacts.empty) {
                                        ComponentIdentifier artifactId = dep.moduleArtifacts[0].id.componentIdentifier

                                        if (artifactId instanceof DefaultProjectComponentIdentifier) {
                                            entries.putIfAbsent("\nproject\t" + dep.moduleName, true)
                                        } else {
                                            entries.putIfAbsent("\nartifact\t" + dep.moduleGroup + "\t" + dep.moduleName + "\t" + dep.moduleVersion, true)
                                        }
                                    }
                                })
                            }
                        })

                        File file = new File(project.buildDir.path + "/modules-info/" + sub.name + "-info.txt")
                        file.write("root\t" + sub.buildDir.path)
                        entries.forEach({key, value ->
                            file << key
                        })
                    }
                })
            }
        }
    }
}