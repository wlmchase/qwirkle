import React, {Component} from 'react';
import GameBoard from '../components/GameBoard';
import TileHand from '../components/TileHand';
import EndTurnButton from '../components/EndTurnButton';
import TurnIndicator from '../components/TurnIndicator';
import DiscardButton from '../components/DiscardButton';
import DisconnectButton from '../components/DisconnectButton';

class GameView extends Component{

    render() {
        let playerLabel = <h1 className='playerLabel'>Player {this.props.state.myPlayer.playerNum}</h1>
        let trapezoidClass = "player" + this.props.state.myPlayer.playerNum + "Trapezoid"
        let turn = <div></div>

        if ( this.props.state.myPlayer.playerNum === this.props.state.currentPlayer.playerNum){
            turn = <p className='myTurnText'>It's your turn!</p>
        }
    

        return (
            <React.Fragment>
                <div className={trapezoidClass}>
                    {playerLabel}
                    <div className='br'></div>
                    {turn}
                </div>
                <div className='gameViewContainer'>
                    <TurnIndicator state={this.props.state}/>
                    {console.log("MY PLAYER ID: " + this.props.state.myPlayer.stringId)}
                    {console.log("CURRENT PLAYER ID: " + this.props.state.currentPlayer.stringId)}
                    <div className='turnDisplay'>
                        <GameBoard state={this.props.state} addToDiscardPile={(tile) => {this.props.addToDiscardPile(tile)}} placeTile={(row, col, tile) => {this.props.placeTile(row, col, tile)}}/>
                        <TileHand state={this.props.state}/>
                    </div>
                    <div className='rightContainer'>
                        {/* <ScoreDisplay state={this.props.state}/> */}
                        <div className='gameButtons'>
                            <DiscardButton state={this.props.state} toggleDiscardState={() => {this.props.toggleDiscardState()}}/>
                            <EndTurnButton endTurn={() => {this.props.endTurn()}}/>
                            <DisconnectButton showDisconnectPrompt={() => {this.props.showDisconnectPrompt()}}/>
                        </div>
                    </div>
                </div>
            </React.Fragment>
        );
    }
}
export default GameView