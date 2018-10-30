
# Chores

## Add Dokka

## Setup CI infrastructure

## Improve developer experience

## 100% Test Coverage

# Features

## Type safe params / sources / etc

```kotlin

task("Foo") {
    params = mapOf("variable" to "value")
}

```

Could become something like

```kotlin

data class Custom(variable: String)

task("Foo") {
    params<Custom> {
        variable = "value"
    }
}

```

## Task Config object

Rather than `Map<String, Any?>`, make a task config thing

## Built in typed resources for cf, tracker, etc.

```kotlin

resource<CloudFoundry>("Prod") {
    space = "prod"
    api = "api.example.pcf.com"
}

```

## Require *either* file or config for a task definition

```kotlin

task("name", "file") {

}

// -  or -

task("name") {
    config {
        image = "something-from-docker"
    }
}

```

## Better Group Syntax

Not sure what to do here, but jobs.add("name") feels inconsistent with the rest of the api.