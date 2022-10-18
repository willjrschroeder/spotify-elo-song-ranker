import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';

const code = new URLSearchParams(window.location.search).get('code');

function CallbackPage({ setSpotifyToken }) {
    const LOCAL_STORAGE_SPOTIFY_TOKEN_KEY = "spotifyToken";

    const history = useHistory();

    useEffect(() => {
        axios.post('http://localhost:3001/api/spotify/login', { code }) // makes a post request to our express server, passing in the authorization code

            .then(response => { // this response contains our access_token and our refresh_token
                setSpotifyToken(response.data.accessToken);
                localStorage.setItem(LOCAL_STORAGE_SPOTIFY_TOKEN_KEY, response.data.accessToken);

                history.push("/spotify/confirmation");
            }).catch(() => { // if there is an error during code authentication, the user is pushed back to the spotify OAuth page.
                history.push("/spotify");
            })

    }, []);

    return (
        <>
        </>
    );
}

export default CallbackPage;