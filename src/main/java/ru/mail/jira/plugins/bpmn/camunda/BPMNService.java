/* (C)2021 */
package ru.mail.jira.plugins.bpmn.camunda;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BPMNService {

  @Autowired
  public BPMNService() {}

  public String generateTestBPMN() {
    // научиться сюда записывать и создавать наши транзишены и потом просто возвращать xml, на
    // фронте в bpmn.io брать и показывать

    /*
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
    */

    BpmnModelInstance bpmnModelInstance =
        Bpmn.createEmptyModel(); // создал тут пустую модель в браузере это {}

    Definitions definitions = bpmnModelInstance.newInstance(Definitions.class);
    definitions.setTargetNamespace("http://camunda.org/examples");

    // нужна тк в нех находятся партишены  (пулы)
    Collaboration collaboration =
        createEmptyCollaboration("My_collaboration_name", "My_collaboration_id", bpmnModelInstance);
    Process process = createEmptyProcess("My_process_name", "My_process_id", bpmnModelInstance);
    Participant participant =
        createEmptyParticipant(
            "My_participant_name", "My_participant", "My_process_id", bpmnModelInstance);

    definitions.addChildElement(collaboration);
    definitions.addChildElement(process);
    collaboration.addChildElement(participant);

    // * Тут всякое сейчас пишу *//
    StartEvent startEvent = createElement(process, "start", StartEvent.class, bpmnModelInstance);
    // StartEventBuilder startEventBuilder = new StartEventBuilder(bpmnModelInstance, startEvent);
    // BpmnModelInstance a = startEventBuilder.createBpmnShape(startEvent).builder().done();

    // Тут создаю евент старта и конца а между ними сами действия(транизишены)

    UserTask task1 = createElement(process, "task1", UserTask.class, bpmnModelInstance);
    task1.setName("User Task");
    EndEvent endEvent = createElement(process, "end", EndEvent.class, bpmnModelInstance);

    // Тут запихиваю все в дефинишен который идет в xml вокруг всего
    bpmnModelInstance.setDefinitions(definitions);
    // Тут делаем стрелочки между элементами
    createSequenceFlow(process, startEvent, task1, bpmnModelInstance);
    createSequenceFlow(process, task1, endEvent, bpmnModelInstance);

    Bpmn.validateModel(bpmnModelInstance);
    ////////

    /*
    BpmnModelInstance modelInstance123 = Bpmn.createEmptyModel();
    Definitions definitions123 = (Definitions) modelInstance123.newInstance(Definitions.class);
    definitions123.setTargetNamespace("http://www.omg.org/spec/BPMN/20100524/MODEL");
    definitions123
        .getDomElement()
        .registerNamespace("camunda", "http://camunda.org/schema/1.0/bpmn");
    modelInstance123.setDefinitions(definitions123);
    Process process123 = (Process) modelInstance123.newInstance(Process.class);
    Collaboration collaboration123 =
        createEmptyCollaboration("My_collaboration_name", "My_collaboration_id", modelInstance123);
    Participant participant123 =
        createEmptyParticipant(
            "My_participant_name", "My_participant", "My_process_id", modelInstance123);
    collaboration123.addChildElement(participant123);
    definitions123.addChildElement(process123);
    definitions123.addChildElement(collaboration123);
    BpmnDiagram bpmnDiagram = (BpmnDiagram) modelInstance123.newInstance(BpmnDiagram.class);
    BpmnPlane bpmnPlane = (BpmnPlane) modelInstance123.newInstance(BpmnPlane.class);
    bpmnPlane.setBpmnElement(process123);
    BpmnPlane bpmnPlane2 = (BpmnPlane) modelInstance123.newInstance(BpmnPlane.class);
    bpmnPlane2.setBpmnElement(collaboration123);
    bpmnDiagram.addChildElement(bpmnPlane2);


    BpmnShape bpmnShape = modelInstance123.newInstance(BpmnShape.class);



    definitions123.addChildElement(bpmnDiagram);
    process123.builder();
    */

    return Bpmn.convertToString(bpmnModelInstance);
  }

  // Можно конечно поменять на дженерик,но мне кажется так удобнее тк понимаешь с чем идет дело
  private Collaboration createEmptyCollaboration(
      String name, String id, BpmnModelInstance instance) {
    Collaboration collaboration = instance.newInstance(Collaboration.class);
    collaboration.setName(name);
    collaboration.setId(id);
    return collaboration;
  }

  private Process createEmptyProcess(String name, String id, BpmnModelInstance instance) {
    Process process = instance.newInstance(Process.class);
    process.setName(name);
    process.setId(id);
    return process;
  }

  private Participant createEmptyParticipant(
      String name, String id, String processRef, BpmnModelInstance instance) {
    Participant participant = instance.newInstance(Participant.class);
    participant.setName(name);
    participant.setAttributeValue("processRef", processRef);
    participant.setId(id);
    return participant;
  }

  protected <T extends BpmnModelElementInstance> T createElement(
      BpmnModelElementInstance parentElement,
      String id,
      Class<T> elementClass,
      BpmnModelInstance instance) {
    T element = instance.newInstance(elementClass);
    element.setAttributeValue("id", id, true);
    parentElement.addChildElement(element);
    return element;
  }

  public SequenceFlow createSequenceFlow(
      Process process, FlowNode from, FlowNode to, BpmnModelInstance instance) {
    String identifier = from.getId() + "-" + to.getId();
    SequenceFlow sequenceFlow = createElement(process, identifier, SequenceFlow.class, instance);
    process.addChildElement(sequenceFlow);
    sequenceFlow.setSource(from);
    from.getOutgoing().add(sequenceFlow);
    sequenceFlow.setTarget(to);
    to.getIncoming().add(sequenceFlow);
    return sequenceFlow;
  }
}
