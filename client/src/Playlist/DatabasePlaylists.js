import "./Playlist.css";
import useGetSpotifyData from "../ManagePlaylists/GetSpotifyData/useGetSpotifyData";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";
import {Link} from "react-router-dom";

function DatabasePlaylist(props) {
    return(<>
                <div>
                    <img className="images" src={props.pd.images[0].url} alt="img"></img>
                    <Link to:{{
                        pathname:"/EloGamePage",
                        state:{
                            playlist: props.pd,
                        }
                    }}></Link>
                </div>
    </>
    )

}
export default DatabasePlaylist;