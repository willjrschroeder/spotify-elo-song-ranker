function Track({ track }) {

    let artists = track.artists.map(a => a.artistName);

    function isEmptyOrUndefined(array) {
        if (array.length == 0 || array === undefined) {
            return false;
          } else {
            return true;
          }
    }

    return(
        <>
        <tr>
            <td>{track.previewUrl}</td>
            <td><img className="img-fluid ${3|rounded-top,rounded-right,rounded-bottom,rounded-left,rounded-circle,|}" src={track.albums[0].albumImageLink} /></td>
            <td>{track.title}</td>
            <td>{track.popularityNumber}</td>
            <td>{isEmptyOrUndefined ? artists.join(", ") : "No "}</td>
            <td>{track.eloScore}</td>
        </tr> 
        </>
    )
}
export default Track;