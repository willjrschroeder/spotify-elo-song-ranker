import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import useAuth from './useAuth';
import axios from 'axios';

const code = new URLSearchParams(window.location.search).get('code');

function CallbackPage({ setSpotifyToken }) {
    //const spotifyAccessToken = useAuth(code);
    const LOCAL_STORAGE_SPOTIFY_TOKEN_KEY = "spotifyToken";
    //localStorage.setItem(LOCAL_STORAGE_SPOTIFY_TOKEN_KEY, spotifyAccessToken)

    const history = useHistory();

    useEffect( () => {
        //if(!spotifyAccessToken) return;

        axios.post('http://localhost:3001/api/spotify/login', { // makes a post request to our express server, passing in the authorization code
            code
        }).then(response => { // this response contains our access_token and our refresh_token
            setSpotifyToken(response.data.accessToken);
            
            // setAccessToken(response.data.accessToken);
            // setRefreshToken(response.data.refreshToken);
            // setExpiresIn(response.data.expiresIn);

            history.push("/spotify/confirmation"); // This removes the auth code from the URL. Doesn't do anything functionally, just makes things look a little cleaner
        }).catch(() => { // if there is an error during code authentication, the user is pushed back to the spotify OAuth page.
            // TODO: Not sure if we want to handle this error in another way
            // **THIS currently happens every time. This is due to React making two requests on every page load
            history.push("/spotify");
        })

         //TODO: We only get pushed to the confirmation page after pressing the authorization button twice
    }, []);

    return (
        <div>
            <p>Success! Your Spotify account has successfully been linked</p>

        </div>
    );
}

export default CallbackPage;