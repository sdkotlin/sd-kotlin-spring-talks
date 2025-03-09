# SD Kotlin Spring Talks Developer Guidelines

## Project Overview

This is a Spring Boot demonstration project written in Kotlin, showcasing
various Spring features and best practices. The project uses Gradle as its build
system and implements modern Kotlin features including Coroutines.

## Project Structure

```
sd-kotlin-spring-talks/
.
├── .github/                          # GitHub Actions CI workflows
├── .idea/                            # IntelliJ IDEA configuration
├── build-logic/                      # Included build with custom Gradle build logic
├── gradle/                           # Gradle Wrapper configuration and version catalog
├── subprojects/
│   ├── app/                          # Main application module
│   ├── child-context/                # Child context demonstrations
│   │   ├── domain-service/
│   │   └── rest-api/
│   ├── component-scanned-service/    # Component scanning examples
│   ├── custom-resources/             # Custom resource handling
│   ├── kotlin-json/                  # JSON processing
│   │   ├── jackson/
│   │   └── kogera/
│   ├── time-logger/                  # Time logging functionality
│   └── time-service/                 # Time service implementation
└── platforms/                        # Gradle platform definitions
```

## Build & Run

- The project uses Gradle as the build system
- Build and verify the project (includes unit and integration tests):
  `./gradlew build`
- Verify dependency configuration: `./gradlew buildHealth`
- Check for potential dependency updates: `./gradlew dependencyUpdates`
- Run the application: `./gradlew :subprojects:app:run`

## Testing

- JUnit Jupiter is the primary testing framework
- AssertJ is used for fluent assertions
- Mockk is available for mocking
- Prefer plain Kotlin mocks
- Use Gradle [test fixtures](https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures) for shared test doubles and utilities
- Tests are organized into unit (`src/test`) and integration (`src/it`) suites
- JUnit 5's `@DynamicTest` feature is used for test parameterization
- Spring's `@SpringBootTest` is used for integration tests
- Run unit tests: `./gradlew test`
- Run integration tests: `./gradlew integrationTest`
- Run all tests: `./gradlew check`

## Version Control

- The primary development branch is `main`
- Git LFS is used for versioning binaries

## Code Style

- Follow the versioned IntelliJ code style configuration
- Format all changed code before commit

## Dependencies

- Use the Gradle version catalog and platforms for dependency management
- The GitHub Actions CI build will fail if `./gradlew buildHealth` does

## Code Quality

- As the project code sometimes contains partial examples, antipattern
  demonstrations, issue reproducers, and uses preview features, it may contain
  many compiler, static analysis, and runtime warnings. No effort is made to
  suppress these with `@Suppress` annotations, as that would excessively
  clutter the examples.
