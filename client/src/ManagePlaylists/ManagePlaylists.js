import Playlist from "../Playlist/Playlist"
import "./ManagePlaylists.css"
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import { useEffect, useState, useContext, useRef } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../context/AuthContext";



const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});
function ManagePlaylists() {


    const spotifyAuth = useContext(SpotifyAuthContext); 
    const serverAuth = useContext(AuthContext);
    
    const userId = serverAuth.user.id;

    const [playlists, setPlaylists] = useState([]);

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
        useEffect(
            () => {
                loadAllPlaylists();
            },
            []
        )

    

    function addPlaylistToDatabase(playlistPackage) {
        console.log(playlistPackage);

        fetch( "http://localhost:8080/api/spotify_data", {
            method:"POST",
            body: JSON.stringify(playlistPackage),
            headers: {
                "Content-Type": "application/json"
            }
        }).then( async response => {
            if( response.status === 201 ) {
                return response.json();
            } 
                return Promise.reject( await response.json() );
        })
        .catch( errorList => {
            if( errorList instanceof TypeError ){
                console.log( "Could not connect to api.");
            } else {
            }
        });

    }
    
        return(<>
        <Link to="/home" className="login">Home</Link>
        <div className="container">
            {playlists.map( (p, index)=> (
                <Playlist key={index} addPlaylist = {addPlaylistToDatabase} p={p} index={index}/>
                )
            )}
        </div>
        </>
        )
    }



export default ManagePlaylists;