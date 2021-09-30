package io.jmix.editor.helium;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Set;

@ConfigurationProperties(prefix = "helium.editor")
@ConstructorBinding
public class HeliumEditorProperties {

    protected Set<String> excludedThemeVariableModules;

    protected String themeVariablesFilePath;

    public HeliumEditorProperties(@DefaultValue("TokenList") Set<String> excludedThemeVariableModules,
                                  @DefaultValue("helium/helium.scss") String themeVariablesFilePath) {
        this.excludedThemeVariableModules = excludedThemeVariableModules;
        this.themeVariablesFilePath = themeVariablesFilePath;
    }

    public Set<String> getExcludedThemeVariableModules() {
        return excludedThemeVariableModules;
    }

    public String getThemeVariablesFilePath() {
        return themeVariablesFilePath;
    }
}
