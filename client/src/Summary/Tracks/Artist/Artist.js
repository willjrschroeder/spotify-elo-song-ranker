function Artist(props) {

    function isEmptyOrUndefined(array) {
        if (array.length == 0 || array === undefined) {
            return false;
          } else {
            return true;
          }
    }

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
      }


    let genres = props.genres.map(g => capitalizeFirstLetter(g.genreName));

    return(
        <>
        <tr>
            <td>{props.artist}</td>
            <td>{isEmptyOrUndefined(props.genres) ? genres.join(", ") : "No genres available"}</td>
        </tr> 
        </>
    )
}
export default Artist;