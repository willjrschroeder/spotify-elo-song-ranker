import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import './ViewByPlaylist.css';

function Track({ track }) {

    let artists = track.artists.map(a => a.artistName);

    function isEmptyOrUndefined(array) {
        if (array.length == 0 || array === undefined) {
            return false;
        } else {
            return true;
        }
    }

    return (
        <>
            <tr>
                <td>
                    <div className='container text-center align-items-center'>
                        <FontAwesomeIcon icon="play-circle" size="2x" > </FontAwesomeIcon>
                    </div>
                </td>

                <td>
                    <div className="d-flex align-items-center">
                        <div classname="container">
                            <img className="rounded-top track" src={track.albums[0].albumImageLink} />
                        </div>
                        <div className='container'>
                            <a href={track.spotifyUrl} target="blank">{track.title}</a>
                        </div>
                    </div>
                </td>

                <td>{track.popularityNumber}</td>
                <td>{isEmptyOrUndefined ? artists.join(", ") : "No "}</td>
                <td>{track.eloScore}</td>
            </tr>
        </>

    )
}
export default Track;