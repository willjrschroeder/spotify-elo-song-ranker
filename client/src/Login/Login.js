import React from "react";
import FormInput from "../FormInput/FormInput"
import jwtDecode from 'jwt-decode';
import {Link, useHistory} from "react-router-dom";



function Login(props) {
    const history = useHistory();

    function loginHandler(event) {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const loginRequest = {username, password};

        fetch("http://localhost:8080/api/security", {
            method: "POST",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(loginRequest)
        })
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else {
                console.log(response) //don't know how to handle errors
            }
        })
        .then( jwtContainer => {
            const jwt = jwtContainer.jwt_token;
            const claimsObject = jwtDecode( jwt );

            console.log( jwt );
            console.log( claimsObject );
            
            props.setLoginInfo({jwt, claims:claimsObject} );
            history.push("/")
        })
        .catch (error => {
            if (error instanceof TypeError) {
                console.log("Could not connect to api.");
            } else {
                console.log(error);
            }
        })
    }

    return (
        <>
        <h2>Login</h2>
        <div>
            <form onSubmit={loginHandler}>
                <FormInput
                InputType = {"text"}
                indentifier = {"username"}
                labelText = {"Username"}
                onChangeHandler = {"inputChangeHandler"}/>
    
                <FormInput
                InputType = {"text"}
                indentifier = {"password"}
                labelText = {"Password"}
                onChangeHandler = {"inputChangeHandler"}/>
                
                <div>
                    <button>Log In</button>
                    <Link to = "/register">Register</Link>
                    <Link to = "/">Cancel</Link>
                </div>
    
            </form>
        </div>
    </>
    )
}



export default Login;