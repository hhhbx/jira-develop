package com.jiradev.jira.plugins.panels.issue;

import com.atlassian.core.util.collection.EasyList;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.atlassian.jira.issue.Issue;
import java.util.List;

public class UserRoleIssueTabPanel extends AbstractIssueTabPanel {

    private static final Logger log = LoggerFactory.getLogger(UserRoleIssueTabPanel.class);

    @Override
    public List getActions(Issue issue, ApplicationUser remoteUser) {
        return EasyList.build(new UserRoleIssueAction(super.descriptor, issue.getProjectObject()));
    }
    @Override
    public boolean showPanel(Issue issue, ApplicationUser remoteUser)
    {
        return true;
    }
}