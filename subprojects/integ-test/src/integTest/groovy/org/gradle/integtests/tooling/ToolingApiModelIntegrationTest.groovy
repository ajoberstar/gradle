/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.integtests.tooling

import org.gradle.tooling.BuildException
import org.gradle.tooling.model.Project

class ToolingApiModelIntegrationTest extends ToolingApiSpecification {
    def "tooling api reports failure to build model"() {
        dist.testFile('build.gradle') << 'broken'

        when:
        withConnection { connection ->
            return connection.getModel(Project.class)
        }

        then:
        BuildException e = thrown()
        e.message == 'Could not fetch model of type \'Project\' from Gradle connection.'
        e.cause.message.contains('A problem occurred evaluating root project')
    }
}
