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
                "Content-type": "application/json"
            },
            body: JSON.stringify({
                username,
                password,
            })
        }).then(async response => {
            if (response.status === 200) {
                const { jwt_token } = await response.json();
                auth.login(jwt_token);
                history.push("/spotify")
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
            <div className="flex-login">
                <h2>Login</h2>
                <div className="messages" id="messages"></div>
                {auth.user ? (
                    <>
                        <h3>You are already logged in!</h3>
                        <button onClick={() => auth.logout()}>Logout</button>
                    </>
                ) : (
                    <form onSubmit={loginHandler}>
                        <FormInput
                            InputType={"text"}
                            indentifier={"username"}
                            labelText={"Username"}
                            onChangeHandler={(event) => setUsername(event.target.value)} />

                        <FormInput
                            InputType={"password"}
                            indentifier={"password"}
                            labelText={"Password"}
                            onChangeHandler={(event) => setPassword(event.target.value)} />

                        <div className="loginButton">
                            <button>Log In</button>
                            <Link to="/"><button>Cancel</button></Link>
                        </div>
                    </form>
                )}

            </div>
        </>
    )
}



export default Login;