import FormInput from "../FormInput/FormInput";
import { useState, useContext } from "react";
import AuthContext from "../context/AuthContext";
import { useHistory, Link } from "react-router-dom";
import "./Register.css";

function Register() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [displayName, setDisplayName] = useState("");
    const loginRequest = { username, password, displayName };

    const auth = useContext(AuthContext);

    const history = useHistory();



    function addUser(event) {
        event.preventDefault();

        if(password !== confirmPassword) {
            showErrors(["Password and confirm password do not match"]);
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
                history.push("/login");
                return response.json();
            }
            return Promise.reject(await response.json());
        })
            .catch(errorList => {
                if (errorList instanceof TypeError) {
                    showErrors(["Could not connect to api."])
                    console.log("Could not connect to api.");
                } else {
                    showErrors(errorList)
                    console.log(errorList);
                }
            });
    }

    function showErrors( listOfErrorMessages ){

        const messageContainer = document.getElementById("messages");
    
        messageContainer.innerHTML = listOfErrorMessages.map( m => "<p>" + m + "</p>" ).reduce( (prev, curr) => prev + curr );
    
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
        </form>
    </div>
    <div className="flex-buttons">
        <button>Submit</button>
        <Link to = "/">Cancel</Link>
    </div>

    </>
    )
}
export default Register;