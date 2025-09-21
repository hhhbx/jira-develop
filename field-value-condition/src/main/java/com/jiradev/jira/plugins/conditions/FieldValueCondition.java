package com.jiradev.jira.plugins.conditions;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
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
        try {
            log.debug("Field Value Condition: Starting condition check");
            
            // 验证参数
            String selectedCustomField = (String) args.get("selectedCustomField");
            String requiredValue = (String) args.get("requiredValue");
            
            if (selectedCustomField == null || selectedCustomField.trim().isEmpty()) {
                log.warn("Field Value Condition: selectedCustomField is null or empty");
                return false;
            }
            
            if (requiredValue == null) {
                log.warn("Field Value Condition: requiredValue is null");
                return false;
            }
            
            // 获取Issue
            Issue issue = getIssue(transientVars);
            if (issue == null) {
                log.warn("Field Value Condition: Issue is null");
                return false;
            }
            
            // 获取自定义字段
            CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(selectedCustomField);
            if (customField == null) {
                log.warn("Field Value Condition: Custom field not found for ID: " + selectedCustomField);
                return false;
            }
            
            // 获取字段值
            Object value = issue.getCustomFieldValue(customField);
            log.debug("Field Value Condition: Field '{}' has value: {}", customField.getName(), value);
            log.debug("Field Value Condition: Required value: {}", requiredValue);
            
            if (value != null) {
                String actualValue = value.toString();
                boolean result = actualValue.equals(requiredValue);
                 log.info("Field Value Condition: Comparison result: start");
                 log.info("Field Value Condition: Comparison result: {}", result);
                 log.info("Field Value Condition: actual: {}", actualValue);
                 log.info("Field Value Condition: required: {}", requiredValue);
                 log.info("Field Value Condition: Comparison result: end");
                return result;
            } else {
                log.debug("Field Value Condition: Field value is null, required: '{}'", requiredValue);
                return false;
            }
        } catch (Exception e) {
            log.error("Field Value Condition: Error in passesCondition", e);
            return false;
        }
    }
}

