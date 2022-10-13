import Playlist from "../Playlist/Playlist"
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import { useEffect, useState, useContext } from "react";

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});
function ManagePlaylists() {
    const spotifyAuth = useContext(SpotifyAuthContext); 

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

    const [playlistPackage, setPlaylistPackage] = useState(null);

    function addPlaylistToDatabase(event) {


        event.preventDefault();

        fetch( "http://localhost:8080/api/playlists", {
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
    
        return(
        <>
            {playlists.map( p => <Playlist key={p.id} playlistData={p} onPlaylistAdd={addPlaylistToDatabase}  />) }
        </>
        )
    
    }



export default ManagePlaylists;