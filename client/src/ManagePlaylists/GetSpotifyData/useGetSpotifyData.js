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
                //createDatabasePlaylistObject(playlist);
                return (data.body) // pass the playlist along to the next API call
            }, function (err) {
                console.log('Something went wrong!', err);
            });

        spotifyApi.getPlaylistTracks(playlistId)
            .then(function (data) {
                setPlaylistTracks(data.body.items);
                //createDatabasePlaylistTracksObject(playlistTracks);
                return (data.body) // pass the playlist along to the next API call
            }, function (err) {
                console.log('Something went wrong!', err);
            });

    }, [spotifyAuth.spotifyAccessToken]);


    //helper method to build the 'playlist' property of the spotifyDataObject
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

    //helper method to build the 'tracks' property of the spotifyDataObject
    function buildTracksArray(tracksArraySpotifyData, fullyHydratedArtistArray){ // takes in Spotify data of the array of tracks tied to a playlist. Also takes in and a 2D array of fully hydrated artists for each track (both arrays in same order)

        const fullyHydratedTracksArray = [];

        for (let i = 0; i < tracksArraySpotifyData.length; i++) {
            let track = tracksArraySpotifyData[i].track; // gets the track from the spotify data array, ignoring the headers

            track.artists = fullyHydratedArtistArray[i]; //replace the partial artist data with the full artist data

            fullyHydratedTracksArray.push( track ); // add the fully hydrated track to a new array
        }

        const tracksArray = fullyHydratedTracksArray.map(track => {
            buildTrackObject(track);
        })
    }

    // helper method to build the individual track objects that go in the 'tracks' property of the spotifyDataObject
    function buildTrackObject(fullyHydratedTrackData) { // takes in Spotify data of a track object, but the artist property has been replaced with a fully hydrated artist
        const track = fullyHydratedTrackData // easier naming for obj construction, while keeping a descriptive name for the input parameter

        const trackObject = {
            trackUri: track.uri,
            title: track.name,
            trackDuration: track.duration_ms,
            popularityNumber: track.popularity,
            spotifyUrl: track.external_urls.spotify,
            thirtySecondPreviewUrl: track.preview_url,
            album: {
                albumUri: track.album.uri,
                albumName: track.album.name,
                releaseDate: track.album.release_date,
                albumImageLink: track.album.images[0].url,
                spotifyUrl: track.album.external_urls.spotify
            },
            artists: track.artists
        }
        return trackObject;
    }

    // helper method to build the array of fully hydrated artists
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

    //return spotifyDataObject; //TODO: we want to return the complete summaryObject
});

export default useGetSpotifyData;