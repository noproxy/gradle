/*
 * Copyright 2019 the original author or authors.
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

package org.gradle.plugins.signing

import org.gradle.api.publish.PublicationArtifact
import org.gradle.api.publish.ivy.IvyArtifact
import org.gradle.api.publish.maven.MavenArtifact
import org.gradle.testing.internal.util.Specification

class SignatureTest extends Specification {
    def "signature become optional if source MavenArtifact is optional"() {
        given:
        def artifact = Mock(MavenArtifact) {
            _ * getOptional() >> artifactOptional
            0 * _
        }
        def spec = Mock(SignatureSpec) {
            _ * isRequired() >> specRequired
        }

        expect:
        result == createSignature(artifact, spec).optional

        where:
        artifactOptional | specRequired | result
        true             | true              | true
        true             | false             | true
        false            | true              | false
        false            | false             | true
    }

    def "signature become optional if source IvyArtifact is optional"() {
        given:
        def artifact = Mock(IvyArtifact) {
            _ * getOptional() >> artifactOptional
            0 * _
        }
        def spec = Mock(SignatureSpec) {
            _ * isRequired() >> specRequired
        }

        expect:
        result == createSignature(artifact, spec).optional

        where:
        artifactOptional | specRequired | result
        true             | true              | true
        true             | false             | true
        false            | true              | false
        false            | false             | true
    }

    private static Signature createSignature(PublicationArtifact artifact, SignatureSpec spec) {
        return new Signature(artifact, { -> new File("test") }, null, null, spec)

    }
}
