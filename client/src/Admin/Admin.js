import { useState, useEffect, useContext } from "react";
import { Link } from "react-router-dom";
import User from "./Users/Users";
import UserPlaylist from "./Users/UserPlaylist";
import AuthContext from "../context/AuthContext";
import "./Admin.css";

function Admin() {
    const [playlists, setPlaylists] = useState([])
    const [currentUser, setCurrentUser] = useState();
    const [users, setUsers] = useState([]);



    const auth = useContext(AuthContext);

    const userLocation = {
        pathname: '/confirmDelUser',
        state: currentUser
    }
    console.log(userLocation);
    console.log(currentUser)

    function getAllUsers() {
        fetch(`http://localhost:8080/api/user/active`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else (console.log(response))
            })
            .then(users => {
                setUsers(users);
            });
    }
    useEffect(
        () => {
            getAllUsers();
            console.log(users);
        },
        []
    )
    function getPlaylistsByUser(id) {
        fetch(`http://localhost:8080/api/playlist/${id}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
            .then(response => {
                if (response.status === 200) {

                    return response.json();
                } else (console.log(response))
            })
            .then(playlists => {
                setPlaylists(playlists);
                console.log(playlists);
            });
    }
    function handleChange(event) {
        let username = event.target.value;
        for (let x = 0; x < users.length; x++) {
            if (users[x].username === username) {
                setCurrentUser(users[x]);
                getPlaylistsByUser(users[x].appUserId);
            }
        }
        console.log(playlists);
    }



    return (
        <>
            <div>
                <h2 className="manage-header">Manage Users</h2>
                <div className="flex-users">
                    <div className="flex-select">
                        <div>Select a User:</div>
                        <form onChange={handleChange}>
                            <input list="users"></input>
                            <datalist id="users">
                                {users.map((u, index) => (<User key={index} u={u}></User>))}
                            </datalist>
                        </form>
                    </div>
                    <div className="flex-user-select">
                        {!currentUser ?
                            <div>
                                <h3>Select a User to View Their Data</h3>
                            </div>
                            :
                            <div>
                                <h4>Username: {currentUser.username}</h4>
                                <h4>Display Name: {currentUser.displayName}</h4>
                                <Link to={userLocation}>
                                    <button>Delete User</button>
                                </Link>
                                <div>
                                    <h4>User Playlists</h4>
                                    <table className="table">
                                        <thead>
                                            <tr>
                                                <th colSpan={3}><div style={{minHeight: 3 + 'em'}}>Playlists in Your Spotify Catalog</div></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {playlists.map((p, index) => (<UserPlaylist p={p} key={index}></UserPlaylist>))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>}
                    </div>
                </div>
            </div>
        </>
    )
}
export default Admin;
