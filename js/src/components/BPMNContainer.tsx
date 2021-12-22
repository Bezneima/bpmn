import React, { useEffect } from 'react';
import { inject, observer } from 'mobx-react';
import * as JIRA from 'JIRA';
import { useContainerStore, useTasksStore } from '../stores/hooks';
import TaskContainer from './TaskContainer';

const BPMNContainer = observer(() => {
  const tasksStore = useTasksStore();
  //const containerStore = useContainerStore();

  useEffect(() => {
    const issueKey: string = JIRA.Issue.getIssueKey();
    tasksStore.currentTask = tasksStore.getTaskByKey(issueKey);
    if (!tasksStore.currentTask) {
      tasksStore.setCurrentTask(tasksStore.createEmptyTask(issueKey));
    }
  });

  return <TaskContainer />;
});

export default inject('rootStore')(BPMNContainer);
