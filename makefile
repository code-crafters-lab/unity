GRADLE_VERSION ?= 8.11.1
upgrade-gradle:
	@gradlew wrapper --gradle-version $(GRADLE_VERSION)
projects:
	@gradlew projects -q