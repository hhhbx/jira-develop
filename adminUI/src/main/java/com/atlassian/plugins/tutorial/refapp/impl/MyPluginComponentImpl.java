package com.atlassian.plugins.tutorial.refapp.impl;

import com.atlassian.plugins.tutorial.refapp.api.MyPluginComponent;
import com.atlassian.sal.api.ApplicationProperties;


public class MyPluginComponentImpl implements MyPluginComponent {
    private final ApplicationProperties applicationProperties;


    public MyPluginComponentImpl(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getName() {
        if (null != applicationProperties) {
            return "myComponent:" + applicationProperties.getDisplayName();
        }

        return "myComponent";
    }
}