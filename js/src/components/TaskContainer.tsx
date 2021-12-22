import { observer } from 'mobx-react';
import { I18n } from '@atlassian/wrm-react-i18n';
import React, { useEffect } from 'react';
import { useTasksStore } from '../stores/hooks';
import styled from 'styled-components';
import ReactBpmn from './ReactBpmn';

const BPMNWrapper = styled.div``;

const TaskContainer = observer(() => {
  const tasksStore = useTasksStore();

  return tasksStore.currentTask ? (
    <>
      <ReactBpmn
        url="https://cdn.staticaly.com/gh/bpmn-io/bpmn-js-examples/dfceecba/starter/diagram.bpmn" //TODO убрать тестовый
        onShown={() => {
          console.log(1);
        }}
        onLoading={() => {
          console.log(2);
        }}
        onError={() => {
          console.log(3);
        }}
      />
      <BPMNWrapper>{I18n.getText('ru.mail.jira.plugins.bpmn.text') + ' ' + tasksStore.currentTask.key}</BPMNWrapper>
    </>
  ) : (
    <>{I18n.getText('ru.mail.jira.plugins.bpmn.cant.find.task')}</>
  );
});

export default TaskContainer;
