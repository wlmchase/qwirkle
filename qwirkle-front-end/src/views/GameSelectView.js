import React, {Component} from 'react';
import QuickJoinButton from '../components/QuickJoinButton';
import JoinByCodeButton from '../components/JoinByCodeButton';
import CreateLobbyButton from '../components/CreateLobbyButton';
import BackButton from '../components/BackButton';

class GameSelectView extends Component{

    render() {
        return (
            this.props.state.showGameSelect ? 
            <div>
                <QuickJoinButton state={this.props.state} quickJoin={() => {this.props.quickJoin()}}/>
                <JoinByCodeButton state={this.props.state} showJoinByCodeScreen={() => {this.props.showJoinByCodeScreen()}}/>
                <CreateLobbyButton createLobby={() => {this.props.createLobby()}}/>
                <BackButton goBack={()=> {this.props.goBack()}}/>
            </div>:
            <div></div>
        );
    }
}
export default GameSelectView