import { React, useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import useGetSpotifyData from '../ManagePlaylists/GetSpotifyData/useGetSpotifyData'; //TODO: delete this once dont testing GetSpotifyData

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
                setUserData(data.body);
            }, function (err) {
                console.log('Something went wrong!', err);
            });

        spotifyApi.getUserPlaylists('31rrun55wnuo6elyf62p2g7mmc3m')
            .then(function (data) {
            }, function (err) {
                console.log('Something went wrong!', err);
            })

    }, [spotifyAuth.spotifyAccessToken])


    const returnedData = useGetSpotifyData('38cfqZXcGK4KPtDrGUNMkI'); //TODO: delete this once done testing GetSpotifyData
    console.log(returnedData);
    function testGetSpotifyData(){ //TODO: delete this once dont testing GetSpotifyData
        console.log(returnedData);
    }

    return (
        <>
            <div>
                {userData ?
                    <div>
                        <h2>Welcome, {userData.display_name}</h2>
                        <img src={userData.images[0].url} alt="User profile pulled down from the linked Spotify account"></img>
                        <h3>Your Spotify Account was successfully linked</h3>
                        <Link to="/home"><button className='btn'>Home</button></Link>
                        <button onClick={testGetSpotifyData}>Test GetSpotifyData</button> {/* TODO: delete this once dont testing GetSpotifyData */}
                    </div>
                    :
                    null
                }
            </div>
        </>
    );
}

export default ConfirmationPage;