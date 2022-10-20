import React, { useState, useContext } from "react";
import FormInput from "../FormInput/FormInput";
import { Link, useHistory } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import "./Login.css"

function Login(props) {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const auth = useContext(AuthContext);

    const history = useHistory();

    const loginHandler = async (event) => {
        event.preventDefault();

        fetch("http://localhost:8080/api/security/authenticate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username,
                password
            })
        }).then(async response => {
            if (response.status === 200) {
                const { jwt_token } = await response.json();
                if(auth.login(jwt_token)) {
                    history.push("/spotify");
                }
                else {
                    showErrors("Invalid login credentials");
                }
            } else if (response.status === 403) {
                showErrors("Invalid login credentials.")
            }
        }).catch(error => {
            showErrors("Internal issue. Try again later.")
        });
    };

    function showErrors(errorMessage) {
        clearErrors();

        const messageContainer = document.getElementById("messages");

        messageContainer.innerHTML = messageContainer.innerHTML + `<p>${errorMessage}</p>`;
    }

    function clearErrors() {
        document.getElementById("messages").innerHTML = "";
    }


    return (
        <>
        <div className="headphones"></div>
        <div className="backing"></div>
            <div className="flex-login">
           
                <h2>Login</h2>
                <div className="messages" id="messages"></div>
                <form onSubmit={loginHandler}>
                    <FormInput
                        inputType={"text"}
                        indentifier={"username"}
                        labelText={"Username"}
                        onChangeHandler={(event) => setUsername(event.target.value)} />

                    <FormInput
                        inputType={"password"}
                        indentifier={"password"}
                        labelText={"Password"}
                        onChangeHandler={(event) => setPassword(event.target.value)} />

                    <div className="loginButton">
                        <button>Log In</button>
                        <Link to="/">
                            <button>Cancel</button>
                        </Link>
                    </div>
                    <div>
                        <Link to="/register">
                            <button>Register</button>
                        </Link>
                    </div>
                </form>
                
            </div>
        </>
    )
}



export default Login;