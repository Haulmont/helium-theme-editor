package io.jmix.editor.helium.screen.upload;

import io.jmix.editor.helium.tools.ModifiedThemeVariableDetails;
import io.jmix.editor.helium.tools.Template;
import io.jmix.editor.helium.tools.ThemeVariablesManager;
import io.jmix.ui.WindowParam;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.RadioButtonGroup;
import io.jmix.ui.component.TextArea;
import io.jmix.ui.component.data.Options;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@UiController("helium_UploadScreen")
@UiDescriptor("upload-screen.xml")
@DialogMode(forceDialog = true)
public class UploadScreen extends Screen {

    public static final String BASE_THEME_MODE_PARAM = "baseThemeMode";
    public static final String BASE_THEME_MODES_PARAM = "baseThemeModes";

    @WindowParam(name = BASE_THEME_MODE_PARAM)
    protected Template baseThemeMode;

    @WindowParam(name = BASE_THEME_MODES_PARAM)
    protected Options<Template> baseThemeModes;

    @Autowired
    protected RadioButtonGroup<Template> baseThemeModeField;
    @Autowired
    protected TextArea<String> textArea;

    @Autowired
    protected ThemeVariablesManager themeVariablesManager;
    @Autowired
    protected Button applyBtn;

    protected List<ModifiedThemeVariableDetails> uploadedThemeVariables = new ArrayList<>();

    public List<ModifiedThemeVariableDetails> getUploadedThemeVariables() {
        return uploadedThemeVariables;
    }

    public Template getBaseThemeMode() {
        return baseThemeModeField.getValue();
    }

    @Subscribe
    protected void onInit(InitEvent event) {
        initBaseThemeModeField();
    }

    protected void initBaseThemeModeField() {
        baseThemeModeField.setOptions(baseThemeModes);
        baseThemeModeField.setValue(baseThemeMode);
    }

    @Install(to = "baseThemeModeField", subject = "optionCaptionProvider")
    protected String baseThemeModeFieldOptionCaptionProvider(Template template) {
        return template.getName();
    }

    @Subscribe("textArea")
    protected void onTextAreaValueChange(HasValue.ValueChangeEvent<String> event) {
        if (event.isUserOriginated()) {
            if (event.getValue() == null) {
                uploadedThemeVariables.clear();
            } else {
                BufferedReader reader = new BufferedReader(new StringReader(event.getValue()));
                uploadedThemeVariables = themeVariablesManager.parseUploadedThemeVariables(reader);
            }
            updateTextArea();
        }
    }

    protected void updateTextArea() {
        if (!uploadedThemeVariables.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (ModifiedThemeVariableDetails details : uploadedThemeVariables) {
                builder.append(details.getName())
                        .append(": ")
                        .append(details.getValue())
                        .append(";")
                        .append("\n");
            }
            textArea.setValue(builder.toString());
        } else {
            textArea.clear();
        }

        updateApplyBtn();
    }

    protected void updateApplyBtn() {
        applyBtn.setEnabled(!uploadedThemeVariables.isEmpty());
    }

    @Subscribe("applyBtn")
    protected void onApplyBtnClick(Button.ClickEvent event) {
        uploadedThemeVariables = themeVariablesManager.updateThemeVariableDetailsByTemplate(uploadedThemeVariables, baseThemeModeField.getValue());
        close(StandardOutcome.COMMIT);
    }

    @Subscribe("cancelBtn")
    protected void onCancelBtnClick(Button.ClickEvent event) {
        close(StandardOutcome.CLOSE);
    }
}