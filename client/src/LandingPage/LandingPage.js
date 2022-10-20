import './LandingPage.css';
import React, { useContext } from 'react';
import AuthContext from "../context/AuthContext";
import { Link } from 'react-router-dom';

function Home() {

    const auth = useContext(AuthContext);

    return (
        <>

            
                <div className='spotify-desc'>Millions of Songs Free on Spotify</div>
                <div className='land-login'><Link to="/login"><button className='register-btn'>Login</button></Link></div>
                <div className='sortify-desc'>Millions of Songs Ranked by Sortify</div>
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