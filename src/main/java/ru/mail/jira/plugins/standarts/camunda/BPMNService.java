/* (C)2021 */
package ru.mail.jira.plugins.standarts.camunda;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.GatewayDirection;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BPMNService {

  @Autowired
  public BPMNService() {}

  /*
  public static String generateTestBPMN() { // TODO Убрать статик
    BpmnModelInstance modelInstance = Bpmn.createEmptyModel();
    Definitions definitions = modelInstance.newInstance(Definitions.class);
    definitions.setTargetNamespace("http://camunda.org/examples");
    modelInstance.setDefinitions(definitions);

    Bpmn.validateModel(modelInstance);

    String xmlString = Bpmn.convertToString(modelInstance);
    return xmlString;
  }
  // empty implementation

  protected <T extends BpmnModelElementInstance> T createElement(
      BpmnModelElementInstance parentElement, String id, Class<T> elementClass) {
    T element = parentElement.getModelInstance().newInstance(elementClass);
    element.setAttributeValue("id", id, true);
    parentElement.addChildElement(element);
    return element;
  }

  public SequenceFlow createSequenceFlow(Process process, FlowNode from, FlowNode to) {
    String identifier = from.getId() + "-" + to.getId();
    SequenceFlow sequenceFlow = createElement(process, identifier, SequenceFlow.class);
    process.addChildElement(sequenceFlow);
    sequenceFlow.setSource(from);
    from.getOutgoing().add(sequenceFlow);
    sequenceFlow.setTarget(to);
    to.getIncoming().add(sequenceFlow);
    return sequenceFlow;
  }
  */

  protected static <T extends BpmnModelElementInstance> T createElement(
      BpmnModelInstance modelInstance,
      BpmnModelElementInstance parentElement,
      String id,
      Class<T> elementClass) {
    T element = modelInstance.newInstance(elementClass);
    element.setAttributeValue("id", id, true);
    parentElement.addChildElement(element);
    return element;
  }

  public static SequenceFlow createSequenceFlow(
      BpmnModelInstance modelInstance, Process process, FlowNode from, FlowNode to) {
    String identifier = from.getId() + "-" + to.getId();
    SequenceFlow sequenceFlow =
        createElement(modelInstance, process, identifier, SequenceFlow.class);
    process.addChildElement(sequenceFlow);
    sequenceFlow.setSource(from);
    from.getOutgoing().add(sequenceFlow);
    sequenceFlow.setTarget(to);
    to.getIncoming().add(sequenceFlow);
    return sequenceFlow;
  }

  public String generateTestBPMN() {
    // научиться сюда записывать и создавать наши транзишены и потом просто возвращать xml, на фронте в bpmn.io брать и показывать
    BpmnModelInstance modelInstance =
        Bpmn.createProcess()
            .name("BPMN API Invoice Process")
            .executable()
            .startEvent()
            .name("Invoice received")
            .camundaFormKey("embedded:app:forms/start-form.html")
            .userTask()
            .name("Assign Approver")
            .camundaFormKey("embedded:app:forms/assign-approver.html")
            .camundaAssignee("demo")
            .userTask()
            .id("approveInvoice")
            .name("Approve Invoice")
            .camundaFormKey("embedded:app:forms/approve-invoice.html")
            .camundaAssignee("${approver}")
            .exclusiveGateway()
            .name("Invoice approved?")
            .gatewayDirection(GatewayDirection.Diverging)
            .condition("yes", "${approved}")
            .userTask()
            .name("Prepare Bank Transfer")
            .camundaFormKey("embedded:app:forms/prepare-bank-transfer.html")
            .camundaCandidateGroups("accounting")
            .serviceTask()
            .name("Archive Invoice")
            .endEvent()
            .name("Invoice processed")
            .moveToLastGateway()
            .condition("no", "${!approved}")
            .userTask()
            .name("Review Invoice")
            .camundaFormKey("embedded:app:forms/review-invoice.html")
            .camundaAssignee("demo")
            .exclusiveGateway()
            .name("Review successful?")
            .gatewayDirection(GatewayDirection.Diverging)
            .condition("no", "${!clarified}")
            .endEvent()
            .name("Invoice not processed")
            .moveToLastGateway()
            .condition("yes", "${clarified}")
            .connectTo("approveInvoice")
            .done();

    // validate and write model to file
    Bpmn.validateModel(modelInstance);

    return Bpmn.convertToString(modelInstance);
  }
}
