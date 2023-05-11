import React, {Component} from 'react';
import {Client} from '@stomp/stompjs';
import GameView from '../views/GameView';
import { Alert } from '@mui/material';
import WaitRoomView from '../views/WaitRoomView';
import WaitView from '../views/WaitView';
import DisconnectPrompt from '../views/DisconnectPrompt';

const SOCKET_URL = 'ws://localhost:8080/ws-message';

class WebSocketController extends Component{

    state = {
        gameStarted: false,
        showRules: false,
        readyToStart: false,
        tilesPlaced: false,
        myPlayerNum: 1,
        currScreen: "start",
        squaresWithTiles: []
    };


    sendPostRequest = (endpoint, body) => {
        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
            },
            body
        };
        console.log("request body: " + JSON.stringify(body));
        fetch(`http://localhost:8080${endpoint}`, requestOptions)
            .then(res => {
                if (res.status === 400){
                    res.json().then(parsed => {console.log(parsed)})
                    // console.log(res);
                }
            })
    }

    startGame = () => {
        this.setState({gameStarted: true});
    }

    
    addToDiscardPile = (tile) => {
        console.log("ADDING TO DISCARD PILE");
        console.log("this.state.discarding: " + this.state.discarding);
        console.log("tile: " + JSON.stringify(tile));


        let index = this.state.myPlayer.hand.findIndex(obj => (obj.color === tile.color && obj.shape === tile.shape));
        console.log("INDEX: " + index);
        if (index !== -1) {
            this.state.myPlayer.hand.splice(index, 1);
        }
        
        this.setState((prevState) => {
            return{
                myPlayer: {
                    ...prevState.myPlayer,
                    hand: this.state.myPlayer.hand
                },
                discardPile: [...prevState.discardPile, tile]
            }
        }, () => {
            console.log("HAND: " + JSON.stringify(this.state.myPlayer.hand));
        });
    }


    endTurn = () => {

        if (this.state.discarding){
            let body = {
                playerId: this.props.myPlayerId,
                tiles: this.state.discardPile
            };
            const requestOptions = {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(body)
            };
            console.log("request body: " + JSON.stringify(body));
            fetch("http://localhost:8080/discardTiles?gameCode=" + this.props.gameCode, requestOptions)
                .then(res => {
                    if (res.status === 400){
                        res.json().then(parsed => {console.log(parsed)})
                    }
                })
                .then(() => {
                    body = {
                        playerId: this.props.myPlayerId
                    }
                    this.sendPostRequest("/endTurn?gameCode=" + this.props.gameCode, JSON.stringify(body));
                })
        }
        else{
            let body = {
                playerId: this.props.myPlayerId
            }
            this.sendPostRequest("/endTurn?gameCode=" + this.props.gameCode, JSON.stringify(body));
        }
        this.setState({tilesPlaced: false});
        
    }



    setGameState = (resBody) => {
        console.log("/game/gameState sent: " + JSON.stringify(resBody));
        resBody.playerList.map((player,i) => {
            player.playerNum = i+1;
            if (player.stringId === resBody.currentPlayer.stringId){
                resBody.currentPlayer.playerNum = player.playerNum;
            }
            return true;
        });
        const myPlayer = resBody.playerList.find(thisPlayer => thisPlayer.stringId === this.props.myPlayerId); // set myPlayer in state so FE can display player specific info
        myPlayer.originalHand = myPlayer.hand;
        this.setState((prevState) => {
            return {
                ...prevState,
                playerList: resBody.playerList,
                currentPlayer: resBody.currentPlayer,
                myPlayer: myPlayer,
                board: resBody.board,
                discarding: false,
                discardPile: [],
                gameStarted: resBody.gameStarted
            }
        }, () => {
            console.log("myPlayer: " + JSON.stringify(this.state.myPlayer));
            // ********* trying to do this this.state.myPlayer.originalHand = this.state.myPlayer.hand;
            // this.setState( (prevState) => {
            //     return{
            //         myPlayer: {
            //             ...prevState.myPlayer,
            //             orignalHand: prevState.myPlayer.hand
            //         }
            //     }
            // })
        });
    }



    placeTile = (row, col, tile) => {
        this.setState(prevState => {
            return{
                squaresWithTiles: [...prevState.squaresWithTiles, {
                    row: row,
                    col: col,
                    tile: tile
                }]
            }
        })

        // let row_position = (position - position%15)/15;
        // let column_position = position%15; 

        this.setState({tilesPlaced: true}, () => {
            let body = {
                playerId: this.props.myPlayerId,
                row_position: row,
                column_position: col,
                shape:tile.shape,
                color: tile.color
            }
    
            this.sendPostRequest("/playTile?gameCode=" + this.props.gameCode, JSON.stringify(body))
        });


        //this.state.myPlayer.hand = this.state.myPlayer.hand.filter(thisTile => (thisTile.shape !== tile.shape) || (thisTile.color !== tile.color)) // remove tile from player's hand to create snap to grid effect
    }

    toggleDiscardState = () => {
        console.log("TILES PLACED" + JSON.stringify(this.state.tilesPlaced))
        if (!this.state.discarding){
            if (!this.state.tilesPlaced){
                this.setState({discarding: true});
            }
            else{
                alert("Cannot discard after playing tiles!");
                <Alert severity="error">Cannot discard after playing tiles!</Alert>
            }
        }
        else{
            if (this.state.discardPile.length === 0){
                this.setState({discarding: false});
            }
        }
    }

    toggleTilesPlaced = () => {
        if (this.state.tilesPlaced){
            this.setState({tilesPlaced: false});
        }
        else if (!this.state.tilesPlaced){
            this.setState({tilesPlaced: true});
        }
    }

    voteToStart = () => {
        this.setState({votedToStart: true}, () => {
            let body = {
                playerId: this.props.myPlayerId
            };
            this.sendPostRequest("/voteToStart?gameCode=" + this.props.gameCode, JSON.stringify(body))
        })
    }

    showDisconnectPrompt = () => {
        this.setState({showDisconnectPrompt: true});
    }

    disconnect = () => {
        let body = {
            playerId: this.props.myPlayerId
        }
        this.props.backToStart();
        this.sendPostRequest("/disconnect?gameCode=" + this.props.gameCode, JSON.stringify(body));
    }

    backToGame = () => {
        this.setState({showDisconnectPrompt: false});
    }

    // WebSocket setup 
    componentDidMount(){
        let onConnected = () => {
            console.log("connected");
            console.log("/games/gameState/" + this.props.gameCode)
            client.subscribe("/games/gameState/" + this.props.gameCode, (res) => {
                if (res.body && this.props.myPlayerId){
                    console.log("res.body: " + JSON.parse(res.body));
                    this.setGameState(JSON.parse(res.body));
                }
                else{
                    console.log("No response");
                }
            });

            console.log("/games/waitingRoom/" + this.props.gameCode)
            client.subscribe("/games/waitingRoom/" + this.props.gameCode, (res) => {
                console.log("waiting room recieved")
                if (res.body && this.props.myPlayerId){
                    let body = JSON.parse(res.body);
                    console.log("wait room body: " + JSON.stringify(body));
                    this.setState({waitRoom: body});
                }
            })
        }

        let onDisconnected = () => {
            console.log("disconnected");
        }

        const client = new Client({
            brokerURL: SOCKET_URL,
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: onConnected,
            onDisconnect: onDisconnected
        });
        client.activate();
    }

    render() {

        console.log("My ID: " + this.props.myPlayerId);
        console.log("My gameCode: " + this.props.gameCode);

        return (
            this.state.showDisconnectPrompt?
            <div className='App'>
                <DisconnectPrompt backToGame={this.backToGame} disconnect={this.disconnect}/>
            </div>:
            this.state.gameStarted?
            <div className='App'>
                <GameView state={this.state} toggleDiscardState={this.toggleDiscardState} addToDiscardPile={this.addToDiscardPile} endTurn={this.endTurn} placeTile={this.placeTile} showDisconnectPrompt={this.showDisconnectPrompt}/>
            </div>:
            this.state.waitRoom && this.props.quickJoin?
            <div className='App'>
                <WaitView/>
            </div>:
            this.state.waitRoom?
            <div className='App'>
                <WaitRoomView state={this.state} gameCode={this.props.gameCode} myPlayerId={this.props.myPlayerId} voteToStart={this.voteToStart} goBack={() => {this.props.goBack()}}/>
            </div>:
            <div className='App'></div>
        );
    }
}
export default WebSocketController