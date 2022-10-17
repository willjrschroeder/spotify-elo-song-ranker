import { useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import GameDisplay from "./GameMechanism/GameDisplay";

function EloGamePage() { //TODO: Don't let this be triggered if there are less than 2 tracks in the playlist

    const location = useLocation();
    const playlist = location.state;
    const [playlistTracks, setPlaylistTracks] = useState([]);
    const serverAuth = useContext(AuthContext);

    useEffect( () => {
        fetch( `http://localhost:8080/api/track/${playlist.playlistUri}`, {
            method:"GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + serverAuth.user.token
            }})
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else( console.log( response ) )
        } )
        .then( tracks => {
            setPlaylistTracks( tracks );
        });

    }, []);

    return(
        <>
        <h1>Rank your songs</h1>
        <GameDisplay trackCollection = {playlistTracks}></GameDisplay>
        
        </>
    )
}
export default EloGamePage;