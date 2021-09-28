package io.jmix.editor.helium.screen.download;

import io.jmix.core.Messages;
import io.jmix.ui.Notifications;
import io.jmix.ui.WindowParam;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Label;
import io.jmix.ui.component.TextArea;
import io.jmix.ui.download.DownloadFormat;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

@UiController("helium_DownloadScreen")
@UiDescriptor("download-screen.xml")
@DialogMode(forceDialog = true)
public class DownloadScreen extends Screen {

    public static final String BASE_THEME_MODE_PARAM = "baseThemeMode";
    public static final String TEXT_PARAM = "text";

    protected static final String FILE_NAME = "helium-ext-defaults.scss";

    @WindowParam(name = BASE_THEME_MODE_PARAM)
    protected String baseThemeMode;
    @WindowParam(name = TEXT_PARAM)
    protected String text;

    @Autowired
    protected TextArea<String> textArea;
    @Autowired
    protected Label<String> firstStepLabel;

    @Autowired
    protected Messages messages;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected Downloader downloader;

    @Subscribe
    public void onInit(InitEvent event) {
        firstStepLabel.setValue(messages.formatMessage(DownloadScreen.class, "firstStep", baseThemeMode));
        textArea.setValue(text);
    }

    @Subscribe("clipboardBtn")
    public void onClipboardBtnClick(Button.ClickEvent event) {
        notifications.create()
                .withCaption(messages.getMessage(DownloadScreen.class, "clipboardNotification"))
                .show();
    }

    @Subscribe("downloadBtn")
    public void onDownloadBtnClick(Button.ClickEvent event) {
        downloader.download(
                text.getBytes(StandardCharsets.UTF_8),
                FILE_NAME,
                DownloadFormat.TEXT);
    }

    @Subscribe("closeBtn")
    public void onCloseBtnClick(Button.ClickEvent event) {
        closeWithDefaultAction();
    }
}