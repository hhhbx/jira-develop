package com.jiradev.jira.plugins.conditions;
import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;

import java.util.Map;

/*
This is the factory class responsible for dealing with the UI for the post-function.
This is typically where you put default values into the velocity context and where you store user input.
 */

public class FieldValueConditionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginConditionFactory {
    @Override
    protected void getVelocityParamsForInput(Map velocityParams)
    {
        //Popluate the velocityParams object with all the custom fields
        velocityParams.put("customFields", ComponentAccessor.getCustomFieldManager().getCustomFieldObjects());
    }
    @Override
    protected void getVelocityParamsForEdit(Map velocityParams, AbstractDescriptor descriptor)
    {
        getVelocityParamsForInput(velocityParams);
        getVelocityParamsForView(velocityParams, descriptor);
    }
    @Override
    protected void getVelocityParamsForView(Map velocityParams, AbstractDescriptor descriptor)
    {
        if (!(descriptor instanceof ConditionDescriptor))
        {
            throw new IllegalArgumentException("Descriptor must be a ConditionDescriptor.");
        }

        ConditionDescriptor conditionDescriptor = (ConditionDescriptor) descriptor;
        String customFieldId = conditionDescriptor.getArgs().get("selectedCustomField").toString();
        CustomField customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(customFieldId);
        velocityParams.put("requiredValue", conditionDescriptor.getArgs().get("requiredValue"));
        velocityParams.put("selectedCustomField", conditionDescriptor.getArgs().get("selectedCustomField"));
        velocityParams.put("selectedCustomFieldName", customField.getName());
    }
    @Override
    public Map getDescriptorParams(Map conditionParams)
    {
        // Process The map
        String requiredValue = extractSingleParam(conditionParams, "requiredValue");
        String selectedCustomField = extractSingleParam(conditionParams, "selectedCustomField");
        return EasyMap.build("requiredValue", requiredValue,"selectedCustomField",selectedCustomField);
    }
}