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
package org.gradle.tooling.internal.consumer

import org.gradle.tooling.UnknownModelException
import org.gradle.tooling.internal.consumer.async.AsyncConnection
import org.gradle.tooling.internal.adapter.ProtocolToModelAdapter
import org.gradle.tooling.model.GradleProject
import spock.lang.Specification

class DefaultProjectConnectionTest extends Specification {
    final AsyncConnection protocolConnection = Mock()
    final ProtocolToModelAdapter adapter = Mock()
    final ConnectionParameters parameters = Mock()
    final DefaultProjectConnection connection = new DefaultProjectConnection(protocolConnection, adapter, parameters)

    def canCreateAModelBuilder() {
        expect:
        connection.model(GradleProject.class) instanceof DefaultModelBuilder
    }

    def canCreateABuildLauncher() {
        expect:
        connection.newBuild() instanceof DefaultBuildLauncher
    }
    
    def modelFailsForUnknownModelType() {
        when:
        connection.model(TestBuild.class)

        then:
        thrown(UnknownModelException)
    }

    def closeStopsBackingConnection() {
        when:
        connection.close()

        then:
        1 * protocolConnection.stop()
    }
}

interface TestBuild extends GradleProject {
    
}
