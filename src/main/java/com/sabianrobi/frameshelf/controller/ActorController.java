package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/actors")
public class ActorController {
    @Autowired
    private PersonService personService;

//    @GetMapping
//    public ResponseEntity<Page<ActorResponse>> getActors(final @RequestParam(name = "page", defaultValue = "0") Integer page,
//                                                         final @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//                                                         final @RequestParam(name = "sort", defaultValue = "[{\"field\":\"name\",\"direction\":\"asc\"}]") String sort) {
//        final Page<ActorResponse> actors = actorService.getActors(
//                GetActorsRequest.builder()
//                        .page(page)
//                        .size(pageSize)
//                        .sort(sort)
//                        .build());
//
//        return ResponseEntity.ok(actors);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ActorResponse> getActor(final @PathVariable("id") Integer id) {
//        final ActorResponse actorResponse = actorService.getActor(id);
//
//        return ResponseEntity.ok(actorResponse);
//    }
//
//    @PostMapping
//    public ResponseEntity<ActorResponse> createMovie(final @RequestBody CreateActorRequest createActorRequest) {
//        final ActorResponse actorResponse = actorService.createActor(createActorRequest);
//
//        return ResponseEntity.ok(actorResponse);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteActor(final @PathVariable("id") Integer id) {
//        actorService.deleteActor(id);
//
//        return ResponseEntity.noContent().build();
//    }
//
}
