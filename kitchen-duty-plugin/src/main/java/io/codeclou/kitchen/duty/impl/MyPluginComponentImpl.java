package io.codeclou.kitchen.duty.impl;

import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.sal.api.ApplicationProperties;
import io.codeclou.kitchen.duty.api.MyPluginComponent;


public class MyPluginComponentImpl implements MyPluginComponent {
    private final ApplicationProperties applicationProperties;


    private final UserSearchService userSearchService;

    public MyPluginComponentImpl(final ApplicationProperties applicationProperties,final UserSearchService userSearchService) {
        this.userSearchService = userSearchService;
        this.applicationProperties = applicationProperties;
    }

    public String getName() {
        if (null != applicationProperties) {
            return "myComponent:" + applicationProperties.getDisplayName();
        }

        return "myComponent";
    }
}
