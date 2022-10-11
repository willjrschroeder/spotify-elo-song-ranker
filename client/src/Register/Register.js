import FormInput from "../FormInput/FormInput";
import {setState, useContext} from "react";
import AuthContext from "../context/AuthContext";
import {useHistory} from "react-router-dom";

function Register() {

    const [username, setUsername] = setState("");
    const [password, setPassword] = setState("");
    const [errors, setErrors] = setState([]);
    const loginRequest = {username, password};

    const auth = useContext(AuthContext);

    const history = useHistory();



function addUser( event ) {
    event.preventDefault();

    fetch( "http://localhost:8080/api/agent", {
        method:"POST",
        body: JSON.stringify(loginRequest),
        headers: {
            "Content-Type": "application/json"
        }
    }).then( async response => {
        if( response.status === 201 ) {
            history.push("/");
            return response.json();
        } 
            return Promise.reject( await response.json() );
    })
    .catch( errorList => {
        if( errorList instanceof TypeError ){
            console.log( "Could not connect to api.");
        } else {
            console.log(errorList);
        }
    });
}

    
    return(<>
        <form onSubmit={addUser}>
            <FormInput
                InputType = {"text"}
                indentifier = {"username"}
                labelText = {"Username"}
                onChangeHandler = {(event) => setUsername(event.target.value)}/>
            <FormInput
                InputType = {"text"}
                indentifier = {"username"}
                labelText = {"Username"}
                onChangeHandler = {(event) => setPassword(event.target.value)}/>
        </form>
    </>
    )
}
export default Register;