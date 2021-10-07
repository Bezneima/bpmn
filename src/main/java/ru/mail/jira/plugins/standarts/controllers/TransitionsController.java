/* (C)2020 */
package ru.mail.jira.plugins.standarts.controllers;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.changehistory.ChangeHistoryItem;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.transition.TransitionManager;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.mail.jira.plugins.standarts.DTOs.BpmnTransition;
import ru.mail.jira.plugins.standarts.camunda.BPMNService;

@Slf4j
@Controller
@Path("/transitions")
public class TransitionsController {
  private final IssueManager issueManager;
  private final TransitionManager transitionManager;
  private final WorkflowManager workflowManager;
  private final ChangeHistoryManager changeHistoryManager;
  private final BPMNService bpmnService;

  @Autowired
  public TransitionsController(
      @ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
      @ComponentImport IssueManager issueManager,
      @ComponentImport TransitionManager transitionManager,
      @ComponentImport WorkflowManager workflowManager,
      @ComponentImport ChangeHistoryManager changeHistoryManager,
      BPMNService bpmnService) {
    this.issueManager = issueManager;
    this.transitionManager = transitionManager;
    this.workflowManager = workflowManager;
    this.changeHistoryManager = changeHistoryManager;
    this.bpmnService = bpmnService;
  }

  @AnonymousAllowed
  @GET
  @Path("/getCurrentTransitions/{key}")
  public Response getCurrentUser(@PathParam("key") String key) {
    log.info("Get current user rest api was used");
    Issue issue = issueManager.getIssueObject(key);
    if (issue == null) {
      log.error("Can't find the task: " + key);
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<BpmnTransition> AllChangeItems =
        changeHistoryManager.getAllChangeItems(issue).stream()
            .filter(changeHistoryItem -> changeHistoryItem.getField().equals("assignee"))
            .sorted(Comparator.comparing(ChangeHistoryItem::getCreated))
            .map(
                changeHistoryItem ->
                    new BpmnTransition(
                        changeHistoryItem.getUser(),
                        changeHistoryItem.getCreated(),
                        changeHistoryItem.getFrom(),
                        changeHistoryItem.getTo()))
            .collect(Collectors.toList());

    // нужно сгенерировать xml ниже
    System.out.println(bpmnService.generateTestBPMN());
    return Response.ok(bpmnService.generateTestBPMN()).build();
  }
}
