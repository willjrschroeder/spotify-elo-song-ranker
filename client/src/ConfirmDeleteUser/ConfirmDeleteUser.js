import { Link } from "react-router-dom";
import "./ConfirmDeleteUser.css";



function ConfirmDeleteUser() {

    function deleteUser() {

    }

    return(
        <>
            <div className="flex-deleteUser">
                <div className="confirmDeleteUser">Are you sure you want to delete user: phil hanky? This action cannot be undone</div>
                <div>
                    <button onClick={deleteUser}>Delete</button>
                    <Link to="/admin">Cancel</Link>
                </div>

            </div>
        </>
    )

}
export default ConfirmDeleteUser;