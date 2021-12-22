/* (C)2020 */
package ru.mail.jira.plugins.bpmn.controllers;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.jira.plugins.bpmn.camunda.BPMNService;
import ru.mail.jira.plugins.bpmn.camunda.TestParticipant;

@Slf4j
@RestController
@Path("/transitions")
public class TransitionsController {
  private final IssueManager issueManager;
  // private final TransitionManager transitionManager;
  // private final WorkflowManager workflowManager;
  // private final ChangeHistoryManager changeHistoryManager;
  private final BPMNService bpmnService;
  private final TestParticipant testParticipant;

  @Autowired
  public TransitionsController(
      @ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
      @ComponentImport IssueManager issueManager,
      // @ComponentImport TransitionManager transitionManager,
      // @ComponentImport WorkflowManager workflowManager,
      @ComponentImport ChangeHistoryManager changeHistoryManager,
      BPMNService bpmnService,
      TestParticipant testParticipant) {
    this.issueManager = issueManager;
    // this.transitionManager = transitionManager;
    // this.workflowManager = workflowManager;
    // this.changeHistoryManager = changeHistoryManager;
    this.bpmnService = bpmnService;
    this.testParticipant = testParticipant;
  }

  @AnonymousAllowed
  @GET
  @Path("/getCurrentTransitions/{key}")
  public String getCurrentUser(@PathParam("key") String key) throws IOException {
    log.info("Get current user rest api was used");
    Issue issue = issueManager.getIssueObject(key);
    if (issue == null) {
      log.error("Can't find the task: " + key);
      // return Response.status(Response.Status.NOT_FOUND).build();
      return null;
    }
    // List<BpmnTransition> AllChangeItems =
    //    changeHistoryManager.getAllChangeItems(issue).stream()
    //        .filter(changeHistoryItem -> changeHistoryItem.getField().equals("assignee"))
    //        .sorted(Comparator.comparing(ChangeHistoryItem::getCreated))
    //        .map(
    //            changeHistoryItem ->
    //                new BpmnTransition(
    //                    changeHistoryItem.getUser(),
    //                    changeHistoryItem.getCreated(),
    //                    changeHistoryItem.getFrom(),
    //                    changeHistoryItem.getTo()))
    //        .collect(Collectors.toList());

    // нужно сгенерировать xml ниже
    System.out.println("========НАЧАЛ========");
    System.out.println(bpmnService.generateTestBPMN());
    System.out.println("========СТООП========");
    return bpmnService.generateTestBPMN();
  }

  @AnonymousAllowed
  @GET
  @Path("/test/{key}")
  public String justTestParticipant(@PathParam("key") String key) throws IOException {
    return testParticipant.createOnlyParticipantShape();
  }
}
