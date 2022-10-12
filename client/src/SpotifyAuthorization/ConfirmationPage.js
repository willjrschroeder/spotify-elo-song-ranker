import { React, useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

function ConfirmationPage() {
    const spotifyAuth = useContext(SpotifyAuthContext); // get access to the spotify token stored in Context

    const [userData, setUserData] = useState();

    useEffect(() => { // make this request once on page load and whenever our token updates
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken); // allow the api helper to use the current token

        spotifyApi.getMe()
            .then(function (data) {
                console.log(data.body);
                setUserData(data.body);
            }, function (err) {
                console.log('Something went wrong!', err);
            });

    }, [spotifyAuth.spotifyAccessToken])

    return (
        <>
            <div>
                {userData ?
                    <div>
                        <h2>Welcome, {userData.display_name}</h2>
                        <img src={userData.images[0].url}></img>
                        <h3>Your Spotify Account was successfully linked</h3>
                        <Link to="/home"><button className='btn'>Home</button></Link>
                    </div>
                    :
                    null
                }
            </div>
        </>
    );
}

export default ConfirmationPage;