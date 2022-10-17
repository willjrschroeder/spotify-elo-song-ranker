import { useContext, useEffect, useState } from "react";
import AuthContext from "../../context/AuthContext";
import GameTrack from "./GameTrack";
import useCalculateEloTrackScores from "./useCalculateEloTrackScores";


function GameDisplay({ updateTrackScores, trackCollection }) {
    const serverAuth = useContext(AuthContext);
    const [winnerTrack, setWinnerTrack] = useState();
    const [loserTrack, setLoserTrack] = useState();
    const [currentTracks, setCurrentTracks] = useState(trackCollection);
    const [unselectableTracks, setUnselectableTracks] = useState([]);
    const [track1, setTrack1] = useState();
    const [track2, setTrack2] = useState();


    useEffect(() => {
        if (!winnerTrack || !loserTrack) return; // don't execute the block if a track is still null

        async function putScores() {
            await Promise.all( // trigger both puts at once
                fetch(`http://localhost:8080/api/track/${winnerTrack.eloScore}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + serverAuth.user.token
                    }
                }),

                fetch(`http://localhost:8080/api/track/${loserTrack.eloScore}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + serverAuth.user.token
                    }
                })
            );
        }

        putScores();
        setUnselectableTracks(unselectableTracks.filter((element) => element !== winnerTrack)); // removes the winner from unselectableTracks
        setUnselectableTracks(unselectableTracks.filter((element) => element !== loserTrack)); // removes the loser from unselectableTracks

    }, [winnerTrack]); // effect is triggered when the winner changes (I think it may change twice if we put winner and loser)

    // this function triggers a put request to update the scores, and it triggers two new random tracks to be picked
    // it is passed in winner and loser tracks with updated ELO scores
    function updateScores(winner, loser) { // probably triggered by onClick, or a left/right arrow key, etc. However we want user input to pick a winner

        // update the collection of tracks in memory to reflect the updated winner and loser scores
        setCurrentTracks(currentTracks.map((track) => {
            if (track.track_uri === winner.track_uri) {
                return winner;
            }
            if (track.track_uri === loser.track_uri) {
                return loser;
            }
            return track;
        }));

        setUnselectableTracks(...unselectableTracks, loser);
        setUnselectableTracks(...unselectableTracks, winner); // mark the tracks being updated as unselectable

        setLoserTrack(loser);
        setWinnerTrack(winner); // this should trigger the useEffect, which updates the tracks in the DB

        // randomly chooses new tracks
        let newTracks = null;
        while (!newTracks) { // continues to loop until there are enough tracks to select from (could be awaiting posts)
            newTracks = randomizeTracks(currentTracks, unselectableTracks);
        }

        // changes the tracks that are being displayed by the return of the function
        setTrack1(newTracks[0]);
        setTrack1(newTracks[1]);

    }

    // function that selects two random tracks to place on the game board
    // does not choose tracks if they have a pending put request to update their score
    // returns selected tracks if at least two tracks are available to be chosen
    // returns two null tracks if there are not at least two selectable tracks
    function randomizeTracks(trackCollection, unselectableTracks) {
        let selectableTracks = trackCollection.filter((element, index) => {
            return (!unselectableTracks.includes(index)); // returns true if the current track index is not included in the unselectableTracks array
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

        return [selectedTrack, selectedTrack2]; // returns the two randomly selected tracks
    }

    return (
        <>
            <GameTrack displayTrack={track1} otherTrack={track2} updateScores={updateScores} />
            <GameTrack displayTrack={track2} otherTrack={track1} updateScores={updateScores} />
        </>
    );
}

export default GameDisplay;