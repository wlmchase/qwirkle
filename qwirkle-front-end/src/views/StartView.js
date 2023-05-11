import React, {Component} from 'react';
import RulesButton from '../components/RulesButton';
import StartButton from '../components/StartButton';

class StartView extends Component{

    render() {
        return (
            <div>
                <div className='title'>
                    <h1 className='titleLetter'>Q</h1>
                    <h1 className='titleLetter'>w</h1>
                    <h1 className='titleLetter'>i</h1>
                    <h1 className='titleLetter'>r</h1>
                    <h1 className='titleLetter'>k</h1>
                    <h1 className='titleLetter'>l</h1>
                    <h1 className='titleLetter'>e</h1>
                </div>
                {/* <StartButton playerJoin={() => {this.props.playerJoin()}}/> */}
                <StartButton showGameSelect={() => {this.props.showGameSelect()}}/>
                <RulesButton showRules={() => {this.props.showRules()}}/>
            </div>
        );
    }
}
export default StartView