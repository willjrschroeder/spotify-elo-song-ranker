function Artist(props) {



    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
      }


    let genres = props.genres.map(g => capitalizeFirstLetter(g.genreName));

    return(
        <>
        <tr>
            <td>{props.artist}</td>
           
        </tr> 
        </>
    )
}
export default Artist;