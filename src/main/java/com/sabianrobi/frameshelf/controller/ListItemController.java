package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.request.AddItemToListRequest;
import com.sabianrobi.frameshelf.entity.request.EditItemInListRequest;
import com.sabianrobi.frameshelf.entity.request.params.GetListItemsParams;
import com.sabianrobi.frameshelf.entity.response.ItemInListResponse;
import com.sabianrobi.frameshelf.mapper.ItemInListMapper;
import com.sabianrobi.frameshelf.mapper.ListMapper;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.ListItemService;
import com.sabianrobi.frameshelf.utility.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

import static com.sabianrobi.frameshelf.utility.Helper.verifyUserHasAccessToList;

@RestController
@RequestMapping("/api/v1/user")
public class ListItemController {

    @Autowired
    private ListItemService listItemService;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ItemInListMapper itemInListMapper;

    @GetMapping("/{userId}/lists/{listId}/items")
    public Page getItemsInList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @ModelAttribute final GetListItemsParams params,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User,
            @PageableDefault(sort = "addedAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        if (params.getType() == ListType.MOVIE) {
            // Sorting & Pagination
            final Set<String> allowedKeys = Set.of("notes", "addedAt", "watchedAt", "createdAt", "updatedAt");
            final Pageable safePageable = Helper.getSafePageable(pageable, allowedKeys);

            // Getting the data
            final Page<MovieInList> movies = listItemService.getMovieListItems(userId, listId, safePageable);

            // Returning data
            return movies.map(itemInListMapper::mapMovieInListToMovieInListResponse);
        } else if (params.getType() == ListType.PERSON) {
            // Sorting & Pagination
            final Set<String> allowedKeys = Set.of("notes", "addedAt", "createdAt", "updatedAt");
            final Pageable safePageable = Helper.getSafePageable(pageable, allowedKeys);

            // Getting the data
            final Page<PersonInList> lists = listItemService.getPersonListItems(userId, listId, safePageable);

            // Returning data
            return lists.map(itemInListMapper::mapPersonInListToPersonInListResponse);
        }

        throw new IllegalArgumentException("Invalid list type");
    }

    @GetMapping("/{userId}/lists/{listId}/items/{itemId}")
    public ResponseEntity<ItemInListResponse> getItemInList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @PathVariable("itemId") final UUID itemId,
            @RequestParam("type") final ListType type,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        final User user = customOAuth2User.getUser();

        final ItemInList itemFromList = listItemService.getItemInList(user.getId(), listId, itemId, type);

        return ResponseEntity.ok(itemInListMapper.mapItemInListToItemInListResponse(itemFromList));
    }

    @PostMapping("/{userId}/lists/{listId}/items")
    public ResponseEntity<ItemInListResponse> addItemToList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @RequestBody final AddItemToListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        final User user = customOAuth2User.getUser();

        final ItemInList itemInList = listItemService.addItemToList(listId, request, user.getId());

        return ResponseEntity.ok(itemInListMapper.mapItemInListToItemInListResponse(itemInList));
    }

    @PatchMapping("/{userId}/lists/{listId}/items/{itemId}")
    public ResponseEntity<ItemInListResponse> editItemInList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @PathVariable("itemId") final UUID itemId,
            @RequestBody final EditItemInListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        final User user = customOAuth2User.getUser();

        final ItemInList itemInList = listItemService.editItemInList(listId, itemId, request, user.getId());

        return ResponseEntity.ok(itemInListMapper.mapItemInListToItemInListResponse(itemInList));
    }

    @DeleteMapping("/{userId}/lists/{listId}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @PathVariable("itemId") final UUID itemId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        final User user = customOAuth2User.getUser();

        listItemService.removeItemFromList(listId, itemId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
