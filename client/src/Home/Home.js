import { useContext } from "react"
import AuthContext from "../context/AuthContext";
import {Link} from "react-router-dom";
import "./Home.css"

function Home() {


    const auth = useContext(AuthContext);

    const canPlay = auth.user !== null;

    const canDelete = auth.user && auth.user.hasRole("admin")

    function loadAllTracks() {

    }

    return (
        <>
            <div>
                <button className='logout' onClick={() => auth.logout()}>logout</button>
            </div>
            <div>
                {canPlay && (<h2>play</h2>)}
            </div>

            <div>
                {canDelete && (<h2>delete</h2>)}
            </div>
            <div>
                <Link to="/playlistManager">Playlists</Link>
            </div>

            <div className="flex-summary">
                <h5>Top 10 Tracks</h5>
            </div>
            <div className="flex-summary">
                <h5>Top 10 Genres</h5>
            </div>
            <div className="flex-summary">
                <h5>Top 10 Artists</h5>
            </div>
        </>
    )
}
export default Home;