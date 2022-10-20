import { useContext, useEffect, useState } from "react"
import AuthContext from "../context/AuthContext";
import { Link } from "react-router-dom";
import "./Home.css"
import Playlist from "../Playlist/Playlist";
import HomePagePlaylist from "../Playlist/HomePagePlaylist";

function Home() {

    const [selectedPlaylist, setSelectedPlaylist] = useState();
    const [playlists, setPlaylists] = useState([]);
    const [tracks, setTracks] = useState([]);
    const auth = useContext(AuthContext);

    function loadPlaylistsbyUser() {
        fetch("http://localhost:8080/api/playlist/" + auth.user.id, {
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
            });
    }

    useEffect(() => {
        loadPlaylistsbyUser();
    }, [])
    function isEmpty() {
        if (playlists === undefined || playlists.length < 1) {
            return true;
        } else {
            return false;
        }
    }

    return ( //TODO: add conditional render for if there are no playlists added yet
        <>
        <div className="flex-home">
            <div className="header">Home Page</div>
            <table className="table table-dark table-striped w-50">
                <thead className="table-dark">
                    <tr>
                        <th></th>
                        <th>Title</th>
                        <th>Rankings</th>
                    </tr>
                </thead>
                <tbody>
                {!isEmpty() ? playlists.map( (p,index) => (<HomePagePlaylist key = {index} p = {p}/>)) : 
                    <tr className="message empty">
                        <td colSpan={3}>Go to Manage Playlists to view tracks here</td>
                    </tr>}
                </tbody>
            </table>
        </div>
            
            
        </>
    )
}
export default Home;