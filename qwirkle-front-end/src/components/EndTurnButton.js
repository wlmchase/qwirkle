import React, {Component} from 'react';

export default class EndTurnButton extends Component{
    render() {
        return (
            <button onClick={() => this.props.endTurn()} className="endTurnButton">End Turn</button>
        );
    }
}