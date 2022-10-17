import "./Playlist.css";
import { Link } from "react-router-dom";

function DatabasePlaylist({ pd }) {

    const location = {
        pathname: '/eloGame',
        state: pd
      }
      console.log(location);

    return (<>
            <tr>    
            <td> <img className="images" src={pd.playlistImageLink} alt="img"></img></td>  
            <td>{pd.name}</td>
            <td>            <Link to={location}> 
                <button>Rank Tracks in this Playlist</button>
            </Link>Add Tracks</td> 
        </tr>
           

    </>
    )

}
export default DatabasePlaylist;