import { useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import Track from "./Track.js";



function ViewByPlaylist() {

    const location = useLocation();
    const playlist = location.state;
    const serverAuth = useContext(AuthContext);
    const [currentTracks, setCurrentTracks] = useState([]);



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

    return (
        <>
            <div className="container mb-5">
                <img src={playlist.playlistImageLink} className="img-fluid rounded-top img-thumbnail" alt="Playlist from Spotify" />
                <h3 className="mt-4">
                    {playlist.playlistName}
                </h3>
            </div>

            <div className="container mt-5">
                <div>
                    <table className="table table-dark table-striped table-sm">
                        <thead className="table-dark">
                            <tr>
                                <th className="col-1"></th>
                                <th className="text-center col-6" >Title</th>
                                <th>Spotify Popularity Score</th>
                                <th>Artist(s)</th>
                                <th>Elo Score</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentTracks.map((t, index) =>
                                <Track track={t} key={index}></Track>
                            )}
                        </tbody>
                    </table>
                </div>


            </div>
        </>

    )

}

export default ViewByPlaylist;