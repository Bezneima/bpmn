import { makeAutoObservable, observable } from 'mobx';

export default class ContainerStore {
  @observable isLoading = true;
  @observable isFullSize = false;
  @observable header?: Element;

  constructor() {
    makeAutoObservable(this);
  }

  setLoading(isLoading: boolean): void {
    this.isLoading = isLoading;
  }

  getLoading(): boolean {
    return this.isLoading;
  }

  setHeader(header: Element) {
    this.header = header;
  }
}
