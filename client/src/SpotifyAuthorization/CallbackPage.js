import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import useAuth from './useAuth';

const code = new URLSearchParams(window.location.search).get('code');

function CallbackPage() {
    const spotifyAccessToken = useAuth(code); //TODO: incorporate this token into React context
    const LOCAL_STORAGE_SPOTIFY_TOKEN_KEY = "spotifyToken";
    localStorage.setItem(LOCAL_STORAGE_SPOTIFY_TOKEN_KEY, spotifyAccessToken)

    const history = useHistory();

    useEffect( () => {
        if(!spotifyAccessToken) return;

        history.push("/spotify/confirmation"); //TODO: We only get pushed to the confirmation page after pressing the authorization button twice
    }, [spotifyAccessToken]);

    return (
        <div>
            <p>Success! Your Spotify account has successfully been linked</p>

        </div>
    );
}

export default CallbackPage;