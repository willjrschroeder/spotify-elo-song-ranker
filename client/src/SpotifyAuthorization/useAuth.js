import { useState, useEffect } from 'react';
import axios from 'axios';

function useAuth(code) {
    const [accessToken, setAccessToken] = useState();
    const [refreshToken, setRefreshToken] = useState();
    const [expiresIn, setExpiresIn] = useState();

    useEffect(() => {
        axios.post('http://localhost:3001/spotify_login', { // makes a post request to our express server, passing in the authorization code
            code
        }).then(response => {
            console.log(response.data);
        })
    }, [code]); // performs every time a new authentication code is accquired
}

export default useAuth;