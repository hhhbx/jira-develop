package com.jiradev.jira.plugins.conditions;

import com.atlassian.jira.component.ComponentAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.opensymphony.module.propertyset.PropertySet;

import java.util.Map;

public class FieldValueCondition extends AbstractJiraCondition {
    private static final Logger log = LoggerFactory.getLogger(FieldValueCondition.class);

    @Override
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) {
        String selectedCustomField = (String) args.get("selectedCustomField");
        String requiredValue = (String) args.get("requiredValue");
        Issue issue = getIssue(transientVars);
        Object value = issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObject(selectedCustomField));
        if (value != null) {
            return value.toString().equals(requiredValue);
        } else {
            return false;
        }
    }
}

