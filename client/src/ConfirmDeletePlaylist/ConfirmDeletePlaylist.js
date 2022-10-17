import { Link } from "react-router-dom";
import "./ConfirmDeletePlaylist.css";

function ConfirmDeletePlaylist() {

    function deletePlaylist() {
        
    }

    return(
        <>
            <div className="flex-deleteUser">
                <div className="confirmDeleteUser">Are you sure you want to delete playlist: playlist1? This action cannot be undone</div>
                <div>
                    <button onClick={deletePlaylist}>Delete</button>
                    <Link to="/playlistManager">Cancel</Link>
                </div>

            </div>
        </>
    )
}
export default ConfirmDeletePlaylist;