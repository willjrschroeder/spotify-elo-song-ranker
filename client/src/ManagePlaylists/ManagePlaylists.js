import Playlist from "../Playlist/Playlist"
import "./ManagePlaylists.css"
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import { useEffect, useState, useContext } from "react";
import AuthContext from "../context/AuthContext";
import DatabasePlaylist from "../Playlist/DatabasePlaylists";



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
            .catch(showMessage('Error connecting to Spotify server.', true));
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
            return Promise.reject(await response.json());
        })
            .catch(errorList => {
                showMessage('Could not connect to server.', true);
            });
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
    }

    return (<>
        <div className="header">Manage Playlists</div>
        <div id="messages" role="alert" style={{ minHeight: '40px' }}></div>
        <div class="container-fluid w-50">
            <div class="row">
            <div class="col">
                    <table className="table">
                        <thead className="thead-dark">
                            <tr className="table-rows">
                                <th colSpan={2}>Spotify Playlists</th>
                            </tr>
                        </thead>
                        <tbody>
                            {playlists.map((playlist, index) => {
                                const databasePlaylistUris = databasePlaylists.map((playlist) => (playlist.playlistUri));

                                return (databasePlaylistUris.includes(playlist.uri)) ? null : <Playlist key={index} addPlaylist={addPlaylistToDatabase} p={playlist} index={index} />
                            })}
                        </tbody>
                    </table>
                </div>
                <div class="col">
                    <table className="table">
                        <thead className="thead-dark">
                            <tr>
                                <th colSpan={2}>Currently Tracked Playlists</th>
                            </tr>
                        </thead>
                        <tbody>
                            {databasePlaylists.map((databasePlaylist, index) => (
                                <DatabasePlaylist key={index} pd={databasePlaylist} />
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </>
    )
}



export default ManagePlaylists;