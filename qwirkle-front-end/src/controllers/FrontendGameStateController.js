import React, {Component} from 'react';
import '../App.css'
import { v4 as uuid } from 'uuid';
import StartView from '../views/StartView';
import RuleView from '../views/RuleView';
import GameSelectView from '../views/GameSelectView';
import JoinByCodeView from '../views/JoinByCodeView';
import WebSocketController from './WebSocketController';


const ACTIVE_URL = process.env.REACT_APP_AWS_BE_URL;

class FrontendGameStateController extends Component{
    state = {
        gameStarted: false,
        showRules: false,
        showGameSelect: false,
        showJoinByCodeScreen: false,
        showWaitRoom: false,
        connectWebSockets: false,
        myPlayerNum: 1,
        currScreen: "start",
        gameCode: "",
        squaresWithTiles: [],
        disconnected: false
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
        fetch(`${ACTIVE_URL}${endpoint}`, requestOptions)
            .then(res => {
                if (res.status === 400){
                    res.json().then(parsed => {console.log(parsed)})
                    // console.log(res);
                }
            })
    }


    quickJoin = () => {
        let playerId = uuid().toString();
        
        this.setState(prevState => {
            return {
                myPlayerId: playerId,
            }
        }, () => {
            let body = {
                playerId: playerId
            };
            const requestOptions = {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(body)
            };
            fetch(`${ACTIVE_URL}/joinRandom`, requestOptions)
            .then(res => {
                return res.text();
            }).then(parsed => {
                console.log("gameCode: " + parsed);
                this.setState({gameCode: parsed}, () => {
                    this.showWaitRoom();
                    this.setState({quickJoin: true, connectWebSockets: true});
                })
            });
        });
    }

    joinByCode = (code) => {
        let playerId = uuid().toString();
        this.setState(prevState => {
            return {
                gameCode: code,
                myPlayerId: playerId,
            }
        }, () => {
            let body = {
                playerId: playerId
            }
            const requestOptions = {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(body)
            };
            fetch(`${ACTIVE_URL}/joinByCode?gameCode=${code}`, requestOptions)
            .then(res => {
                console.log(res.body);
            })
            .then(() => {
                this.showWaitRoom();
                this.setState({
                    connectWebSockets: true
                })
            })
        });
    }

    createLobby = () => {
        let playerId = uuid().toString();
        this.setState(prevState => {
            return {
                myPlayerId: playerId,
            }
        }, () => {
            let body = {
                playerId: playerId
            }
            const requestOptions = {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(body)
            };
            
            fetch(`${ACTIVE_URL}/createLobby`, requestOptions)
            .then(res => {
                return res.text();
            }).then(parsed => {
                console.log("gameCode: " + parsed);
                this.setState({gameCode: parsed}, () => {
                    this.showWaitRoom();
                    this.setState({connectWebSockets: true});
                })
            });

        });
    }

    showRules = () => {
        this.setState({showRules: true});
    }

    showGameSelect = () => {
        this.setState(prevState => {
            return{
                showGameSelect: true,
                showJoinByCodeScreen: false,
                showWaitRoom: false,
                showRules: false,
                connectWebSockets: false,
                // prevScreens: [...prevState.prevScreens, this.state.currScreen]
            }
        }, () => {
            this.setState({
                currScreen: "gameSelect"
            });
            console.log("showGameSelect: " + this.state.showGameSelect);
        });
    }

    showJoinByCodeScreen = () => {
        console.log("function called");
        this.setState(prevState => {
            return{
                showJoinByCodeScreen: true,
                showGameSelect: true,
                showWaitRoom: false,
                showRules: false,
                // prevScreens: [...prevState.prevScreens, this.state.currScreen]
            }
        }, () => {
            this.setState({
                currScreen: "joinByCode"
            })
            console.log("showJoinByCode: " + this.state.showJoinByCodeScreen);
        });
        console.log("exiting function");
    }

    showWaitRoom = () => {
        console.log("show wait room function called");
        this.setState( prevState => {
            return{
                showWaitRoom: true,
                showGameSelect: true,
                showJoinByCodeScreen: false,
                showRules: false,
                // prevScreens: [...prevState.prevScreens, prevState.currScreen]
            }
        }, () => {
            this.setState({
                currScreen: "waitRoom"
            })
            console.log("showWaitRoom: " + this.state.showWaitRoom);
        });
        console.log("exiting function");
    }


    backToStart = () => {
        this.setState({
            showRules: false,
            showGameSelect: false,
            connectWebSockets: false,
            showWaitRoom: false,
            showGameSelect: false,
            showJoinByCodeScreen: false,
            quickJoin: false
        })
    }

    goBack = () => {
        if (this.state.currScreen === "gameSelect"){
            this.backToStart();
        }
        else if (this.state.currScreen === "joinByCode"){
            this.showGameSelect();
        }
        else if (this.state.currScreen === "waitRoom"){
            let body = {
                playerId: this.state.myPlayerId
            }
            this.sendPostRequest("/disconnectWaitRoom?gameCode=" + this.state.gameCode, JSON.stringify(body));
            this.showGameSelect();
            if (this.state.quickJoin){
                this.setState({quickJoin: false})
            }
        }
    }
    
    render() {
        return(
            this.state.connectWebSockets?
            <WebSocketController myPlayerId={this.state.myPlayerId} gameCode={this.state.gameCode} goBack={this.goBack} backToStart={this.backToStart} showGameSelect={this.state.showGameSelect} quickJoin={this.state.quickJoin}/>:
            this.state.showRules?
            <div className='App'>
                <RuleView backToStart={this.backToStart}/>
            </div>:
            this.state.showJoinByCodeScreen?
            <div className='App'>
                <JoinByCodeView state={this.state} goBack={this.goBack} joinByCode={this.joinByCode}/>
            </div>:
            this.state.showGameSelect?
            <div className='App'>
                <GameSelectView state={this.state} goBack={this.goBack} createLobby={this.createLobby} quickJoin={this.quickJoin} showJoinByCodeScreen={this.showJoinByCodeScreen}/>
            </div>:
            <div className='App'>
                <StartView showGameSelect={this.showGameSelect} playerJoin={this.playerJoin} showRules={this.showRules}/>
            </div>
        );
    }
}

export default FrontendGameStateController;