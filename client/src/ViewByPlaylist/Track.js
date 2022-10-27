import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { useEffect, useRef, useState } from 'react';
import './ViewByPlaylist.css';

function Track({ track, playTrack, currentlyPlayingTrack }) {

    const [isPlaying, setIsPlaying] = useState(false);
    const audioPlayer = useRef();

    useEffect( () => {
        if(currentlyPlayingTrack === track) {
            setIsPlaying(true);
        } else {
            setIsPlaying(false);
        }
    }, [currentlyPlayingTrack]);

    let artists = track.artists.map(a => a.artistName);

    function isEmptyOrUndefined(array) {
        if (array.length == 0 || array === undefined) {
            return false;
        } else {
            return true;
        }
    }




    function togglePlaying() {
        playTrack(track, isPlaying);
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