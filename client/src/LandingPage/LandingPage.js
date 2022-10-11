import './LandingPage.css';
import React, {useContext} from 'react';
import {Link} from "react-router-dom";
import AuthContext from "../context/AuthContext";

function Home() {

    const auth = useContext(AuthContext);

    return <>
    <div className='flex-container'>
    <h1 className= "label" >Spotify Elo Home</h1>
    </div>
    {auth.user ? (
    <>
    <h2>Your are logged in</h2>
    </>
    ) : (
        <>
            <div className="flex-container">
                <Link className='login' to = "/login">Login</Link>
            </div>
        </>
    )}
    {auth.user && (
            <div className="flex-container">
            Welcome {auth.user.username}!
            <button className='login' onClick = {() => auth.logout()}>logout</button>
        </div>
    )}
    <div className='flex-container'>About</div>
    </>
}
export default Home;