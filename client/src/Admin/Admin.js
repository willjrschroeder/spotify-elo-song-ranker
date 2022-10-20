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
            <div className="manage-user">
                <h1 className="page-header mt-4">Manage Users</h1>
                <div className="flex-users">
                    
                    <div className="flex-user-select">
                    <div className="flex-select">
                        
                        <form onChange={handleChange}>
                            <label for="users"><span className="bolded">Select a User:</span></label>
                            <select name="users"id="users">
                            <option value="none" selected disabled hidden>Select an Option</option>
                                {users.map((u, index) => (<User key={index} u={u}></User>))}
                            </select>
                        </form>
                    </div>
                        {!currentUser ?
                            <div className="selected">
                                <h4>Select a User to View Their Data</h4>
                            </div>
                            :
                            <div className="selected">
                                <h4><span className="bolded">Username:</span> {currentUser.username}</h4>
                                <h4><span className="bolded">Display Name: </span>{currentUser.displayName}</h4>
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
