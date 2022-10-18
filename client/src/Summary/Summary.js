import { useContext, useState, useEffect } from "react";
import AuthContext from "../context/AuthContext";
import "./Summary.css";
function Summary() {

    const [tracks, setTracks] = useState([]);

    const auth = useContext(AuthContext);

    function getAllTracksByUser() {
        
        fetch( "http://localhost:8080/api/tracks"+auth.user.id )
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else( console.log( response ) )
        } )
        .then( tracks => {
            setTracks( tracks );
        });

    }
    useEffect(
        () => {
            getAllTracksByUser();
        },
        []
    )
    return(
        <>
        <h1>Summary</h1>
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
export default Summary;