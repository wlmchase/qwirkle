import React, {Component} from 'react';
import { DropTarget } from "react-drag-drop-container";

class GameBoard extends Component{


    render(){
        let board = this.props.state.board;

        let boardSquares = [];
        for (let i = 0; i < board.length; i++){
            for (let j = 0; j < board[i].length; j++){
                if (board[i][j] !== null){
                    boardSquares.push(
                        <img className='squareWithTile' src={`/tile_images/${board[i][j].color + "_" + board[i][j].shape}.png`}/>
                    ); 
                }
                else{
                    boardSquares.push(
                        <DropTarget 
                            targetKey="placedTile" 
                            id="a_square" 
                            onHit={(event) => {
                                console.log(JSON.stringify(event.dragData))
                                this.props.placeTile(i, j, event.dragData) // on drop, call placeTile in FEGameStateController
                            }}
                        >
                            <div className ='emptySquare' key={Math.random()}></div>
                        </DropTarget>
                    ); 
                }
            }
        }
        return(
            this.props.state.discarding?
            <div className='garbageCanContainer'>
                {console.log("MAKING DROP TARGET")}
                <DropTarget 
                    targetKey="placedTile" 
                    id="a_square" 
                    onHit={(event) => {
                        console.log(JSON.stringify(event.dragData))
                        this.props.addToDiscardPile(event.dragData) // on drop, call placeTile in FEGameStateController
                    }}
                >
                    <img 
                        className='garbageCan'
                        src='/game_images/garbage.png'
                        alt='garbage can'
                    />
                </DropTarget>
            </div>:
            <div className='gameBoard'>
                {boardSquares}
            </div>
        );
    }
}

export default GameBoard


// "board":[[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]]