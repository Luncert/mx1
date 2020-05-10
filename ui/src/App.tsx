import React, { Component } from 'react';

import 'semantic-ui-css/semantic.min.css?global';

import './App.css';
import Dashboard from './Dashboard';

interface AppState {
    navIdx: number
}

export default class App extends Component<any, AppState> {
    constructor(props: any) {
        super(props)
        this.state = {
            navIdx: 0
        }
    }

    navTo(idx: number) {
        this.setState({navIdx: idx})
    }

    render() {
        const { navIdx } = this.state

        return (
            <div className='app'>
                <div className='app-nav'>
                    {/* <span style={{float: 'left'}}>mx1 icon</span> */}
                    <div className={'app-nav-item ' + (navIdx == 0 ? 'app-nav-item-active' : '')}
                         onClick={() => this.navTo(0)}>Dashboard</div>
                    <div className={'app-nav-item ' + (navIdx == 1 ? 'app-nav-item-active' : '')}
                        onClick={() => this.navTo(1)}>Settings</div>
                    {/* <span>login icon</span> */}
                </div>
                <div className='app-content'>
                    <Dashboard />
                </div>
            </div>
        )
    }
}
