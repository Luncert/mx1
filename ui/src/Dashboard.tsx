import React, { Component } from 'react';
import { Grid, List, Divider, Header, Container } from 'semantic-ui-react'
import { Line as LineChart} from 'react-chartjs-2';

import './Dashboard.css';

export default class Dashboard extends Component {

    render() {
        return (
            <div className='dashboard-container'>
                <List className='node-list' divided relaxed>
                    <List.Item className='node-list-item available-node selected-node'>
                        <List.Content>
                            <List.Header as='a'>probe-test-app</List.Header>
                            <List.Description>127.0.0.1:63201</List.Description>
                        </List.Content>
                    </List.Item>
                    <List.Item className='node-list-item unavailable-node'>
                        <List.Content>
                            <List.Header as='a'>probe-test-app</List.Header>
                            <List.Description>127.0.0.1:45720</List.Description>
                        </List.Content>
                    </List.Item>
                    <List.Item className='node-list-item unavailable-node'>
                        <List.Content>
                            <List.Header as='a'>probe-test-app</List.Header>
                            <List.Description>127.0.0.1:49238</List.Description>
                        </List.Content>
                    </List.Item>
                    <List.Item className='node-list-item unavailable-node'>
                        <List.Content>
                            <List.Header as='a'>probe-test-app</List.Header>
                            <List.Description>127.0.0.1:52900</List.Description>
                        </List.Content>
                    </List.Item>
                </List>
                <div className='node-detail'>
                    <div className='node-dynamic-info'>
                        <div className='node-dynamic-info-item'>
                            <UsageChart chartName='load average' data={[-1, -1, -1, -1, -1, -1, -1]} />
                            <UsageChart chartName='cpu usage' borderColor='rgb(26, 120, 197)'
                                data={[65, 59, 80, 81, 56, 55, 40]} />
                            <UsageChart chartName='mem usage' data={[65, 59, 80, 81, 56, 55, 40]} />
                            <UsageChart chartName='swap usage' data={[65, 59, 80, 81, 56, 55, 40]} />
                        </div>
                        <div className='node-dynamic-info-item'>
                            <JvmMemInfoChart />
                            <ThreadInfoChart />
                        </div>
                    </div>
                    <NodeMetadata />
                </div>
            </div>
        )
    }
}

class DynamicJvmInfo extends Component {

    render() {
        return (
            <div className='node-dynamic-info-chart'>
                loadedClassCount
                totalLoadedClassCount
            </div>
        )
    }
}

class JvmMemInfoChart extends Component {
    render() {
        return (
            <div className='node-dynamic-info-chart'>
                <UsageChart chartName='heap mem usage' data={[-1, -1, -1, -1, -1, -1, -1]} />
                <UsageChart chartName='nonheap mem usage' data={[-1, -1, -1, -1, -1, -1, -1]} />
                heap mem usage
                maxNonHeapMemory
                nonheap mem usage
                maxHeapMemory
            </div>
        )
    }
}

class ThreadInfoChart extends Component {

    render () {
        const data = {
          labels: ['', '', '', '', '', '', '', '', '', ''],
          datasets: [
            {
                label: 'activeThreadCount',
                backgroundColor: 'rgba(0,0,0,0)',
                borderColor: '',
                borderWidth: 1,
                pointStyle: 'circle',
                pointBackgroundColor: '',
                data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            },
            {
                label: 'daemonThreadCount',
                backgroundColor: 'rgba(0,0,0,0)',
                borderColor: '',
                borderWidth: 1,
                pointStyle: 'circle',
                pointBackgroundColor: '',
                data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            },
            {
                label: 'totalStartedThreadCount',
                backgroundColor: 'rgba(0,0,0,0)',
                borderColor: '',
                borderWidth: 1,
                pointStyle: 'circle',
                pointBackgroundColor: '',
                data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            },
            {
                label: 'deadlockedThreadCount',
                backgroundColor: 'rgba(0,0,0,0)',
                borderColor: '',
                borderWidth: 1,
                pointStyle: 'circle',
                pointBackgroundColor: '',
                data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            },
            {
                label: 'monitorDeadlockedThreadCount',
                backgroundColor: 'rgba(0,0,0,0)',
                borderColor: '',
                borderWidth: 1,
                pointStyle: 'circle',
                pointBackgroundColor: '',
                data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            }
        ],
        }
        return (
            <div className='node-dynamic-info-chart'>
                <LineChart data={data}/>
            </div>
        )
    }
}

interface UsageChartProps {
    chartName: string
    borderColor?: string
    data: number[]
}

class UsageChart extends Component<UsageChartProps> {

    render() {
        const data = {
          labels: ['', '', '', '', '', '', '', '', '', ''],
          datasets: [{
            label: this.props.chartName + '(%)',
            backgroundColor: 'rgba(0,0,0,0)',
            borderColor: this.props.borderColor,
            borderWidth: 1,
            pointStyle: 'circle',
            pointBackgroundColor: this.props.borderColor,
            data: this.props.data,
          }],
        }
        const options = {
            //Boolean - If we should show the scale at all
            showScale: false,
            //Boolean - whether to make the chart responsive to window resizing
            responsive: true,
        }
        return (
            <div className='node-dynamic-info-chart'>
                <LineChart data={data} options={options}/>
            </div>
        )
    }
}

class NodeMetadata extends Component {

    render() {
        return (
            <div className='node-metadata'>
                <div className='node-metadata-item'>
                    <span className='title'>System Information</span>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>os name</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>os arch</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>os version</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>environment</span>
                        <span className='field-value'></span>
                    </div>
                </div>
                <div className='node-metadata-item'>
                    <span className='title'>Java Virtual Machine Information</span>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>java version</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>java vendor</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>vm specification version</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>vm specification vendor</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>vm specification name</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>vm version</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>vm vendor</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>vm name</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>java class version</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>java compiler</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>setup arguments</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>total memory</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>max memory</span>
                        <span className='field-value'></span>
                    </div>
                    <div className='node-metadata-item-field'>
                        <span className='field-name'>compiler name</span>
                        <span className='field-value'>JIT</span>
                    </div>
                </div>
                <div className='node-metadata-item'>
                    <span className='title'>Maven Information</span>
                    <div>unable to load maven information</div>
                </div>
            </div>
        )
    }
}