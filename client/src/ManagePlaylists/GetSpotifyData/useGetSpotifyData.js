import { React, useContext, useEffect, useState } from 'react';
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../../context/SpotifyAuthContext';

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

const useGetSpotifyData = ((playlistId) => {
    const spotifyAuth = useContext(SpotifyAuthContext); // get access to the spotify token stored in Context
    spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken); // allow the api helper to use the current token

    const [playlistSpotifyData, setPlaylistSpotifyData] = useState(); //track data object from the getPlaylist API endpoint data
    const [tracksArraySpotifyData, setTracksArraySpotifyData] = useState(); //array of tracks in a playlist from the getPlaylist API endpoint
    const [fullyHydratedArtistArray, setFullyHydratedArtistArray] = useState(); //(2D) array of arrays of artists on a track(from getPlaylist), fully hydrated from the getArtist API endpoint data
    const [spotifyData, setSpotifyData] = useState(); // container for the custom packaged Spotify data we are sending to the back end for writing to the DB

    // This contains the code to retrieve data from the API and store it in the state hooks, which are passed to the build helper methods
    // makes these requests when a new playlistId is passed in
    useEffect(() => {
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.getPlaylist(playlistId) // gets playlist data and tracks data for the passsed playlistId
            .then(function (data) {
                setPlaylistSpotifyData(data.body); // raw playlist data
                setTracksArraySpotifyData(data.body.tracks.items); //raw [tracks] data. Returns from the API with partially hydrated artist data
            }, function (err) {
                console.log('Something went wrong!', err);
            });

            //TODO: Need to wait to do this until the above API call is complete. async await?
            // request full artist data for each artist on a track
            for (const track of tracksArraySpotifyData.track) { // loop through each track in the array
                const artistsOnCurrentTrack = []; // array to store all of the artists who have credits on the current track

                for (const artist of track.artists) { // loop through each artist on the track
                    spotifyApi.getArtist(artist.id) // get the artist data from the Spotify API
                       .then(function (data) {
                            const packagedArtistData = buildArtistObject(data.body); // gets a packaged artist object, removing extraneous Spotify data
                            artistsOnCurrentTrack.push(packagedArtistData) // adds each artist to an array of artists on the track
                        }, function (err) {
                            console.log('Something went wrong!', err);
                        })
                }

                // add the array of artists on the current track to the array of artists tied to all of the tracks on the playlist
                setFullyHydratedArtistArray(...fullyHydratedArtistArray, artistsOnCurrentTrack); // a 2D array of artists. Order corresponds to tracks in the playlist
            }

            //TODO: Need to wait to do this until both API calls above are complete. Promise.all()?

            // call helper method to create the spotifyData object
            const playlistSummaryData = buildSpotifyDataObject(playlistSpotifyData, tracksArraySpotifyData, fullyHydratedArtistArray);
            setSpotifyData(playlistSummaryData);

    }, [playlistId]);

    //helper method to build the spotify data object containing 'playlist' and 'track' properties. This is the object that is returned from the hook
    function buildSpotifyDataObject(playlistSpotifyData, tracksArraySpotifyData, fullyHydratedArtistArray) {
        const spotifyData = {
            playlist: buildPlaylistObject(playlistSpotifyData),
            tracks: buildTracksArray(tracksArraySpotifyData, fullyHydratedArtistArray)
        }

        return spotifyData;
    }

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

        return playlistObject;
    }

    //helper method to build the 'tracks' property of the spotifyDataObject
    function buildTracksArray(tracksArraySpotifyData, fullyHydratedArtistArray) { // takes in Spotify data of the array of tracks tied to a playlist. Also takes in and a 2D array of fully hydrated artists for each track (both arrays in same order)

        const fullyHydratedTracksArray = [];

        for (let i = 0; i < tracksArraySpotifyData.length; i++) {
            let track = tracksArraySpotifyData[i].track; // gets the track from the spotify data array, ignoring the headers

            track.artists = fullyHydratedArtistArray[i]; //replace the partial artist data with the full artist data

            fullyHydratedTracksArray.push(track); // add the fully hydrated track to a new array
        }

        const tracksArray = fullyHydratedTracksArray.map(track => { // gets a packaged tracks array, removing extraneous Spotify data
            buildTrackObject(track);
        })

        return tracksArray; // returns the packaged tracks array
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

    return spotifyData; //TODO: we want to return the complete summaryObject
});

export default useGetSpotifyData;