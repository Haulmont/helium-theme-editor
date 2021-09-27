package io.jmix.editor.helium.tools;

public class ModifiedThemeVariableDetails {

    protected String name;
    protected String module;
    protected String value;
    protected String parentVariableName;

    public ModifiedThemeVariableDetails() {
    }

    public ModifiedThemeVariableDetails(String name, String module, String value) {
        this.name = name;
        this.module = module;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentVariableName() {
        return parentVariableName;
    }

    public void setParentVariableName(String parentVariableName) {
        this.parentVariableName = parentVariableName;
    }
}
