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
                <button className='logout' onClick={() => auth.logout()}>Logout</button>
            </div>
            <div className="button-area">
                <Link to="/playlistManager" className="button">Playlists</Link>
            </div>
            <div className="flex-summary">
                <div>
                    <h5>Top 10 Tracks</h5>
                </div>
                <div>
                    <h5>Top 10 Genres</h5>
                </div>
                <div>
                    <h5>Top 10 Artists</h5>
                </div>
            </div>
        </>
    )
}
export default Home;