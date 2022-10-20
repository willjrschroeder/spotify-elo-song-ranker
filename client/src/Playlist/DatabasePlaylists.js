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
                <td className="col-4">
                    <img className="img-fluid rounded track" src={pd.playlistImageLink} alt="Spotify playlist" />
                </td>
                <td><p className="name">{pd.playlistName}</p></td>
                <td>
                    <Link to={location}>
                        <button className="mb-3">Rank Tracks in this Playlist</button>
                    </Link>

                    
                </td>

                <td><button onClick={handleClick}>Remove This Playlist From Tracking</button></td>
            </tr>


        </>
    )

}
export default DatabasePlaylist;