package songranker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songranker.domain.PlaylistService;
import songranker.models.Playlist;
import songranker.models.Result;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
@CrossOrigin
public class PlaylistController {
    @Autowired
    PlaylistService service;

    @GetMapping("/{appUserId}")
    public ResponseEntity<Object> getPlaylistsByAppUserId(@PathVariable int appUserId) {
        Result<List<Playlist>> result = service.getPlaylistsByAppUserId(appUserId);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
        }
}

