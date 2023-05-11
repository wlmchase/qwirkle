import React, {Component} from 'react';

class DisconnectPrompt extends Component{

    render() {
        return (
            <div className='disconnectPromptContainer'>
                <p className='disconnectText'>Are you sure you want to leave the game?</p>
                <button className='disconnectLeaveButton' onClick={() => {this.props.disconnect()}}>Leave Game</button>
                <button className='disconnectStayButton' onClick={() => {this.props.backToGame()}}>Stay</button>
            </div>
        );
    }
}
export default DisconnectPrompt