workflow "Test" {
  on = "push"
  resolves = ["Run Unit Tests"]
}

action "Run Unit Tests" {
  uses = "docker://maven"
  runs = "./gradlew test"
}
