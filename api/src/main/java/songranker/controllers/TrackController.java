package songranker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songranker.domain.PlaylistService;
import songranker.domain.TrackService;
import songranker.models.Playlist;
import songranker.models.Result;
import songranker.models.Track;

import java.util.List;

@RestController
@RequestMapping("/api/track")
@CrossOrigin
public class TrackController {
    @Autowired
    TrackService service;

    @GetMapping("/{playlistUri}")
    public ResponseEntity<Object> getTracksByPlaylistUri(@PathVariable String playlistUri) {
        Result<List<Track>> result = service.getTracksByPlaylistUri(playlistUri);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{updatedEloScore}")
    public ResponseEntity<Object> updateTrackEloScore(@PathVariable Integer updatedEloScore){
        Result result = service.updateTrackEloScore(updatedEloScore);

        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}

