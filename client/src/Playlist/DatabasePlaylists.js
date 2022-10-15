import "./Playlist.css";
import useGetSpotifyData from "../ManagePlaylists/GetSpotifyData/useGetSpotifyData";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";

function DatabasePlaylist(props) {
    return(<>
                <div>
                    <img className="images" src={props.pd.images[0].url} alt="img"></img>
                    <button onClick={props.playGame}>Play Game with this playlist!</button>
                </div>
    </>
    )

}
export default DatabasePlaylist;