GRADLE_VERSION ?= 9.4.1

chmod:
	@git update-index --chmod=+x gradlew

upgrade-gradle: clean-gradle
	@./gradlew wrapper --gradle-version $(GRADLE_VERSION)

gradle-all:
	@./gradlew wrapper --distribution-type all

gradle-bin:
	@./gradlew wrapper --distribution-type bin

projects:
	@./gradlew projects -q

clean-gradle:
	@rm -rf .gradle build
	@rm -rf dict/.gradle
	@rm -rf exception/.gradle
	@rm -rf gradle-plugins/.gradle
	@rm -rf response/.gradle
