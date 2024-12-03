GRADLE_VERSION ?= 8.11.1
chmod:
	@git update-index --chmod=+x gradlew
upgrade-gradle:
	@gradlew wrapper --gradle-version $(GRADLE_VERSION)
projects:
	@gradlew projects -q