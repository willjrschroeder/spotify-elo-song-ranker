import Playlist from "../Playlist/Playlist"
import "./ManagePlaylists.css"
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import { useEffect, useState, useContext } from "react";
import { Link } from "react-router-dom";
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
                console.log(databasePlaylists);
                setDatabasePlaylists(databasePlaylists);
            });
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
                return response.json(); //TODO: add success handling
            }
            return Promise.reject(await response.json());
        })
            .catch(errorList => {
                if (errorList instanceof TypeError) {
                    console.log("Could not connect to api.");
                } else {
                } //TODO: add error handling
            });

    }

    return (<>
        <div className="header">Manage Playlists</div>
        <div className="flex-container">
            <div>
                <table className="table">
                    <thead className="thead-dark">
                        <tr>
                            <th></th>
                            <th>Name</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {databasePlaylists.map((databasePlaylist, index) => (
                            <DatabasePlaylist key={index} pd={databasePlaylist} />
                        ))}
                    </tbody>
                </table>
            </div>
            <div >
                <table className="table table-bordered">
                    <thead className="thead-dark">
                        <tr className="table-rows">
                            <th></th>
                            <th>Name</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody className="tbl-body">
                        {playlists.map((playlist, index) => {
                            const databasePlaylistUris = databasePlaylists.map((playlist) => (playlist.playlistUri));

                            return (databasePlaylistUris.includes(playlist.uri)) ? null : <Playlist key={index} addPlaylist={addPlaylistToDatabase} p={playlist} index={index} />
                        })}
                    </tbody>
                </table>

            </div>
        </div>
    </>
    )
}



export default ManagePlaylists;