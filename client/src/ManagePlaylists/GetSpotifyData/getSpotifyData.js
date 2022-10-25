import SpotifyWebApi from 'spotify-web-api-node';

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

const getSpotifyData = ((playlistId, userId, spotifyAccessToken) => {

    spotifyApi.setAccessToken(spotifyAccessToken); // allow the api helper to use the current token

    let spotifyData; // container for the custom packaged Spotify data we are sending to the back end for writing to the DB

    // Retrieve data from the API
    // state is then passed to the builder helper methods to create the spotifyData object
    // repeats this process when a new playlistId is passed in
    //useEffect(() => {

    const requestData = async () => {
        // gets playlist data and tracks data for the passsed playlistId
        const data = await spotifyApi.getPlaylist(playlistId)
        let playlistSpotifyData = data.body; // raw playlist data
        let tracksArraySpotifyData = data.body.tracks.items; //raw [tracks] data. Returns from the API with partially hydrated artist data

        let artistFetches = []; // this stores all of the fetches that the Promise.all will be making. Preserves order
        for (let j = 0; j < tracksArraySpotifyData.length; j++) {
            const track = tracksArraySpotifyData[j].track;// gets rid of header junk
            let currentArtists = [];

            for (let i = 0; i < track.artists.length; i++) { // loops through all the artists on the track
                const artist = track.artists[i];

                const artistFetch = spotifyApi.getArtist(artist.id); // fetch for the current artist
                currentArtists.push(artistFetch); // adds the current fetch to the array of artists on the track
            }

            artistFetches = [...artistFetches, currentArtists]; // adds artist array to the array tied to track order
            currentArtists = [];
        }

        let fullyHydratedArtistArray = []; //array to store the packaged artist objects

        await Promise.all(artistFetches.map(innerPromiseArray => { // call promise.all on the 2D array of fetches
            return Promise.all(innerPromiseArray)
        }))
            .then(async (artistData) => {

                for (const artists of artistData) {
                    let artistsOnCurrentTrack = [];
                    for (const artist of artists) {
                        const artistObject = buildArtistObject(artist.body); // packages JSON return into artist objects
                        artistsOnCurrentTrack.push(artistObject)
                    }
                    fullyHydratedArtistArray = [...fullyHydratedArtistArray, artistsOnCurrentTrack]; // builds the 2D array of artists for each track
                    artistsOnCurrentTrack = [];

                }
            });

        // calls helper method to create the spotifyData object
        // needs to be performed AFTER state variables are set with API data
        return buildSpotifyDataObject(playlistSpotifyData, tracksArraySpotifyData, fullyHydratedArtistArray);
    }

    spotifyData = requestData();


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
            description: playlistSpotifyData.description,
            playlistUrl: playlistSpotifyData.external_urls.spotify,
            playlistImageLink: playlistSpotifyData.images[0].url,
            appUserId: userId
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
            track_uri: track.uri,
            title: track.name,
            appUserId: userId,
            trackDuration: track.duration_ms,
            popularityNumber: track.popularity,
            spotifyUrl: track.external_urls.spotify,
            previewUrl: track.preview_url,
            albums: [{
                albumUri: track.album.uri,
                albumName: track.album.name,
                releaseDate: '2022-10-22', // some of these are formatted as '1974-11' - can't parse into java Date obj. Can be fixed, won't track for now
                albumImageLink: track.album.images[0].url,
                spotifyUrl: track.album.external_urls.spotify
            }],
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
            artistImageLink: (artistSpotifyData.images.length === 0 ? null : artistSpotifyData.images[0].url),
            genres: artistSpotifyData.genres // string array of genres
        }

        return artistObject;
    }

    return spotifyData;
});

export default getSpotifyData;