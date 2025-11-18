LIB_DIR?=build/libs
ENGINE?=mustache # mustache,handlebars
TEMPLATE_DIR?=src/main/resources/page
#INPUT_SPEC?=petstore-v3.0.yaml
INPUT_SPEC?=openapi.json
DEBUG?=
GEN_OPTS=
ifdef DEBUG
GEN_OPTS+=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
endif
OPEN_API_JAR=$(shell find $(LIB_DIR) -name "codegen-openapi-*.jar" )
GEN_OPTS+=-cp "$(LIB_DIR)/openapi-generator-cli-7.2.0.jar;$(OPEN_API_JAR)"
GEN_OPTS+=org.openapitools.codegen.OpenAPIGenerator generate -e $(ENGINE) -g page -i $(INPUT_SPEC) -o outdir
GEN_OPTS+=-c config.yaml -t $(TEMPLATE_DIR)
GEN_OPTS+=--global-property verbose=true
#-e handlebars
gen: clean
	@java $(GEN_OPTS)
debug:
	make gen -E DEBUG=y

list:
	@java -cp "$(LIB_DIR)/openapi-generator-cli-7.2.0.jar;$(LIB_DIR)/codegen-openapi.jar" \
	org.openapitools.codegen.OpenAPIGenerator list
help:
	@java -cp "$(LIB_DIR)/openapi-generator-cli-7.2.0.jar;$(LIB_DIR)/codegen-openapi.jar" \
    	org.openapitools.codegen.OpenAPIGenerator config-help -g page
clean:
	@rm -rf outdir