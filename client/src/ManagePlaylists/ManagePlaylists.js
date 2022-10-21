import Playlist from "../Playlist/Playlist"
import "./ManagePlaylists.css"
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import { useEffect, useState, useContext } from "react";
import AuthContext from "../context/AuthContext";
import DatabasePlaylist from "../Playlist/DatabasePlaylists";
import getSpotifyData from "./GetSpotifyData/getSpotifyData";


const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});



function ManagePlaylists() {
    const spotifyAuth = useContext(SpotifyAuthContext);
    const serverAuth = useContext(AuthContext);

    const [playlists, setPlaylists] = useState([]);
    const [databasePlaylists, setDatabasePlaylists] = useState([])

    function getAllPlaylists() {
        fetch(`http://localhost:8080/api/playlist/${serverAuth.user.id}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + serverAuth.user.token
            }
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else (console.log(response))
            })
            .then(databasePlaylists => {
                setDatabasePlaylists(databasePlaylists);
            })
    }


    function loadAllPlaylists() {

        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken);

        spotifyApi.getMe()
            .then(function (data) {
                return spotifyApi.getUserPlaylists(data.body.id);
            })
            .then(function (data) {
                setPlaylists(data.body.items);
            });
    }

    useEffect(() => {
        loadAllPlaylists();
        getAllPlaylists();
    }, []);

    async function transferPlaylistToTracking(playlistId) {
        const data = await getSpotifyData(playlistId, serverAuth.user.id, spotifyAuth.spotifyAccessToken);

        addPlaylistToDatabase(data);

    }

    function addPlaylistToDatabase(playlistPackage) {
        fetch("http://localhost:8080/api/spotify_data", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + serverAuth.user.token
            },
            body: JSON.stringify(playlistPackage)

        }).then(async response => {
            if (response.status === 201) {
                getAllPlaylists();
                showMessage('Playlist successfully added.', false);
            }
        })
    }

    function removePlaylistFromDatabase(playlistUri) {
        fetch(`http://localhost:8080/api/spotify_data/delete/${playlistUri}/${serverAuth.user.id}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + serverAuth.user.token
            }

        }).then(async response => {
            if (response.status === 204) {
                getAllPlaylists();
                showMessage('Playlist successfully deleted.', false);
            }
        })
    }

    function showMessage(message, isErrorMessage) {
        clearMessages();

        const messageContainer = document.getElementById("messages");

        messageContainer.innerHTML = messageContainer.innerHTML + `<p>${message}</p>`;

        if (isErrorMessage) {
            messageContainer.className = "alert alert-danger w-25 m-auto mb-4";
        } else {
            messageContainer.className = "alert alert-success w-25 m-auto mb-4";
        }
    }

    function clearMessages() {
        document.getElementById("messages").innerHTML = "";
        document.getElementById("messages").className = "";
    }

    return (
        <>
            
            <div id="messages" role="alert" style={{ minHeight: '40px' }}></div>
            <div className="manage table-background"> 
            <h2 className="page-header mt-4">Manage Playlists</h2>
                <div className="container-fluid manager ">
                    <div className="row">
                        <div className="col-6">
                            <table className="table">
                                <thead>
                                    <tr>
                                        <th colSpan={3}><div style={{minHeight: 3 + 'em'}}>Playlists in Your Spotify Catalog</div></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {playlists.map((playlist, index) => {
                                        const databasePlaylistUris = databasePlaylists.map((playlist) => (playlist.playlistUri));

                                        return (databasePlaylistUris.includes(playlist.uri)) ? null : <Playlist key={index} addPlaylist={transferPlaylistToTracking} p={playlist} index={index} />
                                    })}
                                </tbody>
                            </table>
                        </div>
                        <div className="col-6">
                            <table className="table">
                                <thead>
                                    <tr>
                                        <th colSpan={4} className="col-1"><div style={{minHeight: 3 + 'em'}}>Currently Tracked Playlists</div></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {databasePlaylists.map((databasePlaylist, index) => (
                                        <DatabasePlaylist key={index} pd={databasePlaylist} removePlaylistFromDatabase={removePlaylistFromDatabase} />
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}



export default ManagePlaylists;