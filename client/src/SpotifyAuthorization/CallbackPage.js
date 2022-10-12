import React from 'react';
import useAuth from './useAuth';

const code = new URLSearchParams(window.location.search).get('code');

function CallbackPage( {passSpotifyAuthToken} ) {
    const spotifyAccessToken = useAuth(code); //TODO: incorporate this token into React context
    passSpotifyAuthToken(spotifyAccessToken);

    return (
        <div>
            {spotifyAccessToken}
        </div>
    );
}

export default CallbackPage;