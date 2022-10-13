import FormInput from "../FormInput/FormInput";
import { useState } from "react";
import { useHistory, Link } from "react-router-dom";
import "./Register.css";

function Register() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [displayName, setDisplayName] = useState("");
    const loginRequest = { username, password, displayName };

    const history = useHistory();

    function addUser(event) {
        event.preventDefault();

        if(password !== confirmPassword) {
            showErrors("Password and confirm password do not match");
            return;
        }

        fetch("http://localhost:8080/api/security/register", {
            method: "POST",
            body: JSON.stringify(loginRequest),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(async response => {
            if (response.status === 201) {
                resetForm();
                clearErrors();
                showSuccess("Registration successful.");
                showSuccess("Please log in with your new account.");
                return response.text();
            }
            return Promise.reject(await response.text());
        })
            .catch(errorMessageString => {
                if (errorMessageString instanceof TypeError) {
                    showErrors("Could not connect to api.")
                } else {
                    showErrors(errorMessageString)
                }
            });
    }

    function showErrors( errorMessage ){ 
        clearErrors();

        const messageContainer = document.getElementById("messages");

        messageContainer.innerHTML = messageContainer.innerHTML + `<p>${errorMessage}</p>`;
    }

    function showSuccess( successMessage ){ 
        const messageContainer = document.getElementById("messages");

        messageContainer.innerHTML = messageContainer.innerHTML + `<p>${successMessage}</p>`;
    }

    function clearErrors(){
        document.getElementById("messages").innerHTML = "";
    }

    function resetForm(){
        setUsername("");
        setPassword("");
        setConfirmPassword("");
        setDisplayName("");
    }
    
    return (<>
    <div className="flex-register">
        <h2>Register</h2>
        <div className="messages" id="messages"></div>
        <form onSubmit={addUser}>
            <FormInput
                InputType={"text"}
                indentifier={"username"}
                labelText={"Username"}
                currVal={username}
                onChangeHandler={(event) => setUsername(event.target.value)} />
            <FormInput
                InputType={"password"}
                indentifier={"password"}
                labelText={"Password"}
                currVal={password}
                onChangeHandler={(event) => setPassword(event.target.value)} />
            <FormInput
                InputType={"password"}
                indentifier={"confirmPassword"}
                labelText={"Confirm Password"}
                currVal={confirmPassword}
                onChangeHandler={(event) => setConfirmPassword(event.target.value)} />
            <FormInput
                InputType={"text"}
                indentifier={"displayName"}
                labelText={"Display Name"}
                currVal={displayName}
                onChangeHandler={(event) => setDisplayName(event.target.value)} />
            <div className="flex-buttons">
                <button>Submit</button>
                <Link to = "/login"><button>Return to Login</button></Link>
            </div>
        </form>
    </div>


    </>
    )
}
export default Register;