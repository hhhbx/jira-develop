package io.codeclou.kitchen.duty.webwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;

public class KitchenDutyPlanningWebworkAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(KitchenDutyPlanningWebworkAction.class);

    @Override
    @SupportedMethods(RequestMethod.GET)
    public String doDefault() {      // 处理 GET
        return INPUT;                // 需要在 xml 里有 <view name="input">...</view>
    }
    @Override
    @SupportedMethods(RequestMethod.POST)
    protected String doExecute() {   // 处理 POST
        return SUCCESS;              // 需要在 xml 里有 <view name="success">...</view>
    }

}
