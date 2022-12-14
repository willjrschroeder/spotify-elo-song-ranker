import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

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
                        <a href={track.spotifyUrl} target="blank"><FontAwesomeIcon icon="fa-brands fa-spotify" size='2x'> </FontAwesomeIcon></a>
                    </div>
                </td>

                <td style={{ textAlign: 'left' }}>
                    <div className="d-flex align-items-center">
                        <div classname="container">
                            <img className="rounded-top track" src={track.albums[0].albumImageLink} />
                        </div>
                        <div className='container'>
                            {track.title}
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