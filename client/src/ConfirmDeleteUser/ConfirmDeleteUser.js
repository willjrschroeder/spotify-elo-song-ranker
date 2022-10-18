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
                const holder = response.json();
                history.push("/admin")
                return holder;
                } else (console.log(response)) // TODO: log success message
        }).catch(error => {
            console.log(error); // TODO: log error message
        })
    }

    return(
        <>
            <div className="flex-deleteUser">
                <div className="confirmDeleteUser">Are you sure you want to delete user: {user.username + ": " + user.displayName}? This action cannot be undone</div>
                <div>
                    <button onClick={deleteUser}>Delete</button>
                    <Link to="/admin">Cancel</Link>
                </div>

            </div>
        </>
    )

}
export default ConfirmDeleteUser;