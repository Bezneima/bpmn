/* (C)2020 */
package ru.mail.jira.plugins.bpmn.actions;

import com.atlassian.jira.web.action.JiraWebActionSupport;

public class ExampleAction extends JiraWebActionSupport {
  public ExampleAction() {}

  @Override
  public String execute() {
    return SUCCESS;
  }
}
