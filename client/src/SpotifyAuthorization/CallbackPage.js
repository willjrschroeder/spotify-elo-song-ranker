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


    return (
        <div>
            <p>Success! Your Spotify account has successfully been linked</p>

        </div>
    );
}

export default CallbackPage;