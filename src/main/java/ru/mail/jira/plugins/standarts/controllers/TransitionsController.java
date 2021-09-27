/* (C)2020 */
package ru.mail.jira.plugins.standarts.controllers;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.changehistory.ChangeHistory;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.issue.history.ChangeItemBean;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.transition.TransitionManager;
import com.atlassian.jira.transition.WorkflowTransitionEntry;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.opensymphony.workflow.loader.ActionDescriptor;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@Path("/transitions")
public class TransitionsController {
  private final IssueManager issueManager;
  private final TransitionManager transitionManager;
  private final WorkflowManager workflowManager;
  private final ChangeHistoryManager changeHistoryManager;

  @Autowired
  public TransitionsController(
      @ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
      @ComponentImport IssueManager issueManager,
      @ComponentImport TransitionManager transitionManager,
      @ComponentImport WorkflowManager workflowManager,
      @ComponentImport ChangeHistoryManager changeHistoryManager) {
    this.issueManager = issueManager;
    this.transitionManager = transitionManager;
    this.workflowManager = workflowManager;
    this.changeHistoryManager = changeHistoryManager;
  }

  @AnonymousAllowed
  @GET
  @Path("/getCurrentTransitions")
  public Response getCurrentUser() {
    log.info("Get current user rest api was used");
    Issue issue = issueManager.getIssueObject("TEST-2");
    System.out.println(issue.getWorkflowId());

    Collection<WorkflowTransitionEntry> a =
        transitionManager.getTransitions(workflowManager.getWorkflows());

    Collection<ActionDescriptor> b = workflowManager.getWorkflow(issue).getAllActions();

    List<ChangeItemBean> changeItemBeans =
        changeHistoryManager.getChangeItemsForField(issue, "status");
    List<ChangeHistory> changeHistories = changeHistoryManager.getChangeHistories(issue);
    return Response.ok().build();
  }
}
