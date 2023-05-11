import React, {Component} from 'react';

export default class JoinByCodeButton extends Component{
    render() {
        return (
            <button onClick={() => this.props.showJoinByCodeScreen()} className="joinByCodeButton">Join by Code</button>
        );
    }
}