import React, {Component} from 'react';

export default class TurnIndicator extends Component{
    render() {
        let playerSquares = [];
        let map = new Map();
        map.set(1, "player1TurnIndicator");
        map.set(2, "player2TurnIndicator");
        map.set(3, "player3TurnIndicator");
        map.set(4, "player4TurnIndicator");

        for (let i = 0; i < this.props.state.playerList.length; i++){
            let classNameMapped = map.get(i+1);
            if ( i+1 === this.props.state.currentPlayer.playerNum){
                classNameMapped = classNameMapped + "Active"
            }
            playerSquares.push(
                <div className={classNameMapped}>
                    <p className='turnIndicatorText'>Player {i+1}</p>
                    <p className='scoreText'>Score: {this.props.state.playerList[i].score}</p>
                </div>
            )
        }

        return (
            <div className='turnIndicatorContainer'>{playerSquares}</div>
        );
    }
}