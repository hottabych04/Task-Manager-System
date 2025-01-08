package com.hottabych04.app.controller.comment;

import com.hottabych04.app.controller.comment.payload.CommentCreateDto;
import com.hottabych04.app.controller.comment.payload.CommentGetDto;
import com.hottabych04.app.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CommentGetDto create(
            @RequestBody CommentCreateDto comment,
            Authentication authentication
    ){
        return commentService.create(comment, authentication);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CommentGetDto getComment(
            @PathVariable Long id,
            Authentication authentication
    ){
        return commentService.getComment(id, authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            Authentication authentication
    ){
        commentService.delete(id, authentication);
    }
}
