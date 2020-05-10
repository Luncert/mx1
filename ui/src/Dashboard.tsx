import React, { Component } from 'react';
import { Grid, List, Divider, Header, Container } from 'semantic-ui-react'

import './Dashboard.css';
import Chart from './Chart';

export default class Dashboard extends Component {

    render() {
        return (
            <div className='dashboard-container'>
                <List className='node-list' divided relaxed animated>
                    <List.Item className='node-list-item available-node selected-node'>
                        <List.Content>
                            <List.Header as='a'>probe-test-app</List.Header>
                            <List.Description>127.0.0.1:63201</List.Description>
                        </List.Content>
                    </List.Item>
                    <List.Item className='node-list-item unavailable-node'>
                        <List.Content>
                            <List.Header as='a'>probe-test-app</List.Header>
                            <List.Description>127.0.0.1:63201</List.Description>
                        </List.Content>
                    </List.Item>
                </List>
                <div className='node-detail'>
                    <div className='node-dynamic-info'>
                        <Chart />
                    </div>
                    {/* <Divider vertical/> */}
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
                            <div className='node-metadata-item-field'>
                                <span className='field-name'></span>
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
                                <span className='field-value'></span>
                            </div>
                        </div>
                        <div className='node-metadata-item'>
                            <span className='title'>Maven Information</span>
                            <div>unable to load maven information</div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
