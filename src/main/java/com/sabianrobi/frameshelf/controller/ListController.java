package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.List;
import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.entity.request.AddItemToListRequest;
import com.sabianrobi.frameshelf.entity.request.CreateListRequest;
import com.sabianrobi.frameshelf.entity.request.UpdateListRequest;
import com.sabianrobi.frameshelf.entity.response.ListResponse;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import com.sabianrobi.frameshelf.mapper.PersonMapper;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class ListController {

    @Autowired
    private ListService listService;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private PersonMapper personMapper;

    @GetMapping("/{userId}/lists")
    public ResponseEntity<java.util.List<ListResponse>> getUserLists(
            @PathVariable("userId") final String userId) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final java.util.List<List> lists = listService.getUserLists(userUuid);

            final java.util.List<ListResponse> listResponses = lists.stream()
                    .map(list -> ListResponse.fromList(list, movieMapper, personMapper))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(listResponses);
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/lists/{listId}")
    public ResponseEntity<ListResponse> getListById(
            @PathVariable("userId") final String userId,
            @PathVariable("listId") final String listId) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final UUID listUuid = UUID.fromString(listId);
            final List list = listService.getListById(listUuid, userUuid);

            return ResponseEntity.ok(ListResponse.fromList(list, movieMapper, personMapper));

        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/lists")
    public ResponseEntity<ListResponse> createList(
            @PathVariable("userId") final String userId,
            @RequestBody final CreateListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userUuid)) {
                return ResponseEntity.status(403).build();
            }

            final List list = listService.createList(user, request.getName(), request.getType());
            return ResponseEntity.ok(ListResponse.fromList(list, movieMapper, personMapper));
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/lists/{listId}/items")
    public ResponseEntity<ListResponse> addItemToList(
            @PathVariable("userId") final String userId,
            @PathVariable("listId") final String listId,
            @RequestBody final AddItemToListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final UUID listUuid = UUID.fromString(listId);
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userUuid)) {
                return ResponseEntity.status(403).build();
            }

            final List updatedList = listService.addItemToList(listUuid, request.getItemId(), user.getId());
            return ResponseEntity.ok(ListResponse.fromList(updatedList, movieMapper, personMapper));
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}/lists/{listId}/items/{itemId}")
    public ResponseEntity<ListResponse> removeItemFromList(
            @PathVariable("userId") final String userId,
            @PathVariable("listId") final String listId,
            @PathVariable("itemId") final Integer itemId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final UUID listUuid = UUID.fromString(listId);
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userUuid)) {
                return ResponseEntity.status(403).build();
            }

            final List updatedList = listService.removeItemFromList(listUuid, itemId, user.getId());
            return ResponseEntity.ok(ListResponse.fromList(updatedList, movieMapper, personMapper));
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{userId}/lists/{listId}")
    public ResponseEntity<ListResponse> updateList(
            @PathVariable("userId") final String userId,
            @PathVariable("listId") final String listId,
            @RequestBody final UpdateListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final UUID listUuid = UUID.fromString(listId);
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userUuid)) {
                return ResponseEntity.status(403).build();
            }

            final List updatedList = listService.updateList(listUuid, request, user.getId());
            return ResponseEntity.ok(ListResponse.fromList(updatedList, movieMapper, personMapper));
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}/lists/{listId}")
    public ResponseEntity<Void> deleteList(
            @PathVariable("userId") final String userId,
            @PathVariable("listId") final String listId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final UUID listUuid = UUID.fromString(listId);
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userUuid)) {
                return ResponseEntity.status(403).build();
            }

            listService.deleteList(listUuid, user.getId());
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
