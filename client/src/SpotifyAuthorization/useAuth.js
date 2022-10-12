import { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';

function useAuth(code) {
    const [accessToken, setAccessToken] = useState();
    const [refreshToken, setRefreshToken] = useState();
    const [expiresIn, setExpiresIn] = useState();
    const history = useHistory();

    // this method is run once every time a new authorization code is generated.
    // it makes a post request to the express server and is returned an accessToken, refreshToken, and expiration time
    useEffect(() => {
        axios.post('http://localhost:3001/api/spotify/login', { // makes a post request to our express server, passing in the authorization code
            code
        }).then(response => { // this response contains our access_token and our refresh_token
            console.log(response.data);
            setAccessToken(response.data.accessToken);
            setRefreshToken(response.data.refreshToken);
            setExpiresIn(response.data.expiresIn);

            window.history.pushState({}, null, "/"); // This removes the auth code from the URL. Doesn't do anything functionally, just makes things look a little cleaner
        }).catch(() => { // if there is an error during code authentication, the user is pushed back to the spotify OAuth page.
            // TODO: Not sure if we want to handle this error in another way
            // **THIS currently happens every time. This is due to React making two requests on every page load
            history.push("/spotify_login");
        })
    }, [code]); // performs every time a new authentication code is accquired


    // this method is run every time the refreshToken or expiresIn variable is updated
    // it makes a post request to the express server and is returned a new access token with a new expiration time
    useEffect(() => {
        if (!refreshToken || !expiresIn) return; // This makes sure a refresh request is not being made before these fields are defined
        const interval = setInterval(() => {
            axios.post('http://localhost:3001/api/spotify/refresh_token', { // makes a post request to our express server, passing in the refreshToken
                refreshToken
            }).then(response => { // this response contains our access_token and our refresh_token
                console.log(response.data);
                setAccessToken(response.data.accessToken);
                setExpiresIn(response.data.expiresIn);
            }).catch(() => { // if there is an error during code authentication, the user is pushed back to the spotify OAuth page.
                // TODO: Not sure if we want to handle this error in another way
                // **THIS currently happens every time. This is due to React making two requests on every page load
                history.push("/spotify_login");
            })
        }, (expiresIn - 60) * 1000); // this sets the token to refresh one minute before it will expire

        return () => clearInterval(interval);
    }, [refreshToken, expiresIn])

    return accessToken; // returns access token to the callback page
}

export default useAuth;