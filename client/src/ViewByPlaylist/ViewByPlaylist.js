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

    return (
        <>


            <div className="container-fluid w-100 playlist-image pb-5 pt-5 mb-5 mt-5">
                <img src={playlist.playlistImageLink} className="img-fluid rounded-top img-thumbnail" alt="Playlist from Spotify" />
                <h3 className="mt-4">
                    {playlist.playlistName}
                </h3>
            </div>

            <div className="container">
                <audio ref={audioPlayer}>
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