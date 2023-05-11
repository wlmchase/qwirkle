import React, {Component} from 'react';

export default class QuickJoinButton extends Component{
    render() {
        return (
            <button onClick={() => this.props.quickJoin()} className="quickJoinButton">Quick Join</button>
        );
    }
}