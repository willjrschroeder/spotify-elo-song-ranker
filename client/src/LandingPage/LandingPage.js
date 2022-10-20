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
                </div>
                <div className='land-register'><Link to="/register"><button className='register-btn'>Register</button></Link></div>

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