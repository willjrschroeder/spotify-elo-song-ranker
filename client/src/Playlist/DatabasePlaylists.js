import "./Playlist.css";
import useGetSpotifyData from "../ManagePlaylists/GetSpotifyData/useGetSpotifyData";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";
import { Link } from "react-router-dom";

function DatabasePlaylist({ pd }) {

    const location = {
        pathname: '/eloGame',
        state: pd
      }
      console.log(location);

    return (<>
        <div>
            <img className="images" src={pd.playlistImageLink} alt="img"></img>
            <Link to={location}> 
                <button>Rank Tracks in this Playlist</button>
            </Link>
        </div>
    </>
    )

}
export default DatabasePlaylist;