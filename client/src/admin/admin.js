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
    const user = {
        username:"fill"
    }


    const auth = useContext(AuthContext);

    const userLocation = {
        pathname: '/confirmDelUser',
        state: currentUser
      }
      console.log(userLocation);

    function getAllUsers() {
            fetch( `http://localhost:8080/api/user/`)
            .then( response => {
                if( response.status === 200 ) {
                    return response.json();
                } else( console.log( response ) )
            } )
            .then( users => {
                setUsers( users );
            });
    }
    useEffect(
        () => {
            getAllUsers();
        },
        []
    )
    function getPlaylistsByUser() {
        fetch( `http://localhost:8080/api/user/${auth.user.id}`)
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else( console.log( response ) )
        } )
        .then( users => {
            setUsers( users );
        });
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
                    {!currentUser ? <div>
                        <h4>Username:</h4>
                        <h4>Display Name:</h4>
                        <button>Update User</button>
                        <div><Link>Delete</Link></div> 
                    </div> : <div>
                        <h4>Username:{currentUser.username}</h4>
                        <h4>Display Name:{currentUser.displayName}</h4>
                        <button>Update User</button>
                        <div><Link to={userLocation}>Delete</Link></div> 
                    </div>}
                    
                    
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
export default Admin;