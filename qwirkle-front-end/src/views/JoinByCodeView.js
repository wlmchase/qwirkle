import React from 'react';
import {useState} from 'react';
import {TextField} from "@mui/material";
import BackButton from '../components/BackButton';



const JoinByCodeView = (props) => {

    const [code, setCode] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        props.joinByCode(code);
    }

    return(  
        <div className='textFieldContainer'>
            <p className='codeText'>Code:</p>
            <form onSubmit={handleSubmit}>
                <label>
                    <input 
                        type="text"
                        className='input' 
                        value={code}
                        onChange={(e) => setCode(e.target.value)}
                    />
                </label>
                <input className='submitButton' type="submit" />
            </form>
            <BackButton goBack={()=> {props.goBack()}}/>
        </div>
    )

}

export default JoinByCodeView