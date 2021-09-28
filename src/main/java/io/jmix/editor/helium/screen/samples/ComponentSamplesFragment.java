package io.jmix.editor.helium.screen.samples;

import io.jmix.core.Metadata;
import io.jmix.editor.helium.entity.Directory;
import io.jmix.editor.helium.entity.Grade;
import io.jmix.editor.helium.entity.User;
import io.jmix.ui.Dialogs;
import io.jmix.ui.Notifications;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.DialogAction;
import io.jmix.ui.action.tagpicker.TagLookupAction;
import io.jmix.ui.app.inputdialog.DialogActions;
import io.jmix.ui.app.inputdialog.InputParameter;
import io.jmix.ui.component.*;
import io.jmix.ui.component.calendar.ListCalendarEventProvider;
import io.jmix.ui.component.calendar.SimpleCalendarEvent;
import io.jmix.ui.component.data.datagrid.ContainerDataGridItems;
import io.jmix.ui.component.data.datagrid.ContainerTreeDataGridItems;
import io.jmix.ui.component.data.table.ContainerGroupTableItems;
import io.jmix.ui.component.data.table.ContainerTableItems;
import io.jmix.ui.component.data.table.ContainerTreeTableItems;
import io.jmix.ui.component.data.tree.ContainerTreeItems;
import io.jmix.ui.component.pagination.data.PaginationLoaderBinder;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataComponents;
import io.jmix.ui.screen.*;
import io.jmix.ui.theme.ThemeVariantsManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UiController("helium_ComponentSamplesFragment")
@UiDescriptor("component-samples-fragment.xml")
public class ComponentSamplesFragment extends ScreenFragment {

    protected static final int SAMPLE_DATA_SIZE = 10;

    // Basic functionality

    @Autowired
    protected RadioButtonGroup<String> sizeField;
    @Autowired
    protected TabSheet previewTabSheet;
    @Autowired
    protected ScrollBoxLayout innerPreviewBox;
    @Autowired
    protected Table<User> basicTable;
    @Autowired
    protected TagPicker<User> basicTagPicker;
    @Autowired
    protected ComboBox<String> basicRequiredComboBox;
    @Autowired
    protected ComboBox<String> basicComboBox;
    @Autowired
    protected RadioButtonGroup<String> basicRadioButtonGroup;
    @Autowired
    protected CheckBoxGroup<String> basicCheckBoxGroup;

    @Autowired
    protected Metadata metadata;
    @Autowired
    protected DataComponents dataComponents;
    @Autowired
    protected ThemeVariantsManager variantsManager;

    protected CollectionContainer<User> usersDc;
    protected CollectionContainer<Directory> directoriesDc;

    @Subscribe
    public void onInit(InitEvent event) {
        initSizeField();
        initDataContainers();
        initContainerSamples();
        initOptions();
    }

    @Subscribe("sizeField")
    public void onSizeFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        String size = event.getValue() == null
                ? ""
                : event.getValue();

