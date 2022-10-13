import Playlist from "./Playlist/Playlist"
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import { useEffect, useState } from "react";

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});
function ManagePlaylists() {

    const [playlists, setPlaylists] = useState([]);

    function loadAllPlaylists() {
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

    spotifyApi.getUserPlaylists('31rrun55wnuo6elyf62p2g7mmc3m')
        .then(function (data) {
            console.log('Retrieved playlists', data.body);
        }, function (err) {
            console.log('Something went wrong!', err);
        });
    }

    return(
        <>
        {playlists.map( p => <Playlist key={p.id} playlistData={p} onPlaylistDeleted={onPlaylistDeleted} />) }
        </>
    )


export default ManagePlaylists;