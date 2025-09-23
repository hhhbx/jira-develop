package com.jiradev.jira.plugins.validators;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.Issue;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class FieldValueValidator implements Validator
{
    private static final Logger log = LoggerFactory.getLogger(FieldValueValidator.class);
    
    public void validate(Map transientVars, Map args, PropertySet ps) throws InvalidInputException
    {
        try {
            log.debug("Field Value Validator: Starting validation");
            
            // 验证参数
            String selectedCustomField = (String) args.get("selectedCustomField");
            String requiredValue = (String) args.get("requiredValue");
            
            if (selectedCustomField == null || selectedCustomField.trim().isEmpty()) {
                log.warn("Field Value Validator: selectedCustomField is null or empty");
                throw new InvalidInputException("Configuration error: Custom field not specified.");
            }
            
            if (requiredValue == null) {
                log.warn("Field Value Validator: requiredValue is null");
                throw new InvalidInputException("Configuration error: Required value not specified.");
            }
            
            // 获取Issue
            Issue issue = (Issue) transientVars.get("issue");
            if (issue == null) {
                log.warn("Field Value Validator: Issue is null");
                throw new InvalidInputException("System error: Issue not found.");
            }
            
            // 获取自定义字段
            CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(selectedCustomField);
            if (customField == null) {
                log.warn("Field Value Validator: Custom field not found for ID: " + selectedCustomField);
                throw new InvalidInputException("Configuration error: Custom field not found.");
            }
            
            // 获取字段值
            Object value = issue.getCustomFieldValue(customField);
            log.debug("Field Value Validator: Field '{}' has value: {}", customField.getName(), value);
            log.debug("Field Value Validator: Required value: {}", requiredValue);
            
            String fieldName = customField.getName();
            
            if(value == null) {
                log.debug("Field Value Validator: Field value is null, validation failed");
                throw new InvalidInputException(fieldName + " must contain the value '" + requiredValue + "'.");
            } else {
                String actualValue = value.toString();
                if(!actualValue.equals(requiredValue)) {
                    log.debug("Field Value Validator: Field value '{}' does not match required value '{}'", actualValue, requiredValue);
                    throw new InvalidInputException(fieldName + " must contain the value '" + requiredValue + "'. Current value: '" + actualValue + "'.");
                } else {
                    log.debug("Field Value Validator: Validation passed");
                }
            }
        } catch (InvalidInputException e) {
            // 重新抛出验证异常
            throw e;
        } catch (Exception e) {
            log.error("Field Value Validator: Unexpected error during validation", e);
            throw new InvalidInputException("System error during validation: " + e.getMessage());
        }
    }
}
