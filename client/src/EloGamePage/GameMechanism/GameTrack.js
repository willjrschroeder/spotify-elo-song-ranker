

function GameTrack({ track1, track2}){
    
    //TODO: return a display of the individual tracks
    
    return (
        <>
        <div className="flexTrackUpdate">
            <h3>{track1.title}: {track1.eloScore}</h3>
        </div>
        <div className="flexTrackUpdate">
            <h3>{track2.title}: {track2.eloScore}</h3>
        </div>

        </>
    );
}

export default GameTrack;