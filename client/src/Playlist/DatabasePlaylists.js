import "./Playlist.css";
import { Link } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";

function DatabasePlaylist({ pd, removePlaylistFromDatabase }) {

    const location = {
        pathname: '/eloGame',
        state: pd
    }

    function handleClick() {
        removePlaylistFromDatabase(pd.playlistUri);
    }

    return (
        <>
            <tr>
                <td>
                    <img className="img-fluid ${3|rounded-top,rounded-right,rounded-bottom,rounded-left,rounded-circle,|}" src={pd.playlistImageLink} alt="Spotify playlist" />
                    </td>
                <td>
                    
                    <p>{pd.playlistName}</p>
                    <Link to={location}>
                        <button>Rank Tracks in this Playlist</button>
                    </Link>
                    <button onClick={handleClick}>Remove This Playlist From Tracking</button>
                </td>
            </tr>


        </>
    )

}
export default DatabasePlaylist;