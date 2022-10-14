import { useContext, useEffect, useState } from 'react';
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../../context/SpotifyAuthContext';

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

const useGetSpotifyData = ((playlistId, userId) => {
    const spotifyAuth = useContext(SpotifyAuthContext); // get access to the spotify token stored in Context
    spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken); // allow the api helper to use the current token

    const [spotifyData, setSpotifyData] = useState(); // container for the custom packaged Spotify data we are sending to the back end for writing to the DB

    // Retrieve data from the API
    // state is then passed to the builder helper methods tp create the spotifyData object
    // repeats this process when a new playlistId is passed in
    //TODO: add a tracker so that if a new playlist ID is passed in before the method completes, the second request is cancelled
    useEffect(() => {
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        const requestData = async () => {
             // gets playlist data and tracks data for the passsed playlistId
            const data = await spotifyApi.getPlaylist(playlistId)
            let playlistSpotifyData = data.body; // raw playlist data
            let tracksArraySpotifyData = data.body.tracks.items; //raw [tracks] data. Returns from the API with partially hydrated artist data
            let fullyHydratedArtistArray = [];
            // gets fully hydrated artist data for each track
            for (const trackWithHeaders of tracksArraySpotifyData) { // loop through each track in the array
                let artistsOnCurrentTrack = []; // array to store all of the artists who have credits on the current track
                const track = trackWithHeaders.track;

                for (const artist of track.artists) { // loop through each artist on the track
                    const artistData = await spotifyApi.getArtist(artist.id) // get the artist data from the Spotify API

                    const packagedArtistData = buildArtistObject(artistData.body); // gets a packaged artist object, removing extraneous Spotify data
                    artistsOnCurrentTrack.push(packagedArtistData) // adds each artist to an array of artists on the track                    
                }

                // add the array of artists on the current track to the array of artists tied to all of the tracks on the playlist
                fullyHydratedArtistArray = [...fullyHydratedArtistArray, artistsOnCurrentTrack];  // a 2D array of artists. Order corresponds to tracks in the playlist
                artistsOnCurrentTrack = [];
            }

            // calls helper method to create the spotifyData object
            // needs to be performed AFTER state variables are set with API data
            const playlistSummaryData = buildSpotifyDataObject(playlistSpotifyData, tracksArraySpotifyData, fullyHydratedArtistArray);
            setSpotifyData(playlistSummaryData);
        }

        requestData();

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
            appUserId: userId //TODO: we need to get this somehow. Can update the JWT to contain the user ID as a claim. Update in back end where JWT is created AND in front end where user is created(App.js)
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
            return buildTrackObject(track);
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

    return spotifyData;
});

export default useGetSpotifyData;