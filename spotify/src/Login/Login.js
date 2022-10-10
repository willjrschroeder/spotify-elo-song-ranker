import React from "react";
import FormInput from "../FormInput/FormInput"



function Login() {

}

return (
    <>
    <h2>Login</h2>
    <div>
        <form>
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
                <Link to = "/Register">Register</Link>
                <Link to = "/">Cancel</Link>
            </div>

        </form>
    </div>
</>
)

export default Login;