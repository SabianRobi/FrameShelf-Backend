package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.List;
import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.entity.request.AddItemToListRequest;
import com.sabianrobi.frameshelf.entity.request.CreateListRequest;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class ListController {
    private ListService listService;

    @GetMapping("/{userId}/lists")
    public ResponseEntity<java.util.List<List>> getUserLists(
            @PathVariable("userId") final String userId) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final java.util.List<List> lists = listService.getUserLists(userUuid);

            return ResponseEntity.ok(lists);
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/lists/{listId}")
    public ResponseEntity<List> getListById(
            @PathVariable("userId") final String userId,
            @PathVariable("listId") final String listId) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final UUID listUuid = UUID.fromString(listId);
            final List list = listService.getListById(listUuid, userUuid);

            return ResponseEntity.ok(list);

        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/lists")
    public ResponseEntity<List> createList(
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
            return ResponseEntity.ok(list);
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/lists/{listId}/items")
    public ResponseEntity<List> addItemToList(
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
            return ResponseEntity.ok(updatedList);
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}/lists/{listId}/items/{itemId}")
    public ResponseEntity<List> removeItemFromList(
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
            return ResponseEntity.ok(updatedList);
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
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
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
