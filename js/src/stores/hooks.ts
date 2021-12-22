import React from 'react';
import { MobXProviderContext } from 'mobx-react';
import RootStore from './RootStore';
import TasksStore from './TasksStore';
import ContainerStore from './ContainerStore';

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
export function useStores() {
  return React.useContext(MobXProviderContext);
}

export function useRootStore(): RootStore {
  const { rootStore } = useStores();
  return rootStore;
}

//тут хранится вся информация о тасках их ключ,бпмн...
export function useTasksStore(): TasksStore {
  const rootStore = useRootStore();
  return rootStore.tasksStore;
}
//Тут хранится вся информация о блоке в котором мы лежим сейчас, пока только загружаемость и полный экран
export function useContainerStore(): ContainerStore {
  const rootStore = useRootStore();
  return rootStore.containerStore;
}
