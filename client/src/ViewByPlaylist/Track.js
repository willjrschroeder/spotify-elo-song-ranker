import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { useRef, useState } from 'react';
import './ViewByPlaylist.css';

function Track({ track }) {

    const [isPlaying, setIsPlaying] = useState(false);
    const audioPlayer = useRef();

    let artists = track.artists.map(a => a.artistName);

    function isEmptyOrUndefined(array) {
        if (array.length == 0 || array === undefined) {
            return false;
        } else {
            return true;
        }
    }

    function togglePlaying() {
        if(!isPlaying) {
            audioPlayer.current.play();
        } else {
            audioPlayer.current.pause();
        }

        setIsPlaying(previous => !previous);
    }

    return (
        <>
            <tr>
                <td>
                    <a href={track.spotifyUrl} target="blank"><FontAwesomeIcon icon="fa-brands fa-spotify" size='2x'> </FontAwesomeIcon></a>
                </td>

                <td>
                <button onClick={togglePlaying}>
                    {isPlaying ? <FontAwesomeIcon icon="fa-circle-stop" size='2x' />: <FontAwesomeIcon icon="fa-circle-play" size='2x' />}
                    
                    <audio ref={audioPlayer}>
                        <source src={track.previewUrl} />

                         
                    </audio>
                    </button>
                </td>

                <td>
                    <img className="rounded-top track" src={track.albums[0].albumImageLink} />
                </td>

                <td style={{ textAlign: 'left' }}>
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