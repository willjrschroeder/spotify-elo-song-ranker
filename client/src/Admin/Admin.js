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
            method: "POST",
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
        },
        []
    )
    function getPlaylistsByUser() {
        fetch(`http://localhost:8080/api/user/${auth.user.id}`, {
            method: "POST",
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
    function handleChange(event) {
        let username = event.target.value;
        let userArray = users.filter(u => u.username === username)
        setCurrentUser(userArray[0]);
    }


    return (
        <>
            <div>
                <h2 className="manage-header">Manage Users</h2>
                <div className="home">
                    <button>Return to Home</button>
                </div>
                <div className="flex-users">
                    <form onChange={handleChange}>
                        <input list="users"></input>
                        <datalist id="users">
                            {users.map((u, index) => (<User key={index} u={u}></User>))}
                        </datalist>

                    </form>
                    <div className="flex-user-select">
                        {!currentUser ? <div>
                            <h4>Username:</h4>
                            <h4>Display Name:</h4>
                            <button>Update User</button>
                            <div><button>Delete</button></div>
                        </div> : <div>
                            <h4>Username: {currentUser.username}</h4>
                            <h4>Display Name: {currentUser.displayName}</h4>
                            <button>Update User</button>
                            <div><Link to={userLocation}>Delete</Link></div>
                        </div>}


                        <div>
                            <h4>User Playlists</h4>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th></th>
                                    </tr>
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
export default Admin;
