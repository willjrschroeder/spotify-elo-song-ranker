import { React, useContext, useEffect, useState } from 'react';
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../../context/SpotifyAuthContext';

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

const useGetSpotifyData = ((playlistId) => {
    const spotifyAuth = useContext(SpotifyAuthContext); // get access to the spotify token stored in Context
    spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken); // allow the api helper to use the current token

    const [playlist, setPlaylist] = useState();
    const [databasePlaylistObject, setDatabasePlaylistObject] = useState();
    const [playlistTracks, setPlaylistTracks] = useState();
    const [databaseTracksObject, setDatabaseTracksObject] = useState();

    useEffect(() => { // make this request once on page load and whenever our token updates
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.getPlaylist(playlistId)
            .then(function (data) {
                setPlaylist(data.body);
                createDatabasePlaylistObject(playlist);
                return (data.body) // pass the playlist along to the next API call
            }, function (err) {
                console.log('Something went wrong!', err);
            });

        spotifyApi.getPlaylistTracks(playlistId)
            .then(function (data) {
                setPlaylistTracks(data.body.items);
                createDatabasePlaylistTracksObject(playlistTracks);
                return (data.body) // pass the playlist along to the next API call
            }, function (err) {
                console.log('Something went wrong!', err);
            });

    }, [spotifyAuth.spotifyAccessToken]);

    function createDatabasePlaylistObject(playlist) {
        if (!playlist) return;

        setDatabasePlaylistObject({
            playlistUri: playlist.uri,
            playlistName: playlist.name,
            description: playlist.description, //TODO: This is going to need to be recoded or something? Passed from the spotify API with ['] evaluating to [&#x27]"
            playlistUrl: playlist.external_urls.spotify,
            playlistImageLink: playlist.images[0].url,
            appUserId: false //TODO: we need to get this somehow. Can update the JWT to contain the user ID as a claim. Update in back end where JWT is created AND in front end where user is created(App.js)
        });
    }

    function createDatabasePlaylistTracksObject(playlistTracks) {
        if (!playlistTracks) return;
        
        console.log(playlistTracks); // TODO: this is an array containing tracks. Build [{track data}] and [{album data}]. need to get genre data elsewhere
    }

    return databasePlaylistObject; //TODO: we want to returnt the complete summaryObject
});

export default useGetSpotifyData;