GRADLE_VERSION ?= 8.14.3
chmod:
	@git update-index --chmod=+x gradlew
upgrade-gradle:
	@./gradlew wrapper --gradle-version $(GRADLE_VERSION)
projects:
	@./gradlew projects -q
