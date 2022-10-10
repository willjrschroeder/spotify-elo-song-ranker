import React from "react";

function FormInput(props){

    return (
        <div >
            <label className="form-label" htmlFor={props.identifier}>{props.labelText}</label>
            <input className="form-control" 
                type={props.inputType} 
                id={props.identifier} 
                name={props.identifier} 
                defaultValue={props.currVal}
                onChange={props.onChangeHandler}
                />
        </div>
    );

}

export default FormInput;