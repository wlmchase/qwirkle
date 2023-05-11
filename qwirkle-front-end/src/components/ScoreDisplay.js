import React, {Component} from 'react';

export default class ScoreDisplay extends Component{
    render() {

        let scores = [];
        for (let i = 0; i < this.props.state.playerList.length; i++){
            console.log("PLAYER " + i+1 + " score: " + this.props.state.playerList[i].score)
            
            if (this.props.state.myPlayer.playerNum === i+1){
                scores.push(
                    <p className='myPlayerScoreText'>Player {i+1}: {this.props.state.playerList[i].score}</p>
                )
            }
            else{
                scores.push(
                    <p className='scoreText'>Player {i+1}: {this.props.state.playerList[i].score}</p>
                )
            }
        }
        
        return (
            <div className='scoreDisplay'>
                <p className='scoresTitle'>Scores</p>
                {scores}
            </div>
        );
    }
}