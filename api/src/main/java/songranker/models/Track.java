package songranker.models;

public class Track {
    private String track_uri;
    private String title;
    private int eloScore;
    private int numOfMatchesPlayed; // number of times the song has been in an ELO match
    private int trackDuration; // stored in milliseconds
    private int popularityNum; // Spotify's popularity of the track. Based on number of recent streams
    private String spotifyUrl; // link to the full track on Spotify's service
    private String previewUrl; // link to a 30-second track preview in .mp3 format
    //TODO: Figure out relationship to Artist, Album, Genre. Likely arrays of these models as data members
}
