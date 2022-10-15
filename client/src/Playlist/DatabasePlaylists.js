import "./Playlist.css";
import useGetSpotifyData from "../ManagePlaylists/GetSpotifyData/useGetSpotifyData";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";

function DatabasePlaylist(props) {
    return(<>
                <div>
                    <img className="images" src={props.playlists.images[0].url} alt="img"></img>
                </div>
    </>
    )

}
export default DatabasePlaylist;