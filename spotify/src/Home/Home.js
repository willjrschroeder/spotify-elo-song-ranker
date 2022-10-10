import './Home.css';
import React from 'react';
import {Link} from "react-router-dom";

function Home() {
    return <>
    <h1 className= "label" >Spotify Elo Home</h1>
    <div className="flex-container">
        <Link>Login</Link>
    </div>
    <div>About</div>
    </>
}
export default Home;