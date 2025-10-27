package io.codeclou.kitchen.duty.impl;

import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import io.codeclou.kitchen.duty.api.MyPluginComponent;


public class MyPluginComponentImpl implements MyPluginComponent {
    private final ApplicationProperties applicationProperties;


    private final UserSearchService userSearchService;
    private final PageBuilderService pageBuilderService;

    public MyPluginComponentImpl(final ApplicationProperties applicationProperties,final UserSearchService userSearchService,final PageBuilderService pageBuilderService) {
        this.userSearchService = userSearchService;
        this.applicationProperties = applicationProperties;
        this.pageBuilderService = pageBuilderService;
    }

    public String getName() {
        if (null != applicationProperties) {
            return "myComponent:" + applicationProperties.getDisplayName();
        }

        return "myComponent";
    }
}
