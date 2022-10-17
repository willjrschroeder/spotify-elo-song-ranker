import { Link, useLocation } from "react-router-dom";
import "./ConfirmDeleteUser.css";



function ConfirmDeleteUser() {

    const location = useLocation();
    const user = location.state;


    function deleteUser() {

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