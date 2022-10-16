import { useContext, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import GameDisplay from "./GameMechanism/GameDisplay";
import GameTrack from "./GameMechanism/GameTrack";

function EloGamePage() {

    const location = useLocation();
    const { playlist } = location.state;
    const [playlistTracks, setPlaylistTracks] = useState([]);
    const serverAuth = useContext(AuthContext);

    function loadTracksByPlaylists(playlistId) {
            fetch( "http://localhost:8080/api/track/" + playlist.playlistUri, {
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
            .then( playlistTracks => {
                setPlaylistTracks( playlistTracks );
            });
    }




    return(
        <>
        <h1>Rank your songs</h1>
        <GameDisplay playlistTracks = {playlistTracks}></GameDisplay>
        
        </>
    )
}
export default EloGamePage;