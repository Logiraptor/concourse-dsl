package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.job
import io.poyarzun.concoursedsl.domain.pipeline
import io.poyarzun.concoursedsl.dsl.generateYML
import org.junit.Test
import kotlin.test.assertEquals

class SemverResourceTest {

    @Test
    fun testGitDriver() {
        val resource = semverResource("version") {
            source(SemverResource.Git, "git@github.com:org/repo.git", "release", "semver.txt") {
                privateKey = "pseudo-random-number"
                username = "sneakers"
                password = "MyV0ic3"
                gitUser = "kingsley"
                depth = 5
                commitMessage = "changing the version to %version%"
                initialVersion = "0.0.1"
            }
        }

        val pipeline = buildTestPipeline(resource)
        val actualYaml = generateYML(pipeline)

        assertEquals(gitExpected, actualYaml)

    }

    @Test
    fun testS3Driver() {
        val resource = semverResource("version") {
            source(SemverResource.S3,
                    bucket = "liza",
                    key = "random",
                    accessKeyId = "more-random",
                    secretAccessKey = "even-more-random") {
                regionName = "the-moon"
                endpoint = "https://aws.amazon.com"
                disableSsl = true
                skipSslVerification = false
                serverSideEncryption = "AES256"
                useV2Signing = true
                initialVersion = "0.0.1"
            }
        }

        val pipeline = buildTestPipeline(resource)
        val actualYaml = generateYML(pipeline)

        assertEquals(s3Expected, actualYaml)
    }

    @Test
    fun testGCSDriver() {

        val resource = semverResource("version") {
            source(SemverResource.Gcs,
                    bucket = "bucket",
                    key = "key",
                    jsonKey = "your-json-here") {
                initialVersion = "0.0.1"
            }
        }

        val pipeline = buildTestPipeline(resource)
        val actualYaml = generateYML(pipeline)

        assertEquals(gcsExpected, actualYaml)
    }

    @Test
    fun testSwiftDriver() {

        val driver = semverResource("version") {
            source(SemverResource.Swift) {
                openstack("my-container", "that-one", "over-there") {
                    identityEndpoint = "http://enpoint"
                    username = "stackuser"
                    userId = "stack.user"
                    password = "stack.password"
                    apiKey = "hiluawriuhrvkljn"
                    domainId = "ijasdfkljhsdf"
                    domainName = "example.com"
                    tenantId = "nvaelinuaevon"
                    tenantName = "David"
                    allowReauth = false
                    tokenId = "iovjlnfaviulerg"
                }
                initialVersion = "0.0.1"
            }
        }

        val pipeline = buildTestPipeline(driver)
        val actualYaml = generateYML(pipeline)

        assertEquals(swiftExpected, actualYaml)
    }


    private fun buildTestPipeline(semver: SemverResource): Pipeline {
        return pipeline {

            resources(semver)

            jobs {
                +job("build") {
                    plan {
                        +get(semver) {
                            params {
                                bump = Bump.minor
                                pre = "rc"
                            }
                        }
                        +put(semver) {
                            params {
                                bump = Bump.major
                                pre = "rc"
                                file = "version.txt"
                            }
                        }
                    }
                }
            }
        }
    }

    val gitExpected =
            """---
jobs:
- name: "build"
  plan:
  - get: "version"
    params:
      bump: "minor"
      pre: "rc"
  - put: "version"
    params:
      file: "version.txt"
      bump: "major"
      pre: "rc"
groups: []
resources:
- name: "version"
  type: "semver"
  source:
    uri: "git@github.com:org/repo.git"
    branch: "release"
    file: "semver.txt"
    initial_version: "0.0.1"
    driver: "git"
    private_key: "pseudo-random-number"
    username: "sneakers"
    password: "MyV0ic3"
    git_user: "kingsley"
    depth: 5
    commit_message: "changing the version to %version%"
resource_types: []

""".trimIndent()

    val swiftExpected = """
---
jobs:
- name: "build"
  plan:
  - get: "version"
    params:
      bump: "minor"
      pre: "rc"
  - put: "version"
    params:
      file: "version.txt"
      bump: "major"
      pre: "rc"
groups: []
resources:
- name: "version"
  type: "semver"
  source:
    initial_version: "0.0.1"
    driver: "swift"
    openstack:
      container: "my-container"
      item_name: "that-one"
      region: "over-there"
      identity_endpoint: "http://enpoint"
      username: "stackuser"
      user_id: "stack.user"
      password: "stack.password"
      api_key: "hiluawriuhrvkljn"
      domain_id: "ijasdfkljhsdf"
      domain_name: "example.com"
      tenant_id: "nvaelinuaevon"
      tenant_name: "David"
      allow_reauth: false
      token_id: "iovjlnfaviulerg"
resource_types: []

    """.trimIndent()

    val gcsExpected = """---
jobs:
- name: "build"
  plan:
  - get: "version"
    params:
      bump: "minor"
      pre: "rc"
  - put: "version"
    params:
      file: "version.txt"
      bump: "major"
      pre: "rc"
groups: []
resources:
- name: "version"
  type: "semver"
  source:
    bucket: "bucket"
    key: "key"
    json_key: "your-json-here"
    initial_version: "0.0.1"
    driver: "gcs"
resource_types: []
"""

    val s3Expected = """
---
jobs:
- name: "build"
  plan:
  - get: "version"
    params:
      bump: "minor"
      pre: "rc"
  - put: "version"
    params:
      file: "version.txt"
      bump: "major"
      pre: "rc"
groups: []
resources:
- name: "version"
  type: "semver"
  source:
    bucket: "liza"
    key: "random"
    access_key_id: "more-random"
    secret_access_key: "even-more-random"
    initial_version: "0.0.1"
    driver: "s3"
    region_name: "the-moon"
    endpoint: "https://aws.amazon.com"
    disable_ssl: true
    skip_ssl_verification: false
    server_side_encryption: "AES256"
    use_v2_signing: true
resource_types: []

    """.trimIndent()

}