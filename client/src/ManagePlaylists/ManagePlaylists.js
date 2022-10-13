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

                setPlaylists(spotifyApi.getUserPlaylists(data.body.items))
            
        });
        }
        useEffect(
            () => {
                loadAllPlaylists();
            },
            []
        )
    
        return(
        <>
            {playlists.map( p => <Playlist key={p.id} playlistData={p} />) }
        </>
        )
    
    }



export default ManagePlaylists;