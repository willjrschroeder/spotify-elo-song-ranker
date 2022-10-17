import { useEffect, useState } from "react";
import GameTrack from "./GameTrack";
import useCalculateEloTrackScores from "./useCalculateEloTrackScores";


function GameDisplay({ updateTrackScores, trackCollection }) {
    const [winnerTrack, setWinnerTrack] = useState();
    const [loserTrack, setLoserTrack] = useState();
    const [unselectableTracks, setUnselectableTracks] = useState([]);
    const [track1, setTrack1] = useState();
    const [track2, setTrack2] = useState();

    useEffect(() => {
        if (!winnerTrack || !loserTrack) return; // don't execute the block if a track is still null

        // const { updatedWinnerTrack, updatedLoserTrack } = useCalculateEloTrackScores(winnerTrack, loserTrack); // this probably lives in track, can't call inside useEffect

        // updateTrackScores(updatedWinnerTrack, updatedLoserTrack ); // sends updated scores back up to the parent EloGamePage
    }, [winnerTrack]); // effect is triggered when the winner changes (I think it may change twice if we put winner and loser)

    function updateScores(winner) { // probably triggered by onClick, or a left/right arrow key, etc. However we want user input to pick a winner
        if(winner.track_uri === track1.track_uri) {
            setLoserTrack(track2);
            setWinnerTrack(track1);
        }

        //TODO: change the loser to the opposite of winner - may need to happen first so the useEffect doesn't trigger early
        //TODO: use the setters to set the winner track to either track 1 or track 2 based on event - triggers the useEffect

        //TODO: call randomize tracks and update track display with track1/2 setters

    }

    // function that selects two random tracks to place on the game board
    // does not choose tracks if they have a pending put request to update their score
    // returns selected tracks if at least two tracks are available to be chosen
    // returns two null tracks if there are not at least two selectable tracks
    function randomizeTracks(trackCollection, unselectableTracks) {
        let selectableTracks = trackCollection.map((element, index) => {
            return (!unselectableTracks.includes(index)); // returns true if the current track index is not included in the unselectableTracks array
        });

        if (selectableTracks.length < 2) { // return nulls if there aren't two selectable tracks. Need to wait for some post requests to resolve
            return [null, null];
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
            <GameTrack track={track1} updateScores={updateScores} />
            <GameTrack track={track2} updateScores={updateScores} />
        </>
    );
}

export default GameDisplay;