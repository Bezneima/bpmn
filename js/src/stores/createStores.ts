import RootStore from './RootStore';
import TasksStore from './TasksStore';
import ContainerStore from './ContainerStore';

export function createRootStore(): RootStore {
  const tasksStore = new TasksStore();
  const containerStore = new ContainerStore();
  return new RootStore(tasksStore, containerStore);
}
