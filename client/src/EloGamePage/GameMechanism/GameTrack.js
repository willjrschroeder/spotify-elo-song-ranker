import useCalculateEloTrackScores from "./useCalculateEloTrackScores";


function GameTrack({ displayTrack, otherTrack, updateScores }) {
    const [updatedWinnerTrack, updatedLoserTrack] = useCalculateEloTrackScores(displayTrack, otherTrack);


    function handleClick() {
        updateScores(updatedWinnerTrack, updatedLoserTrack);
    }

    return (
        <>
        
            <div className="flexTrackUpdate">
            
                <div className="elo-card">
                <button className="elo-button" onClick={handleClick}><img className="elo-image" src={displayTrack.albums[0].albumImageLink} alt="Song's album"/></button>
                    <h3>{displayTrack.title}</h3>
                    <h4>{displayTrack.artists[0].artistName}</h4>
                </div>
            </div>
        </>
    );
}

export default GameTrack;