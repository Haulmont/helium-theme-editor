package io.jmix.editor.helium;

import com.google.common.base.Strings;
import io.jmix.core.security.CoreSecurityConfiguration;
import io.jmix.editor.helium.components.themevariablefield.ThemeVariableField;
import io.jmix.editor.helium.components.themevariablefield.ThemeVariableFieldLoader;
import io.jmix.ui.sys.registration.ComponentRegistration;
import io.jmix.ui.sys.registration.ComponentRegistrationBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.sql.DataSource;

@EnableConfigurationProperties(HeliumEditorProperties.class)
@SpringBootApplication
public class HeliumEditorApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(HeliumEditorApplication.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix="main.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @EventListener
    public void printApplicationUrl(ApplicationStartedEvent event) {
        LoggerFactory.getLogger(HeliumEditorApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }

    @Bean
    public ComponentRegistration themeVariableField() {
        return ComponentRegistrationBuilder.create(ThemeVariableField.NAME)
                .withComponentClass(ThemeVariableField.class)
                .withComponentLoaderClass(ThemeVariableFieldLoader.class)
                .build();
    }

    @EnableWebSecurity
    public class HeliumThemeEditorSecurityConfiguration extends CoreSecurityConfiguration {
    }
}
