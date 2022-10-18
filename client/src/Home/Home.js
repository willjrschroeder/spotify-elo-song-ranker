import { useContext, useEffect, useState } from "react"
import AuthContext from "../context/AuthContext";
import { Link } from "react-router-dom";
import "./Home.css"
import UserPlaylist from "./UserPlaylist";
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
                console.log(playlists);
                setPlaylists(playlists);
            });
    }

    function loadTracksByPlaylist() {
        fetch("http://localhost:8080/api/track/playlist/" + selectedPlaylist, {
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
            .then(tracks => {
                console.log(tracks);
                setTracks(tracks);
            });
    }
    useEffect(() => {
        loadPlaylistsbyUser();
    }, [])

    function handleChange(event) {
        let playlistId = event.target.value;
        setSelectedPlaylist(playlistId);

        loadTracksByPlaylist();
    }

    return (
        <>
            <div className="header">Home Page</div>

        </>
    )
}
export default Home;