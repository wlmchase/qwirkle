import React, {Component} from 'react';
import NewlineText from '../components/NewlineText';

class WaitRoomView extends Component{
    state={
    }


    render() {
        console.log("in WaitRoomView");
        console.log(this.props.state.waitRoom.waitingPlayerList.length);

        let roomSize = this.props.state.waitRoom.size
        let waitingPlayers = this.props.state.waitRoom.waitingPlayerList;
        let waitingPlayerSquares = [];

        for (let i = 0; i < waitingPlayers.length; i++){
            let className = 'waitingPlayerSquare' + (i+1);
            if (waitingPlayers[i].vote){
                waitingPlayerSquares.push(
                    <div className={className} key={i}>
                        <NewlineText text={`P${i+1}\n✅`}/>
                    </div>
                );
            }
            else{
                waitingPlayerSquares.push(
                    <div className={className} key={i}>
                        P{i+1}
                    </div>
                );
            }
        }

        let size = waitingPlayerSquares.length;

        for (let i = 0; i < roomSize-size; i++){
            waitingPlayerSquares.push(<div className='waitingPlayerSquareEmpty' key={i+10}></div>)
        }

        return (
            <React.Fragment>
                <div>
                    <button className='voteToStartButton' onClick={() => {this.props.voteToStart()}}>
                        <p className='voteToStartText'>Vote To Start</p>
                    </button>
                    <button className='waitRoomBackButton' onClick={() => {this.props.goBack()}}>
                        <p>Back</p>
                    </button>
                </div>
                <div className='gameCodeDisplay'>
                    <p className='gameCodeText'>Game Code: {this.props.gameCode}</p>
                </div>
                <div className='waitingSquaresContainer'>
                    {waitingPlayerSquares}
                </div>
            </React.Fragment>
        );
    }
}
export default WaitRoomView