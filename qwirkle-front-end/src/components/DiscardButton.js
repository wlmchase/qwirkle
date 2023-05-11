import React, {Component} from 'react';

export default class DiscardButton extends Component{
    render() {
        return (
            this.props.state.discarding && this.props.state.discardPile.length === 0? // if user hasn't actually discarded anything yet let them go back
            <button onClick={() => this.props.toggleDiscardState()} className="discardButton">Back</button>:
            this.props.state.discarding && this.props.state.discardPile.length > 0?
            <div></div>:
            <button onClick={() => this.props.toggleDiscardState()} className="discardButton">Discard Tiles</button>
        );
    }
}