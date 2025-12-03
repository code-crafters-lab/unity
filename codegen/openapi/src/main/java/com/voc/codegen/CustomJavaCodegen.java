package com.voc.codegen;

import com.google.auto.service.AutoService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.AbstractJavaCodegen;
import org.openapitools.codegen.languages.JavaDubboServerCodegen;
import org.openapitools.codegen.languages.SpringCodegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AutoService(CodegenConfig.class)
public class CustomJavaCodegen extends JavaDubboServerCodegen {

    protected String basePackage = "net.jqsoft.cds";

    public CustomJavaCodegen() {
        super();
        invokerPackage = "net.jqsoft.cds.bid";
        embeddedTemplateDir = "controller";

        this.apiPackage = this.invokerPackage;
        this.modelPackage = this.invokerPackage + ".model";

        this.apiTemplateFiles.clear();
        this.apiTemplateFiles.put("api.mustache", ".java");
        this.apiTemplateFiles.put("apiImpl.mustache", ".java");
        this.apiTemplateFiles.put("apiController.mustache", ".java");

        this.apiTestTemplateFiles.clear();
        this.modelTemplateFiles.clear();
        this.modelTemplateFiles.put("model.mustache", ".java");
        this.modelDocTemplateFiles.clear();
        this.apiDocTemplateFiles.clear();

        cliOptions.add(CliOption.newBoolean("useLombok", "是否使用 lombok", true));
        cliOptions.add(CliOption.newBoolean("useSlf4j", "是否使用 slf4j", false));

//        supportingFiles.add(new SupportingFile("controller.mustache", "", "AController.java"));

    }

    /**
     * Configures the type of generator.
     *
     * @return the CodegenType for this generator
     * @see org.openapitools.codegen.CodegenType
     */
    @Override
    public CodegenType getTag() {
        return CodegenType.OTHER;
    }

    /**
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -g flag.
     *
     * @return the friendly name for the generator
     */
    @Override
    public String getName() {
        return "controller";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     *
     * @return A string value for the help message
     */
    @Override
    public String getHelp() {
        return "gen controller";
    }

    @Override
    public void processOpts() {
        super.processOpts();
    }

    @Override
    public void preprocessOpenAPI(OpenAPI openAPI) {
        super.preprocessOpenAPI(openAPI);
        Paths paths = openAPI.getPaths();
        for (PathItem pathItem : paths.values()) {
            for (Operation operation : pathItem.readOperations()) {
                ApiResponses responses = operation.getResponses();
                for (Map.Entry<String, ApiResponse> entry : responses.entrySet()) {
                    String key = entry.getKey();
                    if (key.contains("undefined")) {
                        responses.remove(key);
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        objs = super.postProcessSupportingFileData(objs);
        return objs;
    }

    @Override
    public String apiFilename(String templateName, String tag) {
        String suffix = apiTemplateFiles().get(templateName);
        if (suffix == null) {
            return null;
        }

        String folder = getFileFolderForTemplate(templateName);
        String filename;
        String apiName = toApiName(tag);

        if ("api.mustache".equals(templateName)) {
            filename = apiName;
        } else if ("apiImpl.mustache".equals(templateName)) {
            filename = apiName + "Impl";
        } else if ("apiController.mustache".equals(templateName)) {
            filename = apiName + "Controller";
        } else {
            filename = toApiFilename(tag);
        }

        return folder + File.separator + filename + suffix;
    }

    public String getFileFolderForTemplate(String templateName) {
        String baseFolder = String.join(File.separator, outputFolder, "src", "main", "java", apiPackage().replace('.', File.separatorChar));
        if ("api.mustache".equals(templateName)) {
            return String.join(File.separator, baseFolder, "service");
        } else if ("apiImpl.mustache".equals(templateName)) {
            return String.join(File.separator, baseFolder, "service", "impl");
        } else if ("apiController.mustache".equals(templateName)) {
            return String.join(File.separator, baseFolder, "controller");
        }
        return baseFolder;
    }

    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
//        if (supportLibraryUseTags() && !useTags) {
//        String basePath = resourcePath;
//        if (basePath.startsWith("/")) {
//            basePath = basePath.substring(1);
//        }
//        final int pos = basePath.indexOf("/");
//        if (pos > 0) {
//            basePath = basePath.substring(0, pos);
//        }
//
//        if (basePath.isEmpty()) {
//            basePath = "default";
//        } else {
//            co.subresourceOperation = !co.path.isEmpty();
//        }
//        final List<CodegenOperation> opList = operations.computeIfAbsent(basePath, k -> new ArrayList<>());
//        opList.add(co);
//        co.baseName = basePath;
//        return;
//        }
        super.addOperationToGroup(tag, resourcePath, operation, co, operations);

    }

}
