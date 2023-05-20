import React, {Component} from 'react';

class WinView extends Component {
    render() {

        let winElement;

        if (this.props.state.winner.playerId === this.props.state.myPlayerId){
            winElement = <h1 className='winText'>You Win!</h1>
        }
        else{
            winElement = <h1 className='winText'>Player {this.props.state.winner.playerNum + 1} wins!</h1>
        }

        return (
            <div>
                {winElement}
                <button className='endGameButton' onClick={() => this.props.backToStart()}>End Game</button>
            </div>
        );
    }
}
export default WinView