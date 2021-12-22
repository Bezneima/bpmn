import TasksStore from './TasksStore';
import ContainerStore from './ContainerStore';

export default class RootStore {
  tasksStore: TasksStore;
  containerStore: ContainerStore;
  constructor(panelStore: TasksStore, containerStore: ContainerStore) {
    this.tasksStore = panelStore;
    this.containerStore = containerStore;
  }
}
