import { useState } from "react";
import { useParams } from "react-router-dom";
import GameTrack from "./GameMechanism/GameTrack";

function EloGamePage() {

    const { playlistId } = useParams
    const [playlistTracks, setPlaylistTracks] = useState([]);

    function loadTracksByPlaylists(playlistId) {
            fetch( "http://localhost:8080/api/tracks/" + playlistId )
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
        <GameTrack></GameTrack>
        
        </>
    )
}
export default EloGamePage;