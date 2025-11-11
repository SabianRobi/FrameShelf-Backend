package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.List;
import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.entity.request.*;
import com.sabianrobi.frameshelf.entity.response.ListResponse;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import com.sabianrobi.frameshelf.mapper.PersonMapper;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    // ----- List Endpoints -----

    @GetMapping("/{userId}/lists")
    public ResponseEntity<java.util.List<ListResponse>> getUserLists(
            @PathVariable("userId") final UUID userId,
            @ModelAttribute final GetUserListsRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            // Verify the authenticated user matches the path parameter
            final User user = customOAuth2User.getUser();
            if (!user.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }

            final java.util.List<List> lists = listService.getUserLists(userId, request);

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

    @PostMapping("/{userId}/lists")
    public ResponseEntity<ListResponse> createList(
            @PathVariable("userId") final UUID userId,
            @RequestBody final CreateListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userId)) {
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

    @GetMapping("/{userId}/lists/{listId}")
    public ResponseEntity<ListResponse> getListById(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {

            final User user = customOAuth2User.getUser();
            if (!user.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }

            final List list = listService.getListById(listId, userId);

            return ResponseEntity.ok(ListResponse.fromList(list, movieMapper, personMapper));
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
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @RequestBody final UpdateListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }

            final List updatedList = listService.updateList(listId, request, user.getId());
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
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }

            listService.deleteList(listId, user.getId());
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // ----- List Item Endpoints -----

    @PostMapping("/{userId}/lists/{listId}/items")
    public ResponseEntity<ListResponse> addItemToList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @RequestBody final AddItemToListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }

            final List updatedList = listService.addItemToList(listId, request, user.getId());
            return ResponseEntity.ok(ListResponse.fromList(updatedList, movieMapper, personMapper));
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{userId}/lists/{listId}/items/{itemId}")
    public ResponseEntity<ListResponse> editItemInList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @PathVariable("itemId") final UUID itemId,
            @RequestBody final EditItemInListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }

            final List updatedList = listService.editItemInList(listId, itemId, request, user.getId());
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
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @PathVariable("itemId") final UUID itemId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        try {
            final User user = customOAuth2User.getUser();

            // Verify the authenticated user matches the path parameter
            if (!user.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }

            final List updatedList = listService.removeItemFromList(listId, itemId, user.getId());
            return ResponseEntity.ok(ListResponse.fromList(updatedList, movieMapper, personMapper));
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
