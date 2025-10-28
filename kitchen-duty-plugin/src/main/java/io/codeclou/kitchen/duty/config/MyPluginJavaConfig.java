package io.codeclou.kitchen.duty.config;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import io.codeclou.kitchen.duty.api.MyPluginComponent;
import io.codeclou.kitchen.duty.impl.MyPluginComponentImpl;
import com.atlassian.plugins.osgi.javaconfig.configs.beans.ModuleFactoryBean;
import com.atlassian.plugins.osgi.javaconfig.configs.beans.PluginAccessorBean;
import com.atlassian.sal.api.ApplicationProperties;
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

    @Bean
    public UserSearchService userSearchService() {
        return importOsgiService(UserSearchService.class);
    }

    @Bean
    public PageBuilderService pageBuilderService() {
        return importOsgiService(PageBuilderService.class);
    }

    @Bean
    public ActiveObjects activeObjects() {
        return importOsgiService(ActiveObjects.class);
    }

    @Bean
    public UserManager userManager() {
        return importOsgiService(UserManager.class);
    }
    @Bean
    public MyPluginComponent myPluginComponent(ApplicationProperties applicationProperties, UserSearchService userSearchService, PageBuilderService pageBuilderService, ActiveObjects activeObjects, UserManager userManager) {
        return new MyPluginComponentImpl(applicationProperties, userSearchService, pageBuilderService,activeObjects,userManager);
    }

    // Exports MyPluginComponent as an OSGi service
    @Bean
    public FactoryBean<ServiceRegistration> registerMyDelegatingService(
            final MyPluginComponent mypluginComponent) {
        return exportOsgiService(mypluginComponent, null, MyPluginComponent.class);
    }
}
