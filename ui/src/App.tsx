import React, { Component } from 'react';

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
        return (
            <div className='app'>
                <div className='nav'>
                    {/* <span style={{float: 'left'}}>mx1 icon</span> */}
                    <div className='navItem' onClick={() => this.navTo(0)}>Dashboard</div>
                    <div className='navItem' onClick={() => this.navTo(1)}>Settings</div>
                    {/* <span>login icon</span> */}
                </div>
                <div className='content'>
                    <Dashboard />
                </div>
            </div>
        )
    }
}
