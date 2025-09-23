package com.jiradev.jira.plugins.validators;

import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;

import java.util.Map;

public class FieldValueValidatorFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory {
    protected void getVelocityParamsForInput(Map velocityParams) {
        //the default message
        velocityParams.put("customFields", ComponentAccessor.getCustomFieldManager().getCustomFieldObjects());
    }

    protected void getVelocityParamsForEdit(Map velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
        getVelocityParamsForView(velocityParams, descriptor);
    }

    protected void getVelocityParamsForView(Map velocityParams, AbstractDescriptor descriptor) {
        if (!(descriptor instanceof ValidatorDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a ValidatorDescriptor.");
        }

        ValidatorDescriptor validatorDescriptor = (ValidatorDescriptor) descriptor;
        String customFieldId = validatorDescriptor.getArgs().get("selectedCustomField").toString();
        CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(customFieldId);
        velocityParams.put("requiredValue", validatorDescriptor.getArgs().get("requiredValue"));
        velocityParams.put("selectedCustomField", validatorDescriptor.getArgs().get("selectedCustomField"));
        velocityParams.put("selectedCustomFieldName", customField.getName());
    }

    public Map getDescriptorParams(Map validatorParams) {
        // Process The map
        String requiredValue = extractSingleParam(validatorParams, "requiredValue");
        String selectedCustomField = extractSingleParam(validatorParams, "selectedCustomField");
        return EasyMap.build("requiredValue", requiredValue, "selectedCustomField", selectedCustomField);
    }
}
