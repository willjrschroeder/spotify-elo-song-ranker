import { React, useContext, useEffect, useState } from 'react';
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../../context/SpotifyAuthContext';

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

function useGetSpotifyData(playlistId){
    const spotifyAuth = useContext(SpotifyAuthContext); // get access to the spotify token stored in Context

    const [userPlaylists, setUserPlaylists] = useState();
    const [databasePlaylistObject, setDatabasePlaylistObject] = useState();

    useEffect(() => { // make this request once on page load and whenever our token updates
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken); // allow the api helper to use the current token

        spotifyApi.getUserPlaylists(playlistId)
            .then(function (data) {
                setUserPlaylists(data.body.items);
                createDatabasePlaylistObject(userPlaylists);
                console.log('Retrieved playlists', data.body.items);
            }, function (err) {
                console.log('Something went wrong!', err);
            })

    }, [spotifyAuth.spotifyAccessToken]);

    function createDatabasePlaylistObject(userPlaylists) {
           
    }

    return userPlaylists;
}

export default useGetSpotifyData;