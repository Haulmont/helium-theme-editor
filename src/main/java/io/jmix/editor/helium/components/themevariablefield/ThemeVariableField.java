package io.jmix.editor.helium.components.themevariablefield;

import com.vaadin.ui.JavaScript;
import io.jmix.core.common.event.Subscription;
import io.jmix.editor.helium.tools.*;
import io.jmix.ui.component.*;
import io.jmix.ui.component.data.ValueSource;
import io.jmix.ui.component.validation.Validator;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Consumer;

@CompositeDescriptor("/io/jmix/editor/helium/components/themevariablefield/theme-variable-field.xml")
public class ThemeVariableField extends CompositeComponent<Form>
        implements Field<ThemeVariable>,
        CompositeWithIcon,
        HasInputPrompt {

    public static final String NAME = "themeVariableField";

    public static final String RGB_POSTFIX = "_rgb";

    protected static final String SET_THEME_VARIABLE_VOID = "setThemeVariable('%s', '%s')";
    protected static final String REMOVE_THEME_VARIABLE_VOID = "removeThemeVariable('%s')";

    // Inner components
    protected Label<String> captionField;
    protected TextField<String> valueField;
    protected ColorPicker colorValueField;
    protected JavaScriptComponent jsComponent;
    protected Button resetBtn;

    protected JavaScript javaScript;

    protected ThemeVariable themeVariable;
    protected Template currentTemplate;
    protected String parentValue;

    public ThemeVariableField() {
        addCreateListener(this::onCreate);
    }

    private void onCreate(CreateEvent createEvent) {
        captionField = getInnerComponent("captionField");
        valueField = getInnerComponent("valueField");
        colorValueField = getInnerComponent("colorValueField");
        jsComponent = getInnerComponent("jsComponent");
        resetBtn = getInnerComponent("resetBtn");

        initColorValueField();
        initValueField();
        refreshJavaScriptComponent();
        initResetBtn();
    }

    @Nullable
    @Override
    public ThemeVariable getValue() {
        return themeVariable;
    }

    @Override
    public void setValue(@Nullable ThemeVariable themeVariable) {
        this.themeVariable = themeVariable;
        setColorValueByTemplate(themeVariable.getDefaultColorTemplate());
    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<ThemeVariable>> listener) {
        // do nothing
        return null;
    }

    @Override
    public void addValidator(Validator<? super ThemeVariable> validator) {
        // do nothing
    }

    @Override
    public void removeValidator(Validator<ThemeVariable> validator) {
        // do nothing
    }

    @Override
    public Collection<Validator<ThemeVariable>> getValidators() {
        // do nothing
        return Collections.emptyList();
    }

    @Override
    public boolean isValid() {
        return colorValueField.isValid()
                && valueField.isValid();
    }

    @Override
    public void validate() throws ValidationException {
        valueField.validate();
        colorValueField.validate();
    }

    @Override
    public String getInputPrompt() {
        return valueField.getInputPrompt();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        valueField.setInputPrompt(inputPrompt);
    }

    @Override
    public boolean isRequired() {
        return valueField.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        valueField.setRequired(required);
    }

    @Override
    public String getRequiredMessage() {
        return valueField.getRequiredMessage();
    }

    @Override
    public void setRequiredMessage(String msg) {
        valueField.setRequiredMessage(msg);
    }

    @Override
    public boolean isEditable() {
        return valueField.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        valueField.setEditable(editable);
        colorValueField.setEditable(editable);
    }

    @Nullable
    @Override
    public String getCaption() {
        return captionField.getValue();
    }

    @Override
    public void setCaption(@Nullable String caption) {
        captionField.setValue(caption);
    }

    @Nullable
    @Override
    public String getDescription() {
        return valueField.getDescription();
    }

    @Override
    public void setDescription(@Nullable String description) {
        valueField.setDescription(description);
        colorValueField.setDescription(description);
    }

    @Override
    public String getContextHelpText() {
        return valueField.getContextHelpText();
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        valueField.setContextHelpText(contextHelpText);
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return valueField.isContextHelpTextHtmlEnabled();
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        valueField.setContextHelpTextHtmlEnabled(enabled);
    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return valueField.getContextHelpIconClickHandler();
    }

    @Override
    public void setContextHelpIconClickHandler(@Nullable Consumer<ContextHelpIconClickEvent> handler) {
        valueField.setContextHelpIconClickHandler(handler);
    }

    @Override
    public boolean isCaptionAsHtml() {
        return captionField.isHtmlEnabled();
    }

    @Override
    public void setCaptionAsHtml(boolean captionAsHtml) {
        captionField.setHtmlEnabled(captionAsHtml);
    }

    @Override
    public boolean isDescriptionAsHtml() {
        return valueField.isDescriptionAsHtml();
    }

    @Override
    public void setDescriptionAsHtml(boolean descriptionAsHtml) {
        valueField.setDescriptionAsHtml(descriptionAsHtml);
    }

    @Override
    public boolean isHtmlSanitizerEnabled() {
        return valueField.isHtmlSanitizerEnabled();
    }

    @Override
    public void setHtmlSanitizerEnabled(boolean htmlSanitizerEnabled) {
        valueField.setHtmlSanitizerEnabled(htmlSanitizerEnabled);
        captionField.setHtmlSanitizerEnabled(htmlSanitizerEnabled);
        colorValueField.setHtmlSanitizerEnabled(htmlSanitizerEnabled);
    }

    @Override
    public void setValueSource(@Nullable ValueSource<ThemeVariable> valueSource) {
        // do nothing
    }

    @Nullable
    @Override
    public ValueSource<ThemeVariable> getValueSource() {
        // do nothing
        return null;
    }

    public void setColorValueByTemplate(Template template) {
        if (themeVariable == null) {
            return;
        }

        currentTemplate = template;

        ThemeVariableDetails details = getThemeVariableDetailsByTemplate(template);
        if (details == null) {
            return;
        }

        if (details.getParentThemeVariable() != null
                && !details.isCommentDependence()) {
            details.setValue(details.getParentThemeVariable().getThemeVariableDetails(template).getValue());
        }

        if (!Objects.equals(details.getValue(), ThemeVariableUtils.getColorString(colorValueField.getValue()))) {
            parentValue = null;
            reset(details);
        }
    }

    public void setColorValueByParent(String parentColorValue) {
        ThemeVariableDetails details = getThemeVariableDetailsByTemplate(currentTemplate);

        if (parentColorValue == null) {
            parentColorValue = details.getValue();
            parentValue = null;

            if (currentTemplate != null
                    && currentTemplate.getParent() != null
                    && themeVariable.hasColorTemplate(currentTemplate)) {
                setThemeVariable(parentColorValue, false);
            } else {
                removeThemeVariable();
            }
        } else {
            String colorModifier = details.getColorModifier();
            if ((colorModifier == null && details.isCommentDependence()) ||
                    ThemeVariablesManager.TRANSPARENT_COLOR_VALUE.equals(parentColorValue)) {
                setThemeVariable(parentColorValue, true);
            } else if (colorModifier != null) {
                String colorModifierValue = details.getColorModifierValue();
                if (colorModifierValue != null) {
                    int percent = Integer.parseInt(colorModifierValue.substring(0, colorModifierValue.length() - 1));
                    parentColorValue = colorModifier.equals("d")
                            ? ThemeVariableUtils.darken(parentColorValue, percent)
                            : ThemeVariableUtils.lighten(parentColorValue, percent);

                    setThemeVariable(parentColorValue, true);
                }
            }
            parentValue = parentColorValue;
        }

        valueField.setValue(null);
        if (ThemeVariablesManager.TRANSPARENT_COLOR_VALUE.equals(parentColorValue)) {
            colorValueField.setValue(null);
        } else {
            colorValueField.setValue(parentColorValue);
        }

        String placeHolder = valueField.getInputPrompt();
        if (!placeHolder.startsWith("var(")) {
            setInputPrompt(parentColorValue);
        }
    }

    public void setColorValue(String value) {
        valueField.setValue(value);
    }

    public Subscription addColorValueChangeListener(Consumer<ValueChangeEvent<String>> listener) {
        return getEventHub().subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    public void refreshJavaScriptComponent() {
        javaScript = JavaScript.getCurrent();
        javaScript.execute(jsComponent.getInitFunctionName() + "()");
    }

    protected void initColorValueField() {
        colorValueField.addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.isUserOriginated()) {
                valueField.setValue(ThemeVariableUtils.getColorString(valueChangeEvent.getValue()));
            }
        });
    }

    protected void initValueField() {
        valueField.addValueChangeListener(valueChangeEvent -> {
            String value = valueChangeEvent.getValue();
            if (value == null) {
                if (parentValue != null) {
                    value = parentValue;
                } else if (themeVariable.getThemeVariableDetails(currentTemplate) != null) {
                    value = themeVariable.getThemeVariableDetails(currentTemplate).getValue();
                }
            }

            if (!ThemeVariablesManager.TRANSPARENT_COLOR_VALUE.equals(value)) {
                value = ThemeVariableUtils.getColorString(value);
            }

            if (ThemeVariablesManager.TRANSPARENT_COLOR_VALUE.equals(value)) {
                colorValueField.setValue(null);
            } else {
                colorValueField.setValue(value);
            }

            boolean valueIsNull = valueChangeEvent.getValue() == null;
            resetBtn.setEnabled(!valueIsNull);

            if (valueIsNull && parentValue == null) {
                removeThemeVariable();
            } else {
                setThemeVariable(value, true);
            }
        });
    }

    protected void initResetBtn() {
        resetBtn.addClickListener(clickEvent -> {
            ThemeVariableDetails details = getThemeVariableDetailsByTemplate(currentTemplate);
            reset(details);
        });
    }

    protected ThemeVariableDetails getThemeVariableDetailsByTemplate(Template template) {
        return themeVariable.getThemeVariableDetails(template);
    }

    protected void reset(ThemeVariableDetails details) {
        if (getInputPrompt() != null &&
                !getInputPrompt().equals(details.getPlaceHolder())) {
            removeThemeVariable();
        }

        String name = themeVariable.getName();
        setCaption(name);
        setDescription(name);

        valueField.setValue(null);

        String value = parentValue != null
                ? parentValue
                : ThemeVariablesManager.TRANSPARENT_COLOR_VALUE.equals(details.getValue())
                ? details.getValue()
                : ThemeVariableUtils.getColorString(details.getValue());

        if (value.equals(ThemeVariablesManager.TRANSPARENT_COLOR_VALUE)) {
            colorValueField.setValue(null);
        } else {
            colorValueField.setValue(value);
        }

        if (currentTemplate != null
                && currentTemplate.getParent() != null
                && themeVariable.hasColorTemplate(currentTemplate)) {
            setThemeVariable(value, false);
        }

        if (parentValue == null) {
            setInputPrompt(details.getPlaceHolder());
        }
    }

    protected void setThemeVariable(String value, boolean isBaseThemeMode) {
        javaScript.execute(String.format(SET_THEME_VARIABLE_VOID, themeVariable.getName(), value));

        if (themeVariable.isRgbUsed()) {
            javaScript.execute(String.format(SET_THEME_VARIABLE_VOID, themeVariable.getName() + RGB_POSTFIX,
                    ThemeVariableUtils.convertHexToRGB(value)));
        }

        fireValueChangeEvent(value, isBaseThemeMode);
    }

    protected void removeThemeVariable() {
        javaScript.execute(String.format(REMOVE_THEME_VARIABLE_VOID, themeVariable.getName()));

        if (themeVariable.isRgbUsed()) {
            javaScript.execute(String.format(REMOVE_THEME_VARIABLE_VOID, themeVariable.getName() + RGB_POSTFIX));
        }

        fireValueChangeEvent(null, true);
    }

    protected void fireValueChangeEvent(@Nullable String value, boolean isBaseThemeMode) {
        ValueChangeEvent<String> valueChangeEvent = new ValueChangeEvent<>(valueField, value, value, isBaseThemeMode);
        publish(ValueChangeEvent.class, valueChangeEvent);
    }
}
