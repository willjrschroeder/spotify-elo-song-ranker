import { React, useContext, useEffect } from 'react';
import useAuth from './useAuth';
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';

const code = new URLSearchParams(window.location.search).get('code');

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

function CallbackPage() {
    const spotifyAccessToken = useAuth(code); //TODO: incorporate this token into React context
    const LOCAL_STORAGE_SPOTIFY_TOKEN_KEY = "spotifyToken";
    localStorage.setItem(LOCAL_STORAGE_SPOTIFY_TOKEN_KEY, spotifyAccessToken)

    const spotifyAuth = useContext(SpotifyAuthContext); // we should be using the token that is being updated within React context

    useEffect(() => { // make this request once on page load and whenevr our token updates
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAuthToken); // allow the api helper to use the current token

        spotifyApi.getMe()
            .then(function (data) {
                console.log('Some information about the authenticated user', data.body);
            }, function (err) {
                console.log('Something went wrong!', err);
            });

    }, [spotifyAuth.spotifyAuthToken])

    return (
        <div>
            <p>Success! Your Spotify account has successfully been linked</p>

        </div>
    );
}

export default CallbackPage;