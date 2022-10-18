import useCalculateEloTrackScores from "./useCalculateEloTrackScores";


function GameTrack({ displayTrack, otherTrack, updateScores }) {
    const [updatedWinnerTrack, updatedLoserTrack] = useCalculateEloTrackScores(displayTrack, otherTrack);


    function handleClick() {
        updateScores(updatedWinnerTrack, updatedLoserTrack);
    }

    return (
        <>
            <div className="flexTrackUpdate">
                <div>
                    <img src={displayTrack.albums[0].albumImageLink} />
                    <h3>{displayTrack.title}</h3>
                    <h4>{displayTrack.artists[0].artistName}</h4>
                </div>

                <button onClick={handleClick}>Choose Track</button>
            </div>
        </>
    );
}

export default GameTrack;