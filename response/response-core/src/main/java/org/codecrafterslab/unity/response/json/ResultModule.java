package org.codecrafterslab.unity.response.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/07/23 23:49
 */
public class ResultModule extends Module {

    private final ResultSerializer resultSerializer;

    public ResultModule(ResultSerializer resultSerializer) {
        this.resultSerializer = resultSerializer;
    }

    @Override
    public String getModuleName() {
        return "ResultModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        simpleSerializers.addSerializer(resultSerializer);
        context.addSerializers(simpleSerializers);
    }
}
