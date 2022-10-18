import { useContext, useEffect, useState } from "react"
import AuthContext from "../context/AuthContext";
import {Link} from "react-router-dom";
import "./Home.css"
import UserPlaylist from "./UserPlaylist";

function Home() {

    const [selectedPlaylist, setSelectedPlaylist] = useState();
    const [playlists, setPlaylists] = useState([]);
    const [tracks, setTracks] = useState([]);
    const auth = useContext(AuthContext);

    function loadPlaylistsbyUser() {
        fetch( "http://localhost:8080/api/playlist/"+auth.user.id , {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else( console.log( response ) )
        } )
        .then( playlists => {
            console.log(playlists);
            setPlaylists( playlists );
        });
    }

    function loadTracksByPlaylist() {
        fetch( "http://localhost:8080/api/track/playlist/" + selectedPlaylist , {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else( console.log( response ) )
        } )
        .then( tracks => {
            console.log(tracks);
            setTracks( tracks );
        });
    }
    useEffect(
        () => {
            loadPlaylistsbyUser();
        },[]
    )
    function handleChange(event) {
        let playlistId = event.target.value;
        setSelectedPlaylist(playlistId);
        
        loadTracksByPlaylist();
    }

    return (
        <>
            <div className="header">Home Page</div>
            <div className="button-area">
                <Link to="/playlistManager" ><button>Manage Playlists</button></Link>
            </div>
            <div className="flex-summary">
            <table className="table">
                        <thead className="thead-dark">
                            <tr className="table-rows">
                                <th colSpan={2}>Spotify Playlists</th>
                            </tr>
                        </thead>
                        <tbody>
                            {playlists.map((playlist, index) => {
                                const databasePlaylistUris = databasePlaylists.map((playlist) => (playlist.playlistUri));

                                return (databasePlaylistUris.includes(playlist.uri)) ? null : <Playlist key={index} addPlaylist={addPlaylistToDatabase} p={playlist} index={index} />
                            })}
                        </tbody>
                    </table>

            </div>
        </>
    )
}
export default Home;