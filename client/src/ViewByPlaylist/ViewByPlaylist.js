import { useContext, useEffect, useRef, useState } from "react";
import { useLocation } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import Track from "./Track.js";



function ViewByPlaylist() {

    const location = useLocation();
    const playlist = location.state;
    const serverAuth = useContext(AuthContext);
    const [currentTracks, setCurrentTracks] = useState([]);
    const [currentlyPlayingTrack, setCurrentlyPlayingTrack] = useState(null);
    const audioPlayer = useRef();



    useEffect(() => {

        fetch(`http://localhost:8080/api/track/playlist/${playlist.playlistUri}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + serverAuth.user.token
            }
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else (console.log(response))
            })
            .then(tracks => {
                setCurrentTracks(tracks);
            });


    }, [playlist]);

    function playTrack(track, isPlaying) {

        audioPlayer.current.src = track.previewUrl;

        if (!isPlaying) {
            audioPlayer.current.play();
            setCurrentlyPlayingTrack(track)
        } else {
            audioPlayer.current.pause();
            setCurrentlyPlayingTrack(null)
        }
    }

    function stopTrack() {
        audioPlayer.current.pause();
        setCurrentlyPlayingTrack(null);
    }

    return (
        <>
            <div className="container-fluid w-100 playlist-image pb-5 pt-5 mb-5 mt-5">
                <img src={playlist.playlistImageLink} className="img-fluid rounded-top img-thumbnail" alt="Playlist from Spotify" />
                <h3 className="mt-4">
                    {playlist.playlistName}
                </h3>
            </div>

            {currentlyPlayingTrack ?
                <div id="current-track-display">
                    <button className="display-button" onClick={stopTrack}>
                        <p>
                            X
                        </p>
                    </button>
                    <div id="track-img-container">
                        <img src={currentlyPlayingTrack.albums[0].albumImageLink} id="track-display-image" alt="Song from Spotify" />
                    </div>
                    <p className="track-display-text">
                        {currentlyPlayingTrack.title.substring(0, 14)
                            + (currentlyPlayingTrack.title.length > 14 ? '...' : '')}
                    </p>
                    <p className="track-display-text">
                        {currentlyPlayingTrack.artists[0].artistName.substring(0, 14)
                            + (currentlyPlayingTrack.artists[0].artistName.length > 14 ? '...' : '')}
                    </p>
                </div>
                : null}

            <div className="container">
                <audio ref={audioPlayer} onEnded={stopTrack}>
                    <source src='' />
                </audio>
            </div>

            <div className="container pt-5 mt-5">

                <h1 className="mb-5">Your Rankings</h1>
                <table className="table table-background table-sm">
                    <thead>
                        <tr>

                            <th className="col-1"><div style={{ minHeight: 3 + 'em' }}></div></th>
                            <th></th>
                            <th></th>
                            <th className="text-center col-4" >Title</th>
                            <th className="">Spotify Popularity Score</th>
                            <th>Artist(s)</th>
                            <th>Elo Score</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentTracks.map((t, index) =>
                            <Track track={t} key={index} playTrack={playTrack} currentlyPlayingTrack={currentlyPlayingTrack}></Track>
                        )}
                    </tbody>
                </table>

            </div>
        </>

    )

}

export default ViewByPlaylist;