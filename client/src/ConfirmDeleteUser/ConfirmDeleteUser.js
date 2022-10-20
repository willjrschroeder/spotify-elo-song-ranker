import { useContext } from "react";
import { Link, useHistory, useLocation } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import "./ConfirmDeleteUser.css";



function ConfirmDeleteUser() {

    const location = useLocation();
    const user = location.state;
    const serverAuth = useContext(AuthContext);

    const history = useHistory();

    function deleteUser() {
        fetch(`http://localhost:8080/api/user/delete/${user.appUserId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + serverAuth.user.token
            }
        }).then(response => {
            if (response.status === 204) {
                showMessage("User successfully deleted.", false);
            } else (showMessage("Error deleting user from the database.", true))
        }).catch(error => {
            showMessage("Error connecting to the server.", true);
        })
    }

    function showMessage(message, isErrorMessage) {
        clearMessages();

        const messageContainer = document.getElementById("messages");

        messageContainer.innerHTML = messageContainer.innerHTML + `<p>${message}</p>`;

        if (isErrorMessage) {
            messageContainer.className = "alert alert-danger w-25 m-auto mb-4";
        } else {
            messageContainer.className = "alert alert-success w-25 m-auto mb-4";
        }
    }

    function clearMessages() {
        document.getElementById("messages").innerHTML = "";
        document.getElementById("messages").className = "";
    }

    return (
        <>
            <div className="flex-deleteUser">
                <div id="messages" role="alert" style={{ minHeight: '40px' }}></div>
                <div className="confirmDeleteUser">Are you sure you want to <span className="delete-user">DELETE USER: {user.username + ": " + user.displayName}?</span>  This action cannot be undone</div>
                <div>
                    <button onClick={deleteUser}>Delete</button>
                    <Link to="/admin"><button>Return To Manage Users</button></Link>
                </div>

            </div>
        </>
    )

}
export default ConfirmDeleteUser;