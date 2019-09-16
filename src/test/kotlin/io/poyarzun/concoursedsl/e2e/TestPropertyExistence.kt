package io.poyarzun.concoursedsl.e2e

import io.poyarzun.concoursedsl.domain.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestPropertyExistence {

    data class DocProperty(
            val filePath: String,
            val name: String,
            val type: String
    ) {
        constructor(
                filePath: String,
                name: String,
                type: String,
                // This allows the compiler to check existence of our known properties below
                ref: Any?
        ) : this(filePath, name, type)

        private fun characterLiteralWithoutSingleQuotes(c: Char): String = when (c) {
            '"' -> "\\\""
            '\\' -> "\\\\"
            else -> c.toString()
        }

        private fun escapeString(value: String): String {
            val result = StringBuilder(value.length + 32)
            result.append('"')
            value.forEach {
                result.append(characterLiteralWithoutSingleQuotes(it))
            }
            result.append('"')
            return result.toString()
        }

        override fun toString(): String {
            val filePath = escapeString(filePath)
            val name = escapeString(name)
            val type = escapeString(type)
            return "DocProperty($filePath, $name, $type)"
        }
    }

    private val knownProperties = setOf(
            DocProperty("/operation/creds/aws-secretsmanager.lit", "aws-secretsmanager-access-key", "string"),
            DocProperty("/operation/creds/aws-secretsmanager.lit", "aws-secretsmanager-secret-key", "string"),
            DocProperty("/operation/creds/aws-secretsmanager.lit", "aws-secretsmanager-session-token", "string"),
            DocProperty("/operation/creds/aws-secretsmanager.lit", "aws-secretsmanager-region", "string"),
            DocProperty("/operation/creds/aws-secretsmanager.lit", "aws-secretsmanager-pipeline-secret-template", "string"),
            DocProperty("/operation/creds/aws-secretsmanager.lit", "aws-secretsmanager-team-secret-template", "string"),
            DocProperty("/operation/creds/aws-ssm.lit", "aws-ssm-access-key", "string"),
            DocProperty("/operation/creds/aws-ssm.lit", "aws-ssm-secret-key", "string"),
            DocProperty("/operation/creds/aws-ssm.lit", "aws-ssm-session-token", "string"),
            DocProperty("/operation/creds/aws-ssm.lit", "aws-ssm-region", "string"),
            DocProperty("/operation/creds/aws-ssm.lit", "aws-ssm-pipeline-secret-template", "string"),
            DocProperty("/operation/creds/aws-ssm.lit", "aws-ssm-team-secret-template", "string"),

            DocProperty("/tasks.lit", "platform", "string", Task::platform),
            DocProperty("/tasks.lit", "image_resource", "resource", Task::imageResource),
            DocProperty("/tasks.lit", "type", "string", Task.Resource::type),
            DocProperty("/tasks.lit", "source", "object", Task.Resource::source),
            DocProperty("/tasks.lit", "params", "object", Task.Resource::params),
            DocProperty("/tasks.lit", "version", "object", Task.Resource::version),
            DocProperty("/tasks.lit", "rootfs_uri", "string", Task::rootfsUri),
            DocProperty("/tasks.lit", "container_limits", "container-limits", Task::containerLimits),
            DocProperty("/tasks.lit", "cpu", "integer", Task.ContainerLimits::cpu),
            DocProperty("/tasks.lit", "memory", "integer", Task.ContainerLimits::memory),
            DocProperty("/tasks.lit", "inputs", "[input]", Task::inputs),
            DocProperty("/tasks.lit", "name", "string", Task.Input::name),
            DocProperty("/tasks.lit", "path", "string", Task.Input::path),
            DocProperty("/tasks.lit", "optional", "bool", Task.Input::optional),
            DocProperty("/tasks.lit", "outputs", "[output]", Task::outputs),
            DocProperty("/tasks.lit", "caches", "[cache]", Task::caches),
            DocProperty("/tasks.lit", "run", "run-config", Task::run),
            DocProperty("/tasks.lit", "args", "[string]", Task.RunConfig::args),
            DocProperty("/tasks.lit", "dir", "string", Task.RunConfig::dir),
            DocProperty("/tasks.lit", "user", "string", Task.RunConfig::user),
            DocProperty("/tasks.lit", "params", "\\{string: string\\}", Task::params),

            DocProperty("/pipelines/groups.lit", "name", "string", Group::name),
            DocProperty("/pipelines/groups.lit", "jobs", "[string]", Group::jobs),
            DocProperty("/pipelines/groups.lit", "resources", "[string]", Group::resources),

            DocProperty("/resource-types.lit", "name", "string", ResourceType::name),
            DocProperty("/resource-types.lit", "type", "string", ResourceType::type),
            DocProperty("/resource-types.lit", "source", "object", ResourceType::source),
            DocProperty("/resource-types.lit", "privileged", "boolean", ResourceType::privileged),
            DocProperty("/resource-types.lit", "params", "object", ResourceType::params),
            DocProperty("/resource-types.lit", "check_every", "string", ResourceType::checkEvery),
            DocProperty("/resource-types.lit", "tags", "[string]", ResourceType::tags),
            DocProperty("/resource-types.lit", "unique_version_history", "bool", ResourceType::uniqueVersionHistory),

            DocProperty("/pipelines.lit", "jobs", "[job]", Pipeline::jobs),
            DocProperty("/pipelines.lit", "resources", "[resource]", Pipeline::resources),
            DocProperty("/pipelines.lit", "resource_types", "[resource_type]", Pipeline::resourceTypes),
            DocProperty("/pipelines.lit", "groups", "[group]", Pipeline::groups),

            DocProperty("/jobs/steps/put.lit", "put", "string", Step.PutStep<Any, Any>::put),
            DocProperty("/jobs/steps/put.lit", "resource", "string", Step.PutStep<Any, Any>::resource),
            DocProperty("/jobs/steps/put.lit", "inputs", "[string]", Step.PutStep<Any, Any>::inputs),
            DocProperty("/jobs/steps/put.lit", "params", "object", Step.PutStep<Any, Any>::params),
            DocProperty("/jobs/steps/put.lit", "get_params", "object", Step.PutStep<Any, Any>::getParams),

            DocProperty("/jobs/steps/do.lit", "do", "[step]", Step.DoStep::`do`),

            DocProperty("/jobs/steps/task.lit", "task", "string", Step.TaskStep::task),
            DocProperty("/jobs/steps/task.lit", "config", "object", Step.TaskStep::config),
            DocProperty("/jobs/steps/task.lit", "file", "string", Step.TaskStep::file),
            DocProperty("/jobs/steps/task.lit", "privileged", "boolean", Step.TaskStep::privileged),
            DocProperty("/jobs/steps/task.lit", "vars", "object", Step.TaskStep::vars),
            DocProperty("/jobs/steps/task.lit", "params", "object", Step.TaskStep::params),
            DocProperty("/jobs/steps/task.lit", "image", "string", Step.TaskStep::image),
            DocProperty("/jobs/steps/task.lit", "input_mapping", "object", Step.TaskStep::inputMapping),
            DocProperty("/jobs/steps/task.lit", "output_mapping", "object", Step.TaskStep::outputMapping),

            DocProperty("/jobs/steps/in_parallel.lit", "in_parallel", "([step] | {config})", Step.InParallelStep::inParallel),
            DocProperty("/jobs/steps/in_parallel.lit", "steps", "[step]", Step.InParallelStep.InParallelConfig::steps),
            DocProperty("/jobs/steps/in_parallel.lit", "limit", "integer", Step.InParallelStep.InParallelConfig::limit),
            DocProperty("/jobs/steps/in_parallel.lit", "fail_fast", "bool", Step.InParallelStep.InParallelConfig::failFast),

            DocProperty("/jobs/steps/timeout.lit", "timeout", "duration", Step::timeout),

            DocProperty("/jobs/steps/get.lit", "get", "string", Step.GetStep<Any>::get),
            DocProperty("/jobs/steps/get.lit", "resource", "string", Step.GetStep<Any>::resource),
            DocProperty("/jobs/steps/get.lit", "version", "(\"latest\" | \"every\" | \\{version\\})", Step.GetStep<Any>::version),
            DocProperty("/jobs/steps/get.lit", "passed", "[string]", Step.GetStep<Any>::passed),
            DocProperty("/jobs/steps/get.lit", "params", "object", Step.GetStep<Any>::params),
            DocProperty("/jobs/steps/get.lit", "trigger", "boolean", Step.GetStep<Any>::trigger),

            DocProperty("/jobs/steps/try.lit", "try", "step", Step.TryStep::`try`),

            DocProperty("/jobs/steps/ensure.lit", "ensure", "step", Step::ensure),
            DocProperty("/jobs/steps/on_success.lit", "on_success", "step", Step::onSuccess),
            DocProperty("/jobs/steps/aggregate.lit", "aggregate", "[step]", Step.AggregateStep::aggregate),
            DocProperty("/jobs/steps/on_error.lit", "on_error", "step", Step::onError),
            DocProperty("/jobs/steps/tags.lit", "tags", "[string]", Step::tags),
            DocProperty("/jobs/steps/attempts.lit", "attempts", "integer", Step::attempts),
            DocProperty("/jobs/steps/on_failure.lit", "on_failure", "step", Step::onFailure),
            DocProperty("/jobs/steps/on_abort.lit", "on_abort", "step", Step::onAbort),

            DocProperty("/jobs.lit", "name", "string", Job::name),
            DocProperty("/jobs.lit", "old_name", "string", Job::oldName),
            DocProperty("/jobs.lit", "plan", "[step]", Job::plan),
            DocProperty("/jobs.lit", "serial", "boolean", Job::serial),
            DocProperty("/jobs.lit", "build_log_retention", "retention_config", Job::buildLogRetention),
            DocProperty("/jobs.lit", "days", "number", Job.RetentionConfig::days),
            DocProperty("/jobs.lit", "builds", "number", Job.RetentionConfig::builds),
            DocProperty("/jobs.lit", "build_logs_to_retain", "number", Job::buildLogsToRetain),
            DocProperty("/jobs.lit", "serial_groups", "[string]", Job::serialGroups),
            DocProperty("/jobs.lit", "max_in_flight", "integer", Job::maxInFlight),
            DocProperty("/jobs.lit", "public", "boolean", Job::public),
            DocProperty("/jobs.lit", "disable_manual_trigger", "boolean", Job::disableManualTrigger),
            DocProperty("/jobs.lit", "interruptible", "boolean", Job::interruptible),
            DocProperty("/jobs.lit", "on_success", "step", Job::onSuccess),
            DocProperty("/jobs.lit", "on_failure", "step", Job::onFailure),
            DocProperty("/jobs.lit", "on_abort", "step", Job::onAbort),
            DocProperty("/jobs.lit", "ensure", "step", Job::ensure),

            DocProperty("/resources.lit", "name", "string", Resource<Any>::name),
            DocProperty("/resources.lit", "type", "string", Resource<Any>::type),
            DocProperty("/resources.lit", "icon", "string", Resource<Any>::icon),
            DocProperty("/resources.lit", "source", "object", Resource<Any>::source),
            DocProperty("/resources.lit", "version", "object", Resource<Any>::version),
            DocProperty("/resources.lit", "check_every", "string", Resource<Any>::checkEvery),
            DocProperty("/resources.lit", "tags", "[string]", Resource<Any>::tags),
            DocProperty("/resources.lit", "public", "boolean", Resource<Any>::public),
            DocProperty("/resources.lit", "webhook_token", "string", Resource<Any>::webhookToken)
    )

    @Test
    fun allPropertiesInDocsExist() {

        ProcessBuilder()
                .command("git", "clone", "https://github.com/concourse/docs.git", "concourse-docs")
                .start().waitFor()

        val docRepoPath = "concourse-docs/lit/docs"
        val grepProcess = ProcessBuilder()
                .command("grep", "-R", "define-attribute", docRepoPath)
                .start()
        val lineRegex = Regex("^(?<filePath>[^:]+):[^{]+\\{(?<name>[^:]+):\\s*(?<type>.*)\\}\\{\$")
        val documentedProperties = grepProcess.inputStream.reader().readLines().map {
            val result = lineRegex.matchEntire(it) ?: throw IllegalStateException("Unexpected grep result: $it")
            val (filePath, name, type) = result.destructured
            DocProperty(filePath.removePrefix(docRepoPath), name, type)
        }.toSet()

        val missingProperties = documentedProperties.minus(knownProperties)
        val extraProperties = knownProperties.minus(documentedProperties)

        if (missingProperties.isNotEmpty()) {
            println("MISSING")
            println(missingProperties.joinToString(",\n"))
        }

        if (extraProperties.isNotEmpty()) {
            println("EXTRA")
            println(extraProperties.joinToString(",\n"))
        }

        assertEquals(missingProperties.size, 0)
        assertEquals(extraProperties.size, 0)
    }
}