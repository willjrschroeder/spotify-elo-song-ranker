import { useContext, useState, useEffect } from "react";
import AuthContext from "../context/AuthContext";
import "./Summary.css";
import Artist from "./Tracks/Artist/Artist";
import Genre from "./Tracks/Genre/Genre";
import Track from "./Tracks/Track/Track";
function Summary() {

    const [tracks, setTracks] = useState([]);
    const [artists, setArtists] = useState([]);
    const [genres, setGenres] = useState([]);

    let tracks10 = tracks.slice(0, 10);
    let artists10 = artists.slice(0, 10);

    const auth = useContext(AuthContext);

    function getAllTracksByUser() {

        fetch("http://localhost:8080/api/track/top10track/" + auth.user.id, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else (console.log(response))
            })
            .then(tracks => {
                setTracks(tracks);
            });
    }

    function getTop10Artists() {

        fetch("http://localhost:8080/api/track/top10artist/" + auth.user.id, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else (console.log(response))
            })
            .then(artists => {
                setArtists(artists);
            });

    }
    function getTop10Genres() {

        fetch("http://localhost:8080/api/track/top10genre/" + auth.user.id, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else (console.log(response))
            })
            .then(genres => {
                setGenres(genres);
            });

    }
    useEffect(
        () => {
            getAllTracksByUser();
            getTop10Artists();
            getTop10Genres();

        },
        []
    )
    function isEmpty() {
        if (tracks === undefined || tracks.length < 1) {
            return true;
        }
        return false;
    }
    return ( //TODO: add conditional render if there are no playlists
        <>
            
            <div className="flex-summary">
            <h1 className="summary-head">Summary</h1>
            {isEmpty() ? <div>Go to Manage Playlists to view all your tracks here</div> : <div></div> }
            <div className="playlist-rows">
                <div>
                    <h3 className="mb-3 head">Top Tracks</h3>
                    <table className="table">
                        <thead>
                            <tr>
                                <th className="col-1"><div style={{ minHeight: 3 + 'em' }}></div></th>
                                <th className="text-center col-4" >Title</th>
                                <th>Spotify Popularity Score</th>
                                <th>Artist(s)</th>
                                <th>Elo Score</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tracks10.map((t, index) =>
                                <Track key={index} track={t}></Track>
                            )}
                        </tbody>
                    </table>
                </div>
                <div className="col-3">
                <h3 className="mb-3 summary-head">Top Genres</h3>
                    <table className="table ">
                        <thead>
                            <tr>
                                <th>Genre</th>
                            </tr>
                        </thead>
                        <tbody>
                            {genres.map((g, index) =>
                                <Genre key={index} genreName={g.genreName}></Genre>
                            )}
                        </tbody>
                    </table>
                </div>
                <div className="col-3">
                    <h3 className="mb-3 summary-head">Top Artists</h3>
                    <table className="table">
                        <thead >
                            <tr>
                                <th>Name</th>
                            </tr>
                        </thead>
                        <tbody>
                            {artists10.map((a, index) =>
                                <Artist key={index} artist={a.artistName} genres={a.genres}></Artist>
                            )}
                        </tbody>
                    </table>
                </div>
                </div>
            </div>
        </>
    )

}
export default Summary;