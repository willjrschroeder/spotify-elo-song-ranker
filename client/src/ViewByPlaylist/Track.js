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
                    <a href={track.spotifyUrl} target="blank"><FontAwesomeIcon icon="fa-brands fa-spotify" size='2x'> </FontAwesomeIcon></a>
                </td>

                <td>
                    <FontAwesomeIcon icon="fa-circle-play" size='2x'> </FontAwesomeIcon>
                </td>

                <td>
                    <img className="rounded-top track" src={track.albums[0].albumImageLink} />
                </td>

                <td style={{ textAlign: 'left'}}>
                    {track.title}
                </td>

                <td>{track.popularityNumber}</td>
                <td>{isEmptyOrUndefined ? artists.join(", ") : "No "}</td>
                <td>{track.eloScore}</td>
            </tr>
        </>

    )
}
export default Track;