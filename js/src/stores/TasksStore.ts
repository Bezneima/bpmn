import { makeAutoObservable, observable } from 'mobx';
import { Task } from '../types/Task';

export default class TasksStore {
  @observable tasks = new Map<string, Task | undefined>();
  @observable cache = false;
  @observable currentTask?: Task;
  constructor() {
    makeAutoObservable(this);
  }

  getTaskByKey(taskKey: string): Task | undefined {
    return this.tasks.get(taskKey);
  }

  setTaskByKey(taskKey: string, task: Task): Map<string, Task | undefined> {
    return this.tasks.set(taskKey, task);
  }

  createEmptyTask(taskKey: string): Task {
    const task = this.createTaskObj(taskKey, false, '');
    this.tasks.set(taskKey, task);
    return task;
  }

  createTaskObj(taskKey: string, isOpened: boolean, BPMN: string): Task {
    return {
      key: taskKey,
      isOpened: isOpened,
      BPMN: BPMN,
    };
  }

  setCurrentTask(task: Task): void {
    this.currentTask = task;
  }

  getCurrentTask(): Task | undefined {
    return this.currentTask;
  }
}
