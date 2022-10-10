import './Home.css';
import React from 'react';
import {Link} from "react-router-dom";

function Home() {




    
    return <>
    <div className='flex-container'>
    <h1 className= "label" >Spotify Elo Home</h1>
    </div>
    <div className="flex-container">
    
    
        <Link className='login' to = "/login">Login</Link>
    </div>
    <div className='flex-container'>About</div>
    </>
}
export default Home;