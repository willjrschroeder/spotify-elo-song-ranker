import { useContext, useState, useEffect } from "react";
import AuthContext from "../context/AuthContext";
import "./Summary.css";
function Summary() {

    const [tracks, setTracks] = useState([]);
    const [artists, setArtists] = useState([]);
    const [genres, setGenres] = useState([]);

    const auth = useContext(AuthContext);

    function getAllTracksByUser() {

        fetch( "http://localhost:8080/api/track/"+auth.user.id , {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else( console.log( response ) )
        } )
        .then( tracks => {
            console.log(tracks);
            setTracks( tracks );
        });
    }

    function getTop10Artists() {
        
        fetch( "http://localhost:8080/api/track/top10artist/"+auth.user.id , {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else( console.log( response ) )
        } )
        .then( artists => {
            console.log(artists);
            setArtists( artists );
        });

    }
    function getTop10Genres() {
        
        fetch( "http://localhost:8080/api/track/top10genre/"+auth.user.id , {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + auth.user.token
            }
        })
        .then( response => {
            if( response.status === 200 ) {
                return response.json();
            } else( console.log( response ) )
        } )
        .then( genres => {
            console.log(genres);
            setGenres( genres );
        });

    }
    useEffect(
        () => {
            getAllTracksByUser();
            getTop10Artists();
            getTop10Genres();

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