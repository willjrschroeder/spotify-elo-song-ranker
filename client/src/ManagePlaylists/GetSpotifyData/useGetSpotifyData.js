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

    function buildPlaylistObject(playlistSpotifyData) {

        const playlistObject = {
            playlistUri: playlistSpotifyData.uri,
            playlistName: playlistSpotifyData.name,
            description: playlistSpotifyData.description, //TODO: This is going to need to be recoded or something? Passed from the spotify API with ['] evaluating to [&#x27]"
            playlistUrl: playlistSpotifyData.external_urls.spotify,
            playlistImageLink: playlistSpotifyData.images[0].url,
            appUserId: false //TODO: we need to get this somehow. Can update the JWT to contain the user ID as a claim. Update in back end where JWT is created AND in front end where user is created(App.js)
        }
    }

    function buildTracksArray(tracksArraySpotifyData){ // takes in Spotify data of the array of tracks tied to a playlist
        const tracksArray = tracksArraySpotifyData
    }

    function buildTrackObject(trackSpotifyData, fullArtistArray) { // takes in Spotify data of a track object and an array of fully hydrated artists on the track
        const track = trackSpotifyData.track // track data, ignoring the headers

        const trackObject = {
            trackUri: track.uri,
            title: track.name,
            trackDuration: track.duration_ms,
            popularityNumber: track.popularity,
            spotifyUrl: trackSpotifyData.external_urls.spotify,
            thirtySecondPreviewUrl: track.preview_url,
            album: {
                albumUri: track.album.uri,
                albumName: track.album.name,
                releaseDate: track.album.release_date,
                albumImageLink: track.album.images[0].url,
                spotifyUrl: track.album.external_urls.spotify
            },
            artists: fullArtistArray.map(artist => {
                buildArtistObject(artist);
            })
        }
        return trackObject;
    }

    function buildArtistObject(artistSpotifyData) {
        const artistObject = {
            artistUri: artistSpotifyData.uri,
            artistName: artistSpotifyData.name,
            spotifyUrl: artistSpotifyData.external_urls.spotify,
            artistImageLink: artistSpotifyData.images[0].url,
            genres: artistSpotifyData.genres // string array of genres
        }

        return artistObject;
    }

    return spotifyDataObject; //TODO: we want to returnt the complete summaryObject
});

export default useGetSpotifyData;