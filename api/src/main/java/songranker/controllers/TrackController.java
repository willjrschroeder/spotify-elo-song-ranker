package songranker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songranker.domain.PlaylistService;
import songranker.domain.TrackService;
import songranker.models.*;

import java.util.List;

@RestController
@RequestMapping("/api/track")
@CrossOrigin
public class TrackController {
    @Autowired
    TrackService service;

    @GetMapping("/playlist/{playlistUri}")
    public ResponseEntity<Object> getTracksByPlaylistUri(@PathVariable String playlistUri) {
        Result<List<Track>> result = service.getTracksByPlaylistUri(playlistUri);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping
    public ResponseEntity<Object> updateTrackEloScore(@RequestBody Track updatedTrack){
        Result result = service.updateTrackEloScore(updatedTrack);

        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
    @GetMapping("/{appUserId}")
    public ResponseEntity<Object> getTracksByUser(@PathVariable int appUserId) {
        Result<List<Track>> result = service.getTracksByUser(appUserId);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(),HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }
    @GetMapping("/top10artist/{appUserId}")
    public ResponseEntity<Object> getTop10Artists(@PathVariable int appUserId) {
        Result<List<Artist>> result = service.getTop10Artists(appUserId);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(),HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }
    @GetMapping("/top10genre/{appUserId}")
    public ResponseEntity<Object> getTop10Genres(@PathVariable int appUserId) {
        Result<List<Genre>> result = service.getTop10Genres(appUserId);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(),HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }
}

