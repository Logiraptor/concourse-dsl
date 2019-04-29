package io.poyarzun.concoursedsl.resources

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.dsl.*
import org.junit.Test
import kotlin.test.assertEquals

class SemverResourceTest {

    @Test
    fun testGitDriver() {

        val params = GitSourceParams(
                uri = "git@github.com:org/repo.git",
                branch = "release",
                file = "semver.txt",
                privateKey = "pseudo-random-number",
                userName = "sneakers",
                password = "MyV0ic3",
                gitUser = "kingsley",
                depth = 5,
                commitMessage = "changing the version to %version%"
        )

        val pipeline = buildTestPipeline(params)
        val actualYaml = generateYML(pipeline)

        assertEquals(git_expected, actualYaml)

    }

    @Test
    fun testS3Driver() {

        val params = S3DSourceParams(
                bucket = "liza",
                key = "random",
                accessKey = "more-random",
                secretAccessKey = "even-more-random",
                regionName = "the-moon",
                endpoint = "https://aws.amazon.com",
                disableSsl = true,
                skipSslVerification = false,
                serverSideEncryption = "AES256",
                useV2Signing = true
        )

        val pipeline = buildTestPipeline(params)
        val actualYaml = generateYML(pipeline)

        assertEquals(s3_expected, actualYaml)

    }

    @Test
    fun testGCSDriver() {

        val params = GcsSourceParams(
                bucket = "bucket",
                key = "key",
                jsonKey = "your-json-here"
        )

        val pipeline = buildTestPipeline(params)
        val actualYaml = generateYML(pipeline)

        assertEquals(gcs_expected, actualYaml)
    }

    @Test
    fun testSwiftDriver() {

        val driver = SwiftSourceParams(
                OpenStack(
                        container = "my-container",
                        itemName = "that-one",
                        region = "over-there",
                        identityEndpoint = "http://enpoint",
                        username = "stackuser",
                        userId = "stack.user",
                        password = "stack.password",
                        apiKey = "hiluawriuhrvkljn",
                        domainId = "ijasdfkljhsdf",
                        domainName = "example.com",
                        tenantId = "nvaelinuaevon",
                        tenantName = "David",
                        allowReauth = false,
                        tokenId = "iovjlnfaviulerg"
                )
        )

        val pipeline = buildTestPipeline(driver)
        val actualYaml = generateYML(pipeline)

        assertEquals(swift_expected, actualYaml)
    }

    private fun buildTestPipeline(params: Semver.SourceParams): Pipeline {
        return pipeline {

            val semver = semverResource("version", params) {
                source {
                    initialVersion = "0.0.1"
                }
            }

            job("build") {
                plan {
                    get(semver) {
                        params {
                            bump = Bump.minor
                            pre = "rc"
                        }
                    }
                    put(semver) {
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

    val git_expected =
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
    get_params: {}
groups: []
resources:
- name: "version"
  type: "semver"
  source:
    uri: "git@github.com:org/repo.git"
    branch: "release"
    file: "semver.txt"
    private_key: "pseudo-random-number"
    user_name: "sneakers"
    password: "MyV0ic3"
    git_user: "kingsley"
    depth: 5
    commit_message: "changing the version to %version%"
    driver: "git"
    initial_version: "0.0.1"
resource_types: []

""".trimIndent()

    val swift_expected = """
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
    get_params: {}
groups: []
resources:
- name: "version"
  type: "semver"
  source:
    open_stack:
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
    driver: "swift"
    initial_version: "0.0.1"
resource_types: []

    """.trimIndent()

    val gcs_expected = """---
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
    get_params: {}
groups: []
resources:
- name: "version"
  type: "semver"
  source:
    bucket: "bucket"
    key: "key"
    json_key: "your-json-here"
    driver: "gcs"
    initial_version: "0.0.1"
resource_types: []
"""

    val s3_expected = """
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
    get_params: {}
groups: []
resources:
- name: "version"
  type: "semver"
  source:
    bucket: "liza"
    key: "random"
    access_key: "more-random"
    secret_access_key: "even-more-random"
    region_name: "the-moon"
    endpoint: "https://aws.amazon.com"
    disable_ssl: true
    skip_ssl_verification: false
    server_side_encryption: "AES256"
    use_v2_signing: true
    driver: "s3"
    initial_version: "0.0.1"
resource_types: []

    """.trimIndent()

}