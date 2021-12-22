/* (C)2021 */
package ru.mail.jira.plugins.bpmn.camunda;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestParticipant {

  @Autowired
  public TestParticipant() {}

  public String createOnlyParticipantShape() {
    BpmnModelInstance modelInstance =
        Bpmn.createEmptyModel(); // создал тут пустую модель в браузере это {}

    Collaboration collaboration =
        createEmptyCollaboration("My_collaboration_name", "My_collaboration_id", modelInstance);
    Process process = createEmptyProcess("My_process_name", "My_process_id", modelInstance);
    Participant participant =
        createEmptyParticipant(
            "My_participant_name", "My_participant", "My_process_id", modelInstance);

    collaboration.addChildElement(participant);
    Definitions definitions = modelInstance.newInstance(Definitions.class);
    definitions.setTargetNamespace("http://camunda.org/examples");
    definitions.addChildElement(collaboration);
    definitions.addChildElement(process);
    modelInstance.setDefinitions(definitions);
    // Выше модель
    // Ниже диаграма

    BpmnPlane bpmnPlane = modelInstance.newInstance(BpmnPlane.class);
    bpmnPlane.setAttributeValue("bpmnElement", "My_collaboration_id");
    bpmnPlane.setBpmnElement(participant);

    BpmnDiagram bpmnDiagram = modelInstance.newInstance(BpmnDiagram.class);
    bpmnDiagram.addChildElement(bpmnPlane);

    definitions.addChildElement(bpmnDiagram);
    Bpmn.validateModel(modelInstance);

    return Bpmn.convertToString(modelInstance);
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
