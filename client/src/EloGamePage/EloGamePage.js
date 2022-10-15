import { useState } from "react";
import useAuth from "../SpotifyAuthorization/useAuth";
import GameDisplay from "./GameDisplay";

function EloGamePage(playlistId) {
    const serverAuth = useAuth();

    const [playlistTracks, setPlaylistTracks] = useState([]);

    function loadTracksByPlaylists(playlistId) { //TODO: place this in a useEffect by itself - just want this to happen once
            fetch( `http://localhost:8080/api/track/${playlistId}/${serverAuth.user.id}` )
            .then( response => {
                if( response.status === 200 ) {
                    return response.json();
                } else( console.log( response ) )
            } )
            .then( playlistTracks => {
                setPlaylistTracks( playlistTracks );
            });
    }

    function displayRandomTracks() {
        //TODO: call on page load and everytime after a winner is chosen - probably a use effect (maybe update track scores goes in here too?)
        //TODO: tracks that are currently awaiting a response from the Java server should be made unselectable somehow
        //TODO: when the response from the server resolves, make the tracks selectable again
    }

    function updateTrackScores(winningTrack, losingTrack){ // takes an input of tracks with updated ELO scores
        //TODO: make a post request to the server and await the response

    }


    return(
        <>
        <h1>Rank your songs</h1>

        <div>
            <GameDisplay updateTrackScores={updateTrackScores} track1={null} track2={null}/> {/* GameDisplay is the two tracks combined, along with a mechanism to choose the winner */}
        </div>
        <div>
            
        </div>
        </>
    )
}
export default EloGamePage;