        innerPreviewBox.setStyleName(size);
    }

    protected void initSizeField() {
        sizeField.setOptionsList(variantsManager.getThemeSizeList());
        sizeField.setValue(variantsManager.getThemeSizeUserSettingOrDefault());
    }

    protected void initDataContainers() {
        usersDc = dataComponents.createCollectionContainer(User.class);
        usersDc.setItems(generateUsersSampleData());

        directoriesDc = dataComponents.createCollectionContainer(Directory.class);
        directoriesDc.setItems(generateDirectoriesSampleData());
    }

    protected void initContainerSamples() {
        basicTable.setItems(new ContainerTableItems<>(usersDc));

        basicTagPicker.setOptionsList(usersDc.getItems());
        basicTagPicker.setValue(basicTagPicker.getOptions()
                .getOptions()
                .skip(2)
                .collect(Collectors.toList()));
    }

    protected List<User> generateUsersSampleData() {
        List<User> users = new ArrayList<>(SAMPLE_DATA_SIZE);
        users.add(createUser("Romeo Montague", "romeo", true));
        users.add(createUser("Juliet Capulet", "juliet", true));
        users.add(createUser("Dave Paris", "daveParis", false));
        users.add(createUser("Ted Montague", "tedMontague", false));
        users.add(createUser("Caroline Montague", "carolinMontague", true));
        users.add(createUser("Fulgencio Capulet", "fulgencio", true));
        users.add(createUser("Gloria Capulet", "gloriaCapulet", false));
        return users;
    }

    protected List<Directory> generateDirectoriesSampleData() {
        List<Directory> directories = new ArrayList<>(SAMPLE_DATA_SIZE);
        for (int i = 0; i < SAMPLE_DATA_SIZE; i++) {
            Directory parent = null;
            if (i > 0) {
                parent = directories.get(i - 1);
            }
            directories.add(createDirectory(i, parent));
        }
        return directories;
    }

    protected User createUser(String name, String login, boolean active) {
        User user = metadata.create(User.class);
        user.setName(name);
        user.setUsername(login);
        user.setActive(active);
        return user;
    }

    protected Directory createDirectory(int index, Directory parent) {
        Directory group = metadata.create(Directory.class);
        group.setName("directory " + index);
        if (parent != null) {
            group.setParent(parent);
        }
        return group;
    }

    protected void initOptions() {
        List<String> options = generateSampleOptions();
        basicCheckBoxGroup.setOptionsList(options);
        basicRadioButtonGroup.setOptionsList(options);
        basicComboBox.setOptionsList(options);
        basicRequiredComboBox.setOptionsList(options);
    }

    protected List<String> generateSampleOptions() {
        return Arrays.asList("Option 1", "Option 2", "Option 3");
    }

    // All components

    @Autowired
    protected TextField<String> textFieldRO;
    @Autowired
    protected TextField<String> textFieldD;

    @Autowired
    protected ComboBox<Grade> comboBoxRO;
    @Autowired
    protected ComboBox<Grade> comboBoxD;

    @Autowired
    protected EntityPicker<User> entityPickerRO;
    @Autowired
    protected EntityPicker<User> entityPickerD;
    @Autowired
    protected EntityPicker<User> entityPickerSmall;
    @Autowired
    protected EntityPicker<User> entityPickerMedium;
    @Autowired
    protected EntityPicker<User> entityPickerLarge;
    @Autowired
    protected EntityPicker<User> entityPicker;

    @Autowired
    protected EntityComboBox<User> entityComboBoxR;
    @Autowired
    protected EntityComboBox<User> entityComboBox;
    @Autowired
    protected EntityComboBox<User> entityComboBoxRO;
    @Autowired
    protected EntityComboBox<User> entityComboBoxD;
    @Autowired
    protected EntityComboBox<User> entityComboBoxMedium;
    @Autowired
    protected EntityComboBox<User> entityComboBoxSmall;
    @Autowired
    protected EntityComboBox<User> entityComboBoxLarge;

    @Autowired
    protected CheckBox checkBoxRO2;
    @Autowired
    protected CheckBox checkBoxD2;

    @Autowired
    protected MultiSelectList<User> multiSelectListSample;
    @Autowired
    protected MultiSelectList<User> multiSelectListRO;
    @Autowired
    protected MultiSelectList<User> multiSelectListDisabled;
    @Autowired
    protected MultiSelectList<User> multiSelectListRequired;
    @Autowired
    protected MultiSelectList<User> multiSelectListLarge;
    @Autowired
    protected MultiSelectList<User> multiSelectListMedium;
    @Autowired
    protected MultiSelectList<User> multiSelectListSmall;

    @Autowired
    protected SingleSelectList<User> singleSelectListSample;
    @Autowired
    protected SingleSelectList<User> singleSelectListRO;
    @Autowired
    protected SingleSelectList<User> singleSelectListDisabled;
    @Autowired
    protected SingleSelectList<User> singleSelectListRequired;
    @Autowired
    protected SingleSelectList<User> singleSelectListLarge;
    @Autowired
    protected SingleSelectList<User> singleSelectListMedium;
    @Autowired
    protected SingleSelectList<User> singleSelectListSmall;

    @Autowired
    protected RadioButtonGroup<Grade> radioButtonGroupD;
    @Autowired
    protected RadioButtonGroup<Grade> radioButtonGroupRO;

    @Autowired
    protected TagField<User> tagFieldSample;
    @Autowired
    protected TagField<User> tagFieldClearable;
    @Autowired
    protected TagField<User> tagFieldRO;
    @Autowired
    protected TagField<User> tagFieldDisabled;
    @Autowired
    protected TagField<User> tagFieldRequired;
    @Autowired
    protected TagField<User> tagFieldLarge;
    @Autowired
    protected TagField<User> tagFieldMedium;
    @Autowired
    protected TagField<User> tagFieldSmall;

    @Autowired
    protected TagPicker<User> tagPickerSample;
    @Autowired
    protected TagPicker<User> tagPickerInline;
    @Autowired
    protected TagPicker<User> tagPickerRO;
    @Autowired
    protected TagPicker<User> tagPickerDisabled;
    @Autowired
    protected TagPicker<User> tagPickerRequired;
    @Autowired
    protected TagPicker<User> tagPickerSmall;
    @Autowired
    protected TagPicker<User> tagPickerMedium;
    @Autowired
    protected TagPicker<User> tagPickerLarge;

    @Autowired
    protected Table<User> tableSample;
    @Autowired
    protected GroupTable<User> groupTableSample;
    @Autowired
    protected Table<User> largeTableSample;
    @Autowired
    protected Table<User> mediumTableSample;
    @Autowired
    protected Table<User> smallTableSample;
    @Autowired
    protected Table<User> tablePopupView;
    @Autowired
    protected TreeTable<Directory> treeTableSample;

    @Autowired
    protected DataGrid<User> dataGridSample;
    @Autowired
    protected TreeDataGrid<Directory> treeDataGridSample;

    @Autowired
    protected TabSheet tabSheet;
    @Autowired
    protected FlowBoxLayout tabSheetStylesBox;

    @Autowired
    protected Tree<Directory> tree;

    @Autowired
    protected SourceCodeEditor codeEditor;
    @Autowired
    protected SourceCodeEditor codeEditorD;
    @Autowired
    protected SourceCodeEditor codeEditorRO;

    @Autowired
    protected CheckBox highlightActiveLineCheck;
    @Autowired
    protected CheckBox printMarginCheck;
    @Autowired
    protected CheckBox showGutterCheck;

    @Autowired
    protected TwinColumn<Directory> twinColumnLarge;
    @Autowired
    protected TwinColumn<Directory> twinColumnMedium;
    @Autowired
    protected TwinColumn<Directory> twinColumnSmall;
    @Autowired
    protected TwinColumn<Directory> twinColumnRequired;
    @Autowired
    protected TwinColumn<Directory> twinColumnSample;

    @Autowired
    protected Pagination paginationSample;
    @Autowired
    protected Pagination paginationPerPageOptions;
    @Autowired
    protected Pagination paginationLarge;
    @Autowired
    protected Pagination paginationMedium;
    @Autowired
    protected Pagination paginationSmall;

    @Autowired
    protected SimplePagination simplePaginationSample;
    @Autowired
    protected SimplePagination simplePaginationItemsPerPage;
    @Autowired
    protected SimplePagination simplePaginationLarge;
    @Autowired
    protected SimplePagination simplePaginationMedium;
    @Autowired
    protected SimplePagination simplePaginationSmall;

    @Autowired
    protected ProgressBar progressBarP;
    @Autowired
    protected ProgressBar progressBar;

    @Autowired
    protected SuggestionField<User> suggestionFieldLarge;
    @Autowired
    protected SuggestionField<User> suggestionFieldMedium;
    @Autowired
    protected SuggestionField<User> suggestionFieldSmall;
    @Autowired
    protected SuggestionField<User> suggestionFieldDisabled;
    @Autowired
    protected SuggestionField<User> suggestionFieldReadOnly;
    @Autowired
    protected SuggestionField<User> suggestionFieldRequired;
    @Autowired
    protected SuggestionField<User> suggestionFieldSample;

    @Autowired
    protected EntitySuggestionField<User> entitySuggestionFieldLarge;
    @Autowired
    protected EntitySuggestionField<User> entitySuggestionFieldMedium;
    @Autowired
    protected EntitySuggestionField<User> entitySuggestionFieldSmall;
    @Autowired
    protected EntitySuggestionField<User> entitySuggestionFieldDisabled;
    @Autowired
    protected EntitySuggestionField<User> entitySuggestionFieldReadonly;
    @Autowired
    protected EntitySuggestionField<User> entitySuggestionFieldSample;

    @Autowired
    protected Calendar<Date> monthCalendar;
    @Autowired
    protected Calendar<Date> weekCalendar;
    @Autowired
    protected Calendar<Date> dayCalendar;

    @Autowired
    protected Dialogs dialogs;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected UiComponents uiComponents;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        textFieldRO.setValue("Value");
        textFieldD.setValue("Value");

        comboBoxRO.setValue(Grade.STANDARD);
        comboBoxD.setValue(Grade.STANDARD);

        checkBoxRO2.setValue(true);
        checkBoxD2.setValue(true);

        entityPicker.setValue(usersDc.getItems().get(0));
        entityPickerRO.setValue(usersDc.getItems().get(0));
        entityPickerD.setValue(usersDc.getItems().get(0));
        entityPickerLarge.setValue(usersDc.getItems().get(0));
        entityPickerMedium.setValue(usersDc.getItems().get(0));
        entityPickerSmall.setValue(usersDc.getItems().get(0));

        entityComboBox.setOptionsList(usersDc.getItems());
        entityComboBoxR.setOptionsList(usersDc.getItems());
        entityComboBoxRO.setOptionsList(usersDc.getItems());
        entityComboBoxD.setOptionsList(usersDc.getItems());
        entityComboBoxLarge.setOptionsList(usersDc.getItems());
        entityComboBoxMedium.setOptionsList(usersDc.getItems());
        entityComboBoxSmall.setOptionsList(usersDc.getItems());

        entityComboBox.setValue(usersDc.getItems().get(0));
        entityComboBoxR.setValue(usersDc.getItems().get(0));
        entityComboBoxRO.setValue(usersDc.getItems().get(0));
        entityComboBoxD.setValue(usersDc.getItems().get(0));
        entityComboBoxLarge.setValue(usersDc.getItems().get(0));
        entityComboBoxMedium.setValue(usersDc.getItems().get(0));
        entityComboBoxSmall.setValue(usersDc.getItems().get(0));

        multiSelectListSample.setOptionsList(usersDc.getItems());
        multiSelectListRO.setOptionsList(usersDc.getItems());
        multiSelectListDisabled.setOptionsList(usersDc.getItems());
        multiSelectListRequired.setOptionsList(usersDc.getItems());
        multiSelectListLarge.setOptionsList(usersDc.getItems());
        multiSelectListMedium.setOptionsList(usersDc.getItems());
        multiSelectListSmall.setOptionsList(usersDc.getItems());
        multiSelectListRO.setValue(usersDc.getItems().stream().skip(2).collect(Collectors.toList()));
        multiSelectListDisabled.setValue(usersDc.getItems().stream().skip(2).collect(Collectors.toList()));

        singleSelectListSample.setOptionsList(usersDc.getItems());
        singleSelectListRO.setOptionsList(usersDc.getItems());
        singleSelectListDisabled.setOptionsList(usersDc.getItems());
        singleSelectListRequired.setOptionsList(usersDc.getItems());
        singleSelectListLarge.setOptionsList(usersDc.getItems());
        singleSelectListMedium.setOptionsList(usersDc.getItems());
        singleSelectListSmall.setOptionsList(usersDc.getItems());
        singleSelectListRO.setValue(usersDc.getItems().get(0));
        singleSelectListDisabled.setValue(usersDc.getItems().get(0));

        radioButtonGroupRO.setValue(Grade.STANDARD);
        radioButtonGroupD.setValue(Grade.STANDARD);

        initTagPicker(tagPickerSample, usersDc);
        initTagPicker(tagPickerInline, usersDc);
        initTagPicker(tagPickerRO, usersDc);
        initTagPicker(tagPickerDisabled, usersDc);
        initTagPicker(tagPickerSmall, usersDc);
        initTagPicker(tagPickerMedium, usersDc);
        initTagPicker(tagPickerLarge, usersDc);
        tagPickerRequired.setOptionsList(usersDc.getItems());

        initTagField(tagFieldSample, usersDc);
        initTagField(tagFieldClearable, usersDc);
        initTagField(tagFieldRO, usersDc);
        initTagField(tagFieldDisabled, usersDc);
        initTagField(tagFieldLarge, usersDc);
        initTagField(tagFieldMedium, usersDc);
        initTagField(tagFieldSmall, usersDc);
        tagFieldRequired.setSearchExecutor(this::userSearchExecutor);

        tabSheetStylesBox.getComponents().stream()
                .filter(component -> component instanceof CheckBox)
                .map(component -> ((CheckBox) component))
                .forEach(checkBox -> checkBox.addValueChangeListener(this::changeTableStyle));

        tree.expandTree();

        highlightActiveLineCheck.setValue(codeEditor.isHighlightActiveLine());
        printMarginCheck.setValue(codeEditor.isShowPrintMargin());
        showGutterCheck.setValue(codeEditor.isShowGutter());

        codeEditorRO.setValue("highlightActiveLineCheck.setValue(codeEditor.isHighlightActiveLine());");
        codeEditorD.setValue("highlightActiveLineCheck.setValue(codeEditor.isHighlightActiveLine());");

        tableSample.setItems(new ContainerTableItems<>(usersDc));
        groupTableSample.setItems(new ContainerGroupTableItems<>(usersDc));
        smallTableSample.setItems(new ContainerTableItems<>(usersDc));
        mediumTableSample.setItems(new ContainerTableItems<>(usersDc));
        largeTableSample.setItems(new ContainerTableItems<>(usersDc));
        tablePopupView.setItems(new ContainerTableItems<>(usersDc));
        dataGridSample.setItems(new ContainerDataGridItems<>(usersDc));

        treeDataGridSample.setItems(new ContainerTreeDataGridItems<>(directoriesDc, "parent"));
        treeTableSample.setItems(new ContainerTreeTableItems<>(directoriesDc, "parent"));
        tree.setItems(new ContainerTreeItems<>(directoriesDc, "parent"));

        twinColumnSample.setOptionsList(directoriesDc.getItems());
        twinColumnRequired.setOptionsList(directoriesDc.getItems());
        twinColumnSmall.setOptionsList(directoriesDc.getItems());
        twinColumnMedium.setOptionsList(directoriesDc.getItems());
        twinColumnLarge.setOptionsList(directoriesDc.getItems());

        progressBar.setValue(0.5);
        progressBarP.setValue(0.5);

        suggestionFieldSample.setSearchExecutor(this::userSearchExecutor);
        suggestionFieldReadOnly.setSearchExecutor(this::userSearchExecutor);
        suggestionFieldDisabled.setSearchExecutor(this::userSearchExecutor);
        suggestionFieldRequired.setSearchExecutor(this::userSearchExecutor);
        suggestionFieldLarge.setSearchExecutor(this::userSearchExecutor);
        suggestionFieldMedium.setSearchExecutor(this::userSearchExecutor);
        suggestionFieldSmall.setSearchExecutor(this::userSearchExecutor);

        entitySuggestionFieldSample.setSearchExecutor(this::userSearchExecutor);
        entitySuggestionFieldReadonly.setSearchExecutor(this::userSearchExecutor);
        entitySuggestionFieldDisabled.setSearchExecutor(this::userSearchExecutor);
        entitySuggestionFieldLarge.setSearchExecutor(this::userSearchExecutor);
        entitySuggestionFieldMedium.setSearchExecutor(this::userSearchExecutor);
        entitySuggestionFieldSmall.setSearchExecutor(this::userSearchExecutor);

        ListCalendarEventProvider eventProvider = new ListCalendarEventProvider();

        SimpleCalendarEvent<Date> calendarEvent1 = new SimpleCalendarEvent<>();
        calendarEvent1.setCaption("Event 1");
        calendarEvent1.setDescription("Description 1");
        calendarEvent1.setStart(new Date(2020 - 1900, 2, 23));
        calendarEvent1.setEnd(DateUtils.addHours(calendarEvent1.getStart(), 4));
        eventProvider.addEvent(calendarEvent1);

        SimpleCalendarEvent<Date> calendarEvent2 = new SimpleCalendarEvent<>();
        calendarEvent2.setCaption("Event 2");
        calendarEvent2.setDescription("Description 2");
        calendarEvent2.setStart(new Date(2020 - 1900, 2, 25));
        calendarEvent2.setEnd(DateUtils.addHours(calendarEvent2.getStart(), 6));
        eventProvider.addEvent(calendarEvent2);

        SimpleCalendarEvent<Date> calendarEvent3 = new SimpleCalendarEvent<>();
        calendarEvent3.setCaption("Event 3");
        calendarEvent3.setDescription("Description 3");
        calendarEvent3.setStart(new Date(2020 - 1900, 2, 26));
        calendarEvent3.setEnd(DateUtils.addHours(calendarEvent3.getStart(), 2));
        calendarEvent3.setAllDay(true);
        eventProvider.addEvent(calendarEvent3);

        monthCalendar.setEventProvider(eventProvider);
        weekCalendar.setEventProvider(eventProvider);
        dayCalendar.setEventProvider(eventProvider);

        monthCalendar.addRangeSelectListener(dateCalendarRangeSelectEvent -> {
        });
        weekCalendar.addRangeSelectListener(dateCalendarRangeSelectEvent -> {
        });
        dayCalendar.addRangeSelectListener(dateCalendarRangeSelectEvent -> {
        });

        dayCalendar.setStartDate(new Date());
        dayCalendar.setEndDate(new Date());

        initPaginationComponent(paginationSample, usersDc);
        initPaginationComponent(paginationPerPageOptions, usersDc);
        initPaginationComponent(paginationLarge, usersDc);
        initPaginationComponent(paginationMedium, usersDc);
        initPaginationComponent(paginationSmall, usersDc);

        initPaginationComponent(simplePaginationSample, usersDc);
        initPaginationComponent(simplePaginationItemsPerPage, usersDc);
        initPaginationComponent(simplePaginationLarge, usersDc);
        initPaginationComponent(simplePaginationMedium, usersDc);
        initPaginationComponent(simplePaginationSmall, usersDc);
    }

    protected void initPaginationComponent(PaginationComponent pagination, CollectionContainer<User> usersDc) {
        CollectionLoader<User> usersDl = dataComponents.createCollectionLoader();
        usersDl.setContainer(dataComponents.createCollectionContainer(User.class));
        usersDl.setLoadDelegate(userLoadContext -> {
            int firstResult = userLoadContext.getQuery().getFirstResult();
            int maxResults = userLoadContext.getQuery().getMaxResults();
            return usersDc.getItems().stream()
                    .skip(firstResult)
                    .limit(maxResults)
                    .collect(Collectors.toList());
        });

        pagination.setDataBinder(getApplicationContext().getBean(PaginationLoaderBinder.class, usersDl));
        pagination.setTotalCountDelegate(() -> usersDc.getItems().size());
        usersDl.load();
    }

    protected void initTagPicker(TagPicker<User> tagPicker, CollectionContainer<User> usersDc) {
        tagPicker.setOptionsList(usersDc.getItems());
        tagPicker.setValue(tagPicker.getOptions().getOptions().skip(2).collect(Collectors.toList()));

        TagLookupAction lookupAction = (TagLookupAction) tagPicker.getAction(TagLookupAction.ID);
        if (lookupAction != null) {
            lookupAction.addActionPerformedListener(actionPerformedEvent -> {/* do nothing */});
        }
    }

    protected void initTagField(TagField<User> tagField, CollectionContainer<User> usersDc) {
        tagField.setSearchExecutor(this::userSearchExecutor);
        tagField.setValue(usersDc.getItems().stream().skip(2).collect(Collectors.toList()));
    }

    protected List<User> userSearchExecutor(String searchString, Map<String, Object> searchParams) {
        return usersDc.getItems().stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getName(), searchString))
                .collect(Collectors.toList());
    }

    protected void changeTableStyle(HasValue.ValueChangeEvent<Boolean> e) {
        String id = e.getComponent().getId();
        Boolean checked = e.getValue();
        if (checked != null) {
            if (checked) {
                tabSheet.addStyleName(prepareStyleName(id));
            } else {
                tabSheet.removeStyleName(prepareStyleName(id));
            }
        }
    }

    protected String prepareStyleName(@Nullable String stylename) {
        if (stylename == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stylename.length(); i++) {
            char c = stylename.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("-").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Install(to = "dataGridSample", subject = "detailsGenerator")
    protected Component dataGridDetailsGenerator(User user) {
        VBoxLayout mainLayout = uiComponents.create(VBoxLayout.class);
        mainLayout.setWidth("100%");
        mainLayout.setHeight("200px");

        return mainLayout;
    }

    @Subscribe("showDetailsBtn")
    public void onShowDetailsBtnClick(Button.ClickEvent event) {
        User singleSelected = dataGridSample.getSingleSelected();
        if (singleSelected != null) {
            dataGridSample.setDetailsVisible(singleSelected, true);
        }
    }

    @Subscribe("closeDetailsBtn")
    public void onCloseDetailsBtnClick(Button.ClickEvent event) {
        User singleSelected = dataGridSample.getSingleSelected();
        if (singleSelected != null) {
            dataGridSample.setDetailsVisible(singleSelected, false);
        }
    }

    @Subscribe("showMessageDialogBtn")
    public void onShowMessageDialogBtnClick(Button.ClickEvent event) {
        dialogs.createMessageDialog()
                .withCaption("Confirmation")
                .withMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Et sollicitudin quam massa id enim et. Purus parturient pretium arcu quis vitae feugiat sit quis. Sem dictum vel nisi, cursus purus nibh fermentum tortor. Ultrices scelerisque orci, ullamcorper imperdiet orci bibendum a, aliquet. Purus mauris vitae odio fermentum semper diam commodo quis. Pulvinar nulla duis adipiscing nunc eu laoreet laoreet. Ornare sodales donec malesuada id eu arcu lectus ipsum scelerisque.")
                .show();
    }

    @Subscribe("showOptionDialogBtn")
    public void onShowOptionDialogBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Title")
                .withMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Et sollicitudin quam massa id enim et. Purus parturient pretium arcu quis vitae feugiat sit quis. Sem dictum vel nisi, cursus purus nibh fermentum tortor. Ultrices scelerisque orci, ullamcorper imperdiet orci bibendum a, aliquet. Purus mauris vitae odio fermentum semper diam commodo quis. Pulvinar nulla duis adipiscing nunc eu laoreet laoreet. Ornare sodales donec malesuada id eu arcu lectus ipsum scelerisque.")
                .withActions(
                        new DialogAction(DialogAction.Type.OK)
                                .withHandler(e ->
                                        notifications.create()
                                                .withCaption("OK pressed")
                                                .show()
                                ),

                        new DialogAction(DialogAction.Type.CANCEL))
                .show();
    }

    @Subscribe("showInputDialogBtn")
    public void onShowInputDialogBtnClick(Button.ClickEvent event) {
        dialogs.createInputDialog(this)
                .withCaption("Enter values")
                .withParameters(
                        InputParameter.stringParameter("name")
                                .withCaption("Name").withRequired(true),
                        InputParameter.doubleParameter("quantity")
                                .withCaption("Quantity").withDefaultValue(1.0),
                        InputParameter.enumParameter("roleType", Grade.class)
                                .withCaption("Role Type")
                )
                .withActions(DialogActions.OK_CANCEL)
                .show();
    }

    @Subscribe("showTrayBtn")
    public void onShowTrayBtnClick(Button.ClickEvent event) {
        notifications.create()
                .withCaption("Tray notification")
                .withDescription("Hi there! I’m a Jmix’s tray message")
                .withType(Notifications.NotificationType.TRAY)
                .show();
    }

    @Subscribe("showHumanizedBtn")
    public void onShowHumanizedBtnClick(Button.ClickEvent event) {
        notifications.create()
                .withCaption("Humanized notification")
                .withDescription("Hi there! I’m a Jmix’s humanized message")
                .withType(Notifications.NotificationType.HUMANIZED)
                .show();
    }

    @Subscribe("showWarningBtn")
    public void onShowWarningBtnClick(Button.ClickEvent event) {
        notifications.create()
                .withCaption("Warning notification")
                .withDescription("Hi there! I’m a Jmix’s warning message")
                .withType(Notifications.NotificationType.WARNING)
                .withContentMode(ContentMode.HTML)
                .show();
    }

    @Subscribe("showErrorBtn")
    public void onShowErrorBtnClick(Button.ClickEvent event) {
        notifications.create()
                .withCaption("Error notification")
                .withDescription("Hi there! I’m a Jmix’s error message")
                .withType(Notifications.NotificationType.ERROR)
                .withContentMode(ContentMode.HTML)
                .show();
    }

    @Subscribe("showSystemBtn")
    public void onShowSystemBtnClick(Button.ClickEvent event) {
        notifications.create()
                .withCaption("System notification")
                .withDescription("Hi there! I’m a Jmix’s system message")
                .withType(Notifications.NotificationType.SYSTEM)
                .withContentMode(ContentMode.HTML)
                .show();
    }

    @Subscribe("highlightActiveLineCheck")
    protected void onHighlightActiveLineCheckValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue() != null) {
            codeEditor.setHighlightActiveLine(event.getValue());
        }
    }

    @Subscribe("printMarginCheck")
    protected void onPrintMarginCheckValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue() != null) {
            codeEditor.setShowPrintMargin(event.getValue());
        }
    }

    @Subscribe("showGutterCheck")
    protected void onShowGutterCheckValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue() != null) {
            codeEditor.setShowGutter(event.getValue());
        }
    }
}
