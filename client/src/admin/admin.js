import { useState } from "react";
import { Link } from "react-router-dom";
import User from "./Users/Users";
import UserPlaylist from "./Users/UserPlaylist";

function admin() {
    const [playlists, setPlaylists] = useState([])
    const [currentUser, setCurrentUser] = useState(null);
    const [users, setUsers] = useState([]);

    const userLocation = {
        pathname: '/confirmDelUser',
        state: currentUser
      }
      console.log(location);

    function getAllUsers() {

    }
    function getPlaylistsByUser() {

    }

    return(
        <>
        <div>
            <h2 className="manage-header">Manage Users</h2>
            <div className="home">
                <button>Return to Home</button>
            </div>
            <div className="flex-users">
                <form onChange={(event) => setCurrentUser(event.target.value)}>
                    <input list="users"></input>
                    <datalist id="users"></datalist>
                        {users.map( u => (<User u={u}></User>))}
                </form>
                <div className="flex-user-select">
                    <div>
                        <h4>Username:{currentUser.username}</h4>
                        <h4>Display Name:{currentUser.displayName}</h4>
                        <button onClick={EditUser}>Update User</button>
                        <div><Link to={location}>Delete</Link></div> 
                    </div>
                    <div>
                        <h4>User Playlists</h4>
                        <table>
                            <thead>
                                <th>Name</th>
                                <th></th>
                            </thead>
                            <tbody>
                                {playlists.map((p, index) => (<UserPlaylist p={p} key={index}></UserPlaylist>))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        </>
    )
}
export default admin;