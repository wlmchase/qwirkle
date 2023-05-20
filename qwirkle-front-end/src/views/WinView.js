import React, {Component} from 'react';

class WaitView extends Component {
    state={}

    render() {
        let winner;
        if (this.props.state.winner.playerId === this.props.state.myPlayerId){
            winner = <h1 className='winText'>You Win!</h1>
        }
        else{
            winner = <h1 className='winText'>Player {this.props.state.winner.playerNum + 1} wins!</h1>
        }

        return (
            <div>
                {winner}
                <button className='endGameButton'>End Game</button>
            </div>
        );
    }
}
export default WaitView