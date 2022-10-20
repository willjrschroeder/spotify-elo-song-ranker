import { useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import GameDisplay from "./GameMechanism/GameDisplay";
import GameTrack from "./GameMechanism/GameTrack";
import "./GameMechanism/GameTrack.css";

function EloGamePage() { //TODO: Don't let this be triggered if there are less than 2 tracks in the playlist

    const location = useLocation();
    const playlist = location.state;

    const serverAuth = useContext(AuthContext);
    const [currentTracks, setCurrentTracks] = useState();
    const [track1, setTrack1] = useState();
    const [track2, setTrack2] = useState();


    // Load a collection of tracks from the API. Perform once on page load
    useEffect(() => {
        fetch(`http://localhost:8080/api/track/playlist/${playlist.playlistUri}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + serverAuth.user.token
            }
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else (console.log(response))
            })
            .then(tracks => {
                setCurrentTracks(tracks);
                randomizeTracks(tracks);
            });

    }, []);


    // Update scores in the DB. Repeat every time a new winner and loser are chosen. Should maybe be a function, not useEffect
    // dependceny array issues from having the winner, loser, and unselectableTracks state variables referenced
    function sendScoresToDatabase(winner, loser) {
        if (!winner || !loser) return; // don't execute the block if a track is still null

        async function putScores(winner, loser) {
            await Promise.all( // trigger both puts at once
                fetch(`http://localhost:8080/api/track/`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + serverAuth.user.token
                    },
                    body: JSON.stringify(winner)
                }),

                fetch(`http://localhost:8080/api/track/`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + serverAuth.user.token
                    },
                    body: JSON.stringify(loser)
                })
            );
        }

        putScores(winner, loser);

    }


// this function triggers a put request to update the scores, and it triggers two new random tracks to be picked
// it is passed in winner and loser tracks with updated ELO scores
function updateScores(winner, loser) { // probably triggered by onClick in GameTrack

    // update the collection of tracks in memory to reflect the updated winner and loser scores
    const updatedTrackList = currentTracks.map((track) => {
        if (track.track_uri === winner.track_uri) {
            return winner;
        }
        if (track.track_uri === loser.track_uri) {
            return loser;
        }
        return track;
    });

    setCurrentTracks(updatedTrackList);

    sendScoresToDatabase(winner, loser);

    // randomly chooses new tracks
    let newTracks = null;
    while (!newTracks) { // continues to loop until there are enough tracks to select from (could be awaiting posts)
        newTracks = randomizeTracks(updatedTrackList);
    }

    // changes the tracks that are being displayed by the return of the function
    setTrack1(newTracks[0]);
    setTrack2(newTracks[1]);

}

// function that selects two random tracks to place on the game board
// does not choose tracks if they have a pending put request to update their score
// returns selected tracks if at least two tracks are available to be chosen
// returns two null tracks if there are not at least two selectable tracks
function randomizeTracks(trackCollection) {
    
    // select the first random track
    let selectedIndex = Math.floor(Math.random() * trackCollection.length);
    let selectedIndex2 = -1;
    let keep_going = true;

    // select the second random track, making sure it isn't the same index as the first track
    keep_going = true;
    while (keep_going) {
        selectedIndex2 = Math.floor(Math.random() * trackCollection.length);
        if (selectedIndex !== selectedIndex2) { // stop when the indices are not equal
            keep_going = false;
        }
    }
    let selectedTrack = trackCollection[selectedIndex];
    let selectedTrack2 = trackCollection[selectedIndex2];

    setTrack1(selectedTrack);
    setTrack2(selectedTrack2);
    return [selectedTrack, selectedTrack2]; // returns the two randomly selected tracks
}

if (track1 == null || track2 == null) return null; // wait to display tracks until they are selected


return (
    <>
    <h4 className="page-header">Which song do you like more?</h4>
    <div className="elo-page">
        <GameTrack displayTrack={track1} otherTrack={track2} updateScores={updateScores} />
        <GameTrack displayTrack={track2} otherTrack={track1} updateScores={updateScores} />
    </div>
    </>
)
}
export default EloGamePage;