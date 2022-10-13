import { React, useContext, useEffect, useState } from 'react';
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../../context/SpotifyAuthContext';

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

function useGetSpotifyData(playlistId){
    const spotifyAuth = useContext(SpotifyAuthContext); // get access to the spotify token stored in Context

    const [playlist, setPlaylist] = useState();
    const [databasePlaylistObject, setDatabasePlaylistObject] = useState();

    useEffect(() => { // make this request once on page load and whenever our token updates
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken); // allow the api helper to use the current token

        spotifyApi.getPlaylist("38cfqZXcGK4KPtDrGUNMkI")
            .then(function (data) {
                setPlaylist(data.body);
                createDatabasePlaylistObject(playlist);
            }, function (err) {
                console.log('Something went wrong!', err);
            })

    }, [spotifyAuth.spotifyAccessToken]);

    function createDatabasePlaylistObject(playlist) {
        if (!playlist) return;

        setDatabasePlaylistObject( {
            playlistUri: playlist.uri,
            playlistName: playlist.name,
            description: playlist.description,
            playlistUrl: playlist.external_urls.spotify,
            playlistImageLink: playlist.images[0].url,
            appUserId: false
        } );
    }

    return databasePlaylistObject;
}

export default useGetSpotifyData;