/* (C)2021 */
package ru.mail.jira.plugins.bpmn.DTOs;

import java.sql.Timestamp;

public class BpmnTransition {

  String user;
  Timestamp created;
  String fromAssigned;
  String toAssigned;

  public BpmnTransition(String user, Timestamp created, String fromAssigned, String toAssigned) {
    this.user = user;
    this.created = created;
    this.fromAssigned = fromAssigned;
    this.toAssigned = toAssigned;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public Timestamp getCreated() {
    return created;
  }

  public void setCreated(Timestamp created) {
    this.created = created;
  }

  public String getFromAssigned() {
    return fromAssigned;
  }

  public void setFromAssigned(String fromAssigned) {
    this.fromAssigned = fromAssigned;
  }

  public String getToAssigned() {
    return toAssigned;
  }

  public void setToAssigned(String toAssigned) {
    this.toAssigned = toAssigned;
  }
}
