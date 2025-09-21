package com.jiradev.jira.plugins.panels.issue;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueAction;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanelModuleDescriptor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleActors;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class UserRoleIssueAction extends AbstractIssueAction {

    private ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager.class);
    private TreeMap people = new TreeMap();
    private Project project;

    public UserRoleIssueAction(IssueTabPanelModuleDescriptor issueTabPanelModuleDescriptor, Project project){
        super(issueTabPanelModuleDescriptor);
        this.project = project;
    }

    @Override
    public Date getTimePerformed(){
        return null;
    }

    @Override
    public void populateVelocityParams(Map params){
        //Get all the project roles
        Collection<ProjectRole> projectRoles = projectRoleManager.getProjectRoles();
        //Iterate through each role and get the users associated with the role
        for (ProjectRole projectRole : projectRoles){
            ProjectRoleActors roleActors = projectRoleManager.getProjectRoleActors(projectRole, project);
            people.put(projectRole.getName(),roleActors.getUsers());
        }
        params.put("people",people);
        params.put("avatarService",ComponentAccessor.getAvatarService());
    }
}