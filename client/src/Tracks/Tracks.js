// data.body.items gives an array/list of playlists
import Track from "./Track/Track"

function Tracks() {


    const [tracks, setTracks] = useState([]);

    function loadTracksPlaylist() {

        function getAllTracks() {
            fetch( "http://localhost:8080/api/tracks" )
            .then( response => {
                if( response.status === 200 ) {
                    return response.json();
                } else( console.log( response ) )
            } )
            .then( tracks => {
                setTracks( tracks );
            });
        }
        
        }
        useEffect(
            () => {
                getAllTracks();
            },
            []
        )


    return(
        <>
        <table className="table">
            <thead>
                <tr>
                    <th>Track Name</th>
                    <th>Spotify Populatity Score</th>
                    <th>Artist(s)</th>
                    <th>EloScore</th>
                </tr>
            </thead>
            <tbody>
                {tracks.map(
                    (t, index) => (<Track t={t} key = {index} />)
                )}
            </tbody>
        </table>
        </>
    )
}
export default Tracks;