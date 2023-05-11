import React, {Component} from 'react';
import { DragDropContainer } from "react-drag-drop-container";

class TileHand extends Component{

    render() {
        
        let tiles = [];
        console.log("hand length: " + this.props.state.myPlayer.hand.length)
        for (let i = 0; i < this.props.state.myPlayer.hand.length; i++){ 
            let tileId = this.props.state.myPlayer.hand[i].color + "_" + this.props.state.myPlayer.hand[i].shape
            
            if(this.props.state.currentPlayer.stringId === this.props.state.myPlayer.stringId){
                tiles.push(
                    <DragDropContainer 
                        className='draggableTile' 
                        targetKey="placedTile" 
                        dragData={
                            {     
                                shape: this.props.state.myPlayer.hand[i].shape, 
                                color: this.props.state.myPlayer.hand[i].color
                            } 
                        }
                        key={tileId + "_" + i}> 
                        <img className='tileInHand' src={`/tile_images/${tileId}.png`} key={tileId} alt='yellow cross'/>
                    </DragDropContainer>
                )
            }
            else{
                console.log("TILE ID!!!!!!!!!!!! " + tileId);
                tiles.push(
                    <img className='tileInHand' src={`/tile_images/${tileId}.png`} key={tileId + "_" + i} alt='yellow cross'/>
                )
            }

        }

        return (
            
            <div className='tileHand'>
                {tiles}
            </div>
        );
    }
}
export default TileHand