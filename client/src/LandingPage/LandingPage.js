import './LandingPage.css';
import React, { useContext } from 'react';
import AuthContext from "../context/AuthContext";

function Home() {

    const auth = useContext(AuthContext);

    return (
        <>

            <div className='label'>
                <div>Rank your songs from Spotify here!</div>
                
                </div>

            {auth.user && (
                <div className="flex-container">
                    Welcome {auth.user.username}!
                    <button className='login' onClick={() => auth.logout()}>logout</button>
                </div>
            )}
        </>
    );
}
export default Home;