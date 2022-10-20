import { React, useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SpotifyWebApi from 'spotify-web-api-node';
import AuthContext from '../context/AuthContext';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import "./ConfirmationPage.css";

const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});

function ConfirmationPage() {
    const spotifyAuth = useContext(SpotifyAuthContext); // get access to the spotify token stored in Context
    const auth = useContext(AuthContext);

    const [userData, setUserData] = useState();

    useEffect(() => { // make this request once on page load and whenever our token updates
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken); // allow the api helper to use the current token

        spotifyApi.getMe()
            .then(function (data) {
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
                        <h2 className='welcome-mesg'>Welcome, {auth.user.display_name}</h2>
                        <img className='elo-image' src={userData.images[0].url} alt="User profile pulled down from the linked Spotify account"></img>
                        <h3 className='welcome-mesg'>Your Spotify Account, {userData.display_name}, successfully linked</h3>
                        <Link to="/home"><button className='link-btn'>Home</button></Link>
                    </div>
                    :
                    <p className='welcome-mesg'>Sorry, there was an error linking your Spotify account. Please try again.</p>
                }
            </div>
        </>
    );
}

export default ConfirmationPage;