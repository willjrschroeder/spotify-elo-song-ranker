import React, {useState, useContext} from "react";
import FormInput from "../FormInput/FormInput";
import {Link, useHistory} from "react-router-dom";
import AuthContext from "../context/AuthContext";



function Login(props) {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    // const [errors, setErrors] = useState([]);

    // const loginMap = new Map(Object.entries(loginRequest));

    const auth = useContext(AuthContext);

    const history = useHistory();

    const loginHandler = async (event) => {
        event.preventDefault();

        const response = await fetch("http://localhost:8080/api/security/authenticate", {
            method: "POST",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify({
                username,
                password,
            })
        })
        
        if( response.status === 200 ) {
            const {jwt_token} = await response.json();
            console.log(jwt_token);
            // NEW:LOGIN
            auth.login(jwt_token);
            history.push("/home")
        } else if (response.status === 403){
            console.log("login failed")
            // setErrors(["Login failed."])
        } else {
            console.log("unknown error.")
            // setErrors(["Unknown Error."])
        
        }
    };
        // then( jwtContainer => {
        //     const { jwt_token } = await jwtContainer.jwt_token;
        //     console.log(jwt_token);
        //         // NEW: login!
        //     auth.login(jwt_token);
        //     history.push("/");

        //     console.log( jwt_token );
            
        //     // props.setUser({jwt, claims:claimsObject} );
        //     history.push("/")
        // });
        // .catch (error => {
        //     if (error instanceof TypeError) {
        //         console.log("Could not connect to api.");
        //     } else {
        //         console.log(error);
        //     }
        // })
    

    return (
        <>
        <h2>Login</h2>
        <div>
            {/* {auth.user ? (
                <>
                <h3>You are already logged in!</h3>
                <button onClick={() => auth.logout()}>Logout</button>
                </>
            ) : ( */}
                <form onSubmit={loginHandler}>
                <FormInput
                InputType = {"text"}
                indentifier = {"username"}
                labelText = {"Username"}
                onChangeHandler = {(event) => setUsername(event.target.value)}/>
    
                <FormInput
                InputType = {"password"}
                indentifier = {"password"}
                labelText = {"Password"}
                onChangeHandler = {(event) => setPassword(event.target.value)}/>
                
                <div>
                    <button>Log In</button>
                    <Link to = "/register">Register</Link>
                    <Link to = "/">Cancel</Link>
                </div>
    
            </form>
            {/* )} */}
            
        </div>
    </>
    )
}



export default Login;