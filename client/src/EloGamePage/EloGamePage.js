import { useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import GameDisplay from "./GameMechanism/GameDisplay";
import GameTrack from "./GameMechanism/GameTrack";

function EloGamePage() { //TODO: Don't let this be triggered if there are less than 2 tracks in the playlist

    const location = useLocation();
    const playlist = location.state;

    const serverAuth = useContext(AuthContext);
    const [currentTracks, setCurrentTracks] = useState();
    const [unselectableTracks, setUnselectableTracks] = useState([]);
    const [track1, setTrack1] = useState();
    const [track2, setTrack2] = useState();


    // Load a collection of tracks from the API. Perform once on page load
    useEffect(() => {
        fetch(`http://localhost:8080/api/track/${playlist.playlistUri}`, {
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
                randomizeTracks(tracks, unselectableTracks);
            });

    }, []);


    // Update scores in the DB. Repeat every time a new winner and loser are chosen. Should maybe be a function, not useEffect
    // dependceny array issues from having the winner, loser, and unselectableTracks state variables referenced
    function sendScoresToDatabase(winner, loser) {
        if (!winner || !loser) return; // don't execute the block if a track is still null

        async function putScores(winner, loser) {
            await Promise.all( // trigger both puts at once
                fetch(`http://localhost:8080/api/track/${winner.eloScore}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + serverAuth.user.token
                    }
                }),

                fetch(`http://localhost:8080/api/track/${loser.eloScore}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + serverAuth.user.token
                    }
                })
            );
        }

        putScores(winner, loser);
        setUnselectableTracks(unselectableTracks.filter((element) => element !== winner)); // removes the winner from unselectableTracks
        setUnselectableTracks(unselectableTracks.filter((element) => element !== loser)); // removes the loser from unselectableTracks

    }


// this function triggers a put request to update the scores, and it triggers two new random tracks to be picked
// it is passed in winner and loser tracks with updated ELO scores
function updateScores(winner, loser) { // probably triggered by onClick in GameTrack

    // update the collection of tracks in memory to reflect the updated winner and loser scores
    setCurrentTracks(currentTracks.map((track) => {
        if (track.trackUri === winner.track_uri) {
            return winner;
        }
        if (track.track_uri === loser.track_uri) {
            return loser;
        }
        return track;
    }));

    setUnselectableTracks(...unselectableTracks, loser);
    setUnselectableTracks(...unselectableTracks, winner); // mark the tracks being updated as unselectable

    sendScoresToDatabase(winner, loser);

    // randomly chooses new tracks
    let newTracks = null;
    while (!newTracks) { // continues to loop until there are enough tracks to select from (could be awaiting posts)
        newTracks = randomizeTracks(currentTracks, unselectableTracks);
    }

    // changes the tracks that are being displayed by the return of the function
    setTrack1(newTracks[0]);
    setTrack2(newTracks[1]);

}

// function that selects two random tracks to place on the game board
// does not choose tracks if they have a pending put request to update their score
// returns selected tracks if at least two tracks are available to be chosen
// returns two null tracks if there are not at least two selectable tracks
function randomizeTracks(trackCollection, unselectableTracks) {
    let selectableTracks = trackCollection.filter((element) => {
        return (!unselectableTracks.includes(element)); // returns true if the current track is not included in the unselectableTracks array
    });

    // returns null if there aren't two selectable tracks. Need to wait for put requests to resolve
    if (selectableTracks.length < 2) {
        return null;
    }

    // select the first random track
    let selectedIndex = Math.floor(Math.random() * selectableTracks.length);
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
        <GameTrack displayTrack={track1} otherTrack={track2} updateScores={updateScores} />
        <GameTrack displayTrack={track2} otherTrack={track1} updateScores={updateScores} />

    </>
)
}
export default EloGamePage;