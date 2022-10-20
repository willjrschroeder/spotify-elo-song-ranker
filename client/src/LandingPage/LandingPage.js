import './LandingPage.css';
import React, { useContext } from 'react';
import AuthContext from "../context/AuthContext";
import { Link } from 'react-router-dom';

function Home() {

    const auth = useContext(AuthContext);

    return (
        <>

            <div className='label'>
                <div>Rank your songs from Spotify</div>
                <div>Get Started now: <Link to="/register"><button>Register</button></Link></div>
                
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