package io.codeclou.kitchen.duty.webwork;

import com.atlassian.webresource.api.assembler.PageBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;

import javax.inject.Inject;

public class KitchenDutyPlanningWebworkAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(KitchenDutyPlanningWebworkAction.class);


    // PageBuilderService 用来告诉 Jira 页面需要哪些 web-resource（CSS/JS等）
    private final PageBuilderService pageBuilderService;

    @Inject
    public KitchenDutyPlanningWebworkAction(PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }


    @Override
    @SupportedMethods(RequestMethod.GET)
    public String doDefault() {      // 处理 GET
        requireResources();
        return "kitchen-duty-planning-success";                // 需要在 xml 里有 <view name="success">...</view>
    }
    @Override
    @SupportedMethods(RequestMethod.POST)
    protected String doExecute() {   // 处理 POST
        requireResources();
        return "kitchen-duty-planning-success";              // 需要在 xml 里有 <view name="success">...</view>
    }

    /**
     * 把资源声明封装成一个私有方法，避免重复写
     */
    private void requireResources() {
        pageBuilderService
            .assembler()
            .resources()
            .requireWebResource("io.codeclou.kitchen-duty-plugin:kitchen-duty-plugin-resources")
            .requireWebResource("io.codeclou.kitchen-duty-plugin:kitchen-duty-plugin-resources--planning-page");

        // 你也可以在这里 log 一下方便调试
        log.debug("Kitchen Duty Planning page resources required.");
    }
}
