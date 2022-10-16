import { useEffect, useState } from "react";
import GameTrack from "./GameTrack";
import useCalculateEloTrackScores from "./useCalculateEloTrackScores";


function GameDisplay({ updateTrackScores, track1, track2 }){
    const [winnerTrack, setWinnerTrack] = useState();
    const [loserTrack, setLoserTrack] = useState();

    useEffect( () => {
        if(!winnerTrack || !loserTrack) return; // don't execute the block if a track is still null

        // const { updatedWinnerTrack, updatedLoserTrack } = useCalculateEloTrackScores(winnerTrack, loserTrack); // gets tracks with new ELO scores

        // updateTrackScores(updatedWinnerTrack, updatedLoserTrack ); // sends updated scores back up to the parent EloGamePage
    }, 
    [winnerTrack]); // effect is triggered when the winner changes (I think it may change twice if we put winner and loser)

    function chooseWinner(event, winner){ // probably triggered by onClick, or a left/right arrow key, etc. However we want user input to pick a winner 
        //TODO: change the loser to the opposite of winner - may need to happen first so the useEffect doesn't trigger early
        //TODO: use the setters to set the winner track to either track 1 or track 2 based on event - triggers the useEffect

    }

    return(
        <>
            <GameTrack track1={track1}/>
            <GameTrack track2={track2}/>
        </>
    );
}

export default GameDisplay;