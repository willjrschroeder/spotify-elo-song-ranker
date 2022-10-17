function useCalculateEloTrackScores(winnerTrack, loserTrack){
    const k = 32; // constant that determines the maximum a score can change by in each match
    let winnerScore = winnerTrack.eloScore;
    let loserScore = loserTrack.eloScore;
    let updatedWinner = {...winnerTrack};
    let updatedLoser = {...loserTrack};

    //(1.0 * 1.0) / (1 + 1.0 * Math.pow(10, (1.0 * (rating1 - rating2)) / 400)) - Formula for expected scores
    // This is the percent chance that the winner and loser had to win the match [based on current ELO scores]
    let winnerExpectedProb = (1.0 * 1.0) / (1 + 1.0 * Math.pow(10, (1.0 * (winnerScore - loserScore)) / 400)); 
    let loserExpectedProb = (1.0 * 1.0) / (1 + 1.0 * Math.pow(10, (1.0 * (loserScore - winnerScore)) / 400));

    //updated scores based on gap between past scores and constant k
    let updatedWinnerScore = Math.round(winnerScore + (k * (1 - winnerExpectedProb)));
    let updatedLoserScore = Math.round(loserScore + (k * (0 - loserExpectedProb)));

    updatedWinner.eloScore = updatedWinnerScore;
    updatedLoser.eloScore = updatedLoserScore;

    return [updatedWinner, updatedLoser]
}

export default useCalculateEloTrackScores;