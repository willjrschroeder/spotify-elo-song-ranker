

function GameTrack({ track1, updateScores }) {

    function handleClick() {
        updateScores(track1);
    }

    return (
        <>
            <div className="flexTrackUpdate">
                <h3>{track1.title}: {track1.eloScore}</h3>
                <button onClick={handleClick}>Choose Track</button>
            </div>
        </>
    );
}

export default GameTrack;