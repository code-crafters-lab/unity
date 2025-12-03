# 1. 定义 classpath 分隔符（默认类 Unix）
CP_SEP := :

# 2. 检查是否为 Windows 系统，切换分隔符为 ;
#    - Windows 的 OS 环境变量值为 Windows_NT
#    - 兼容 Git Bash/MINGW 等 Windows 子系统（uname 输出含 MINGW）
ifneq ($(OS),Windows_NT)
    ifneq (,$(findstring MINGW,$(shell uname)))
        CP_SEP := ;
    endif
else
    CP_SEP := ;
endif

OPENAPI_GENERATOR_CLI_VERSION?=7.17.0
LIB_DIR?=build/libs
ENGINE?=mustache # mustache,handlebars
TEMPLATE_DIR?=src/main/resources/page
INPUT_SPEC?=openapi.json
DEBUG?=
GEN_OPTS=
ifdef DEBUG
GEN_OPTS+=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
endif
OPEN_API_JAR=$(shell find $(LIB_DIR) -name "openapi-0.1.0.jar" )
GEN_OPTS+=-cp "$(LIB_DIR)/openapi-generator-cli-$(OPENAPI_GENERATOR_CLI_VERSION).jar$(CP_SEP)$(OPEN_API_JAR)"
GEN_OPTS+=org.openapitools.codegen.OpenAPIGenerator generate -e $(ENGINE) -g page -i $(INPUT_SPEC) -o outdir
GEN_OPTS+=-c config.yaml
#GEN_OPTS+=-t $(TEMPLATE_DIR)
#GEN_OPTS+=--global-property verbose=true
gen: clean
	java $(GEN_OPTS)
debug:
	@make gen -e DEBUG=y

list:
	@java -cp "$(LIB_DIR)/openapi-generator-cli-$(OPENAPI_GENERATOR_CLI_VERSION).jar$(CP_SEP)$(LIB_DIR)/openapi-0.1.0.jar" \
 		org.openapitools.codegen.OpenAPIGenerator list
help:
	@java -cp "$(LIB_DIR)/openapi-generator-cli-$(OPENAPI_GENERATOR_CLI_VERSION).jar$(CP_SEP)$(LIB_DIR)/openapi-0.1.0.jar" \
    	org.openapitools.codegen.OpenAPIGenerator config-help -g spring
clean:
	@rm -rf outdir
