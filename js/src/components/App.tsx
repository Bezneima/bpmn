import React from 'react';
import { Provider } from 'mobx-react';
import RootStore from '../stores/RootStore';
import BPMNContainer from './BPMNContainer';

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
const App = ({ rootStore }: { rootStore: RootStore }) => (
  <Provider rootStore={rootStore}>
    <BPMNContainer />
  </Provider>
);

export default App;
