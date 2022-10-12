import React from 'react';
import useAuth from './useAuth';

const code = new URLSearchParams(window.location.search).get('code');

function CallbackPage() {
    const spotifyAccessToken = useAuth(code); //TODO: incorporate this token into React context
    const LOCAL_STORAGE_SPOTIFY_TOKEN_KEY = "spotifyToken";
    localStorage.setItem(LOCAL_STORAGE_SPOTIFY_TOKEN_KEY, spotifyAccessToken)

    return (
        <div>
            {spotifyAccessToken}
        </div>
    );
}

export default CallbackPage;