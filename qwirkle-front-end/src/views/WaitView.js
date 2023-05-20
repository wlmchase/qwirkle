import React, {Component} from 'react';
import BackButton from '../components/BackButton';

class WaitView extends Component {
    state={}

    render() {

        return (
            <div>
                <p className='waitText'> Looking for a game </p>
                <BackButton goBack={() => this.props.goBack()}/>
            </div>
        );
    }
}
export default WaitView