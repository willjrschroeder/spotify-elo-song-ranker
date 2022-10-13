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
            //TODO: display this error
        }

        fetch("http://localhost:8080/api/security/register", {
            method: "POST",
            body: JSON.stringify(loginRequest),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(async response => {
            if (response.status === 201) {
                clearErrors();
                history.push("/login");
                return response.json();
            }
            return Promise.reject(await response.json());
        })
            .catch(listOfErrorMessages => {
                if (listOfErrorMessages instanceof TypeError) {
                    showErrors("Could not connect to api.")
                    console.log("Could not connect to api.");
                } else { //TODO: This else clause is always triggered on error
                    showErrors(listOfErrorMessages.messages) // TODO: the caught response 'errorList' is not an array or a list. It is a listOfErrorMessages object
                    console.log(listOfErrorMessages.messages);
                }
            });
    }

    function showErrors( errorMessage ){ //TODO: update this function to handle listOfErrorMessages object.
                                                // the object has a 'message' property
                                                // listOfErrorMessages.message yields a string of the error message
                                                // we'll can probably just pass in the string to the function?
                                                // and then custom error messages can be plain strings too
                                                // // not arrays of strings ex. ["Could not connect to api"] vs "Could not connect to api"

        const messageContainer = document.getElementById("messages");
    
        //TODO: this should not use a map, we'll working with strings instead of arrays
        messageContainer.innerText = messageContainer.innerText + (<p>errorMessage</p>);
    
    }
    function clearErrors(){
        document.getElementById("messages").innerHTML = "";
    }
    


    return (<>
    <div className="flex-register">
        <h2>Register</h2>
        <div className="messages"></div>
        <form onSubmit={addUser}>
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
            <FormInput
                InputType={"password"}
                indentifier={"confirmPassword"}
                labelText={"Confirm Password"}
                onChangeHandler={(event) => setConfirmPassword(event.target.value)} />
            <FormInput
                InputType={"text"}
                indentifier={"displayName"}
                labelText={"Display Name"}
                onChangeHandler={(event) => setDisplayName(event.target.value)} />
            <div className="flex-buttons">
                <button>Submit</button>
                <Link to = "/">Cancel</Link>
            </div>
        </form>
    </div>


    </>
    )
}
export default Register;