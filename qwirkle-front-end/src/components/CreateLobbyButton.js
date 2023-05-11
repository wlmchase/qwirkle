import React, {Component} from 'react';

export default class CreateLobbyButton extends Component{
    render() {
        return (
            <button onClick={() => this.props.createLobby()} className="createLobbyButton">Create Lobby</button>
        );
    }
}