import useCalculateEloTrackScores from "./useCalculateEloTrackScores";


function GameTrack({ displayTrack, otherTrack, updateScores }) {
    const { updatedWinnerTrack, updatedLoserTrack } = useCalculateEloTrackScores(displayTrack, otherTrack);


    function handleClick() {
        updateScores(updatedWinnerTrack, updatedLoserTrack);
    }

    return (
        <>
            <div className="flexTrackUpdate">
                <h3>{displayTrack.title}: {displayTrack.eloScore}</h3>
                <button onClick={handleClick}>Choose Track</button>
            </div>
        </>
    );
}

export default GameTrack;