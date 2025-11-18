GRADLE_VERSION ?= 8.14.3

chmod:
	@git update-index --chmod=+x gradlew

upgrade-gradle: clean-gradle
	@./gradlew wrapper --gradle-version $(GRADLE_VERSION)

projects:
	@./gradlew projects -q

clean-gradle:
	@rm -rf .gradle build
	@rm -rf dict/.gradle
	@rm -rf exception/.gradle
	@rm -rf gradle-plugins/.gradle
	@rm -rf response/.gradle
