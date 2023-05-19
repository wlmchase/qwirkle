import React, {Component} from 'react';

class WaitView extends Component {
    state={}

    render() {

        // console.log("in WaitRoomView");
        // console.log(this.props.state.waitRoom.waitingPlayerList.length);
        // console.log(this.props.state.disconnected);


        // if (this.props.state.disconnected === true) {
        //     console.log("is this code being reached?");
        //     //this.props.goBack();
        //     this.props.disconnect()
        // }

        return (
            <div>
                <p className='waitText'> Connecting to game </p>
            </div>
        );
    }
}
export default WaitView