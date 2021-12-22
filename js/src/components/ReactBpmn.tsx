import React, { RefObject } from 'react';
import { BpmnProps, BpmnState } from '../types/BpmnProps';
/* eslint-disable */ //Todo незнаю как подключить правильно
const BpmnJS = require('bpmn-js');

export default class ReactBpmn extends React.Component<BpmnProps, BpmnState> {
  containerRef: RefObject<any>;
  bpmnViewer: any;

  constructor(props: BpmnProps) {
    super(props);
    this.state = {};
    this.containerRef = React.createRef();
  }

  componentDidMount() {
    const { url, diagramXML } = this.props;
    const container = this.containerRef.current;
    //Todo из-за того что подключаю модуль нужно писать default
    this.bpmnViewer = new BpmnJS.default({ container });
    this.bpmnViewer.on('import.done', (event: any) => {
      const { error, warnings } = event;
      if (error) {
        return this.handleError(error);
      }
      this.bpmnViewer.get('canvas').zoom('fit-viewport');
      return this.handleShown(warnings);
    });
    if (url) {
      return this.fetchDiagram(url);
    }
    if (diagramXML) {
      return this.displayDiagram(diagramXML);
    }
  }

  componentWillUnmount() {
    this.bpmnViewer.destroy();
  }

  componentDidUpdate(prevProps: any, prevState: any) {
    const { props, state } = this;

    if (props.url !== prevProps.url) {
      return this.fetchDiagram(props.url);
    }

    const currentXML = props.diagramXML || state.diagramXML;

    const previousXML = prevProps.diagramXML || prevState.diagramXML;

    if (currentXML && currentXML !== previousXML) {
      return this.displayDiagram(currentXML);
    }
  }

  displayDiagram(diagramXML: any) {
    this.bpmnViewer.importXML(diagramXML);
  }

  fetchDiagram(url: any) {
    this.handleLoading();
    fetch(url)
      .then((response) => response.text())
      .then((text) => this.setState({ diagramXML: text }))
      .catch((err) => this.handleError(err));
  }

  handleLoading() {
    const { onLoading } = this.props;
    if (onLoading) {
      onLoading();
    }
  }

  handleError(err: any) {
    const { onError } = this.props;

    if (onError) {
      onError(err);
    }
  }

  handleShown(warnings: any) {
    const { onShown } = this.props;
    if (onShown) {
      onShown(warnings);
    }
  }

  render() {
    return <div className="react-bpmn-diagram-container" ref={this.containerRef} />;
  }
}
