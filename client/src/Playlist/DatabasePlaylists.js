import "./Playlist.css";
import { Link } from "react-router-dom";

function DatabasePlaylist({ pd }) {

    const location = {
        pathname: '/eloGame',
        state: pd
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
                </td>
            </tr>


        </>
    )

}
export default DatabasePlaylist;