import React, {Component} from 'react';

export default class RulesButton extends Component{
    render() {
        return (
            <button onClick={() => this.props.showRules()} className="rulesButton">Rules</button>
        );
    }
}