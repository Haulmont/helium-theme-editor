package io.jmix.editor.helium.components.themevariablefield;

import io.jmix.ui.xml.layout.loader.AbstractFieldLoader;

public class ThemeVariableFieldLoader extends AbstractFieldLoader<ThemeVariableField> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(ThemeVariableField.NAME);
        loadId(resultComponent, element);
    }
}
