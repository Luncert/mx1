import React, { Component } from 'react';
import { Grid } from 'semantic-ui-react'

import './Dashboard.css';
import Chart from './Chart';

export default class Dashboard extends Component {

    render() {
        return (
            <Grid style={{height: '100%', width: '100%'}}>
                <Grid.Column width={4}>
                    app base info
                </Grid.Column>
                <Grid.Column width={12}>
                    charts
                </Grid.Column>
                <Chart />
            </Grid>
        )
    }
}