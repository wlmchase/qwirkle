import React, {Component} from 'react';

export default class DisconnectButton extends Component{
    render() {
        return (
            <button onClick={() => this.props.showDisconnectPrompt()} className="disconnectButton">Disconnect</button>
        );
    }
}