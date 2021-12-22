import React from 'react';
import ReactDOM from 'react-dom';
import { configure } from 'mobx';
import Events from 'jira/util/events';
import EventTypes from 'jira/util/events/types';
import App from './components/App';
import { createRootStore } from './stores/createStores';

export function init(): void {
  configure({ enforceActions: 'observed', isolateGlobalState: true });
  const rootStore = createRootStore();
  const listener = (event: Event, context: JQuery) => {
    try {
      let $context: JQuery<Element | Document>;
      if (event.type === EventTypes.ISSUE_REFRESHED) {
        $context = $(document);
      } else {
        $context = context;
      }
      if ($context.length === 0) return;
      $context
        .find('#BPMN-generator-panel-container')
        .get()
        .forEach((container: Element) => {
          const curContext = $context[0];
          if (curContext instanceof Element) {
            const affected = $.contains(curContext, container);
            if (!affected) {
              console.debug('Found out that panel container is not affected => no render occurred');
              return;
            }
          }
          if (container == null) {
            console.error('BPMN container not found');
            return;
          }
          ReactDOM.render(<App rootStore={rootStore} />, container);
        });
    } catch (e) {
      console.error('BPMN has error occured in events listener', e);
    }
  };

  Events.bind(EventTypes.NEW_CONTENT_ADDED, listener);
  Events.bind(EventTypes.ISSUE_REFRESHED, listener);
}
