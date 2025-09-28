package com.atlassian.plugins.tutorial.refapp.config;

import com.atlassian.plugins.tutorial.refapp.api.MyPluginComponent;
import com.atlassian.plugins.tutorial.refapp.impl.MyPluginComponentImpl;
import com.atlassian.plugins.osgi.javaconfig.configs.beans.ModuleFactoryBean;
import com.atlassian.plugins.osgi.javaconfig.configs.beans.PluginAccessorBean;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.websudo.WebSudoManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.osgi.framework.ServiceRegistration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.exportOsgiService;
import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.importOsgiService;

@Configuration
@Import({
        ModuleFactoryBean.class,
        PluginAccessorBean.class
})
public class MyPluginJavaConfig {


    // imports ApplicationProperties from OSGi
    @Bean
    public ApplicationProperties applicationProperties() {
        return importOsgiService(ApplicationProperties.class);
    }

    // imports SAL API components from OSGi
    @Bean
    public UserManager userManager() {
        return importOsgiService(UserManager.class);
    }

    @Bean
    public LoginUriProvider loginUriProvider() {
        return importOsgiService(LoginUriProvider.class);
    }

    @Bean
    public WebSudoManager webSudoManager() {
        return importOsgiService(WebSudoManager.class);
    }
    @Bean
    public TemplateRenderer templateRenderer() {
        return importOsgiService(TemplateRenderer.class);
    }

    @Bean
    public PluginSettingsFactory pluginSettingsFactory() {
        return importOsgiService(PluginSettingsFactory.class);
    }

    @Bean
    public MyPluginComponent myPluginComponent(ApplicationProperties applicationProperties) {
        return new MyPluginComponentImpl(applicationProperties);
    }

    // Exports MyPluginComponent as an OSGi service
    @Bean
    public FactoryBean<ServiceRegistration> registerMyDelegatingService(
            final MyPluginComponent mypluginComponent) {
        return exportOsgiService(mypluginComponent, null, MyPluginComponent.class);
    }
}