package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.request.AddItemToListRequest;
import com.sabianrobi.frameshelf.entity.request.EditItemInListRequest;
import com.sabianrobi.frameshelf.entity.request.params.GetListItemsParams;
import com.sabianrobi.frameshelf.entity.response.ListResponse;
import com.sabianrobi.frameshelf.mapper.ListMapper;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import com.sabianrobi.frameshelf.mapper.PersonMapper;
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

import java.util.Arrays;
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
    private MovieMapper movieMapper;

    @Autowired
    private PersonMapper personMapper;

    @GetMapping("/{userId}/lists/{listId}/items")
    public Page getItemsInList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @ModelAttribute final GetListItemsParams params,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User,
            @PageableDefault(sort = "addedAt", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        if (params.getType() == ListType.MOVIE) {
            // Sorting & Pagination
            final Set<String> allowedKeys = Set.of("notes", "addedAt", "watchedAt", "createdAt", "updatedAt");
            final Pageable safePageable = Helper.getSafePageable(pageable, allowedKeys);

            // Getting the data
            final Page<MovieInList> movies = listItemService.getMovieListItems(userId, listId, safePageable);

            // Returning data
            return movies.map(movieMapper::mapMovieInListToMovieInListResponse);
        } else if (params.getType() == ListType.PERSON) {
            // Sorting & Pagination
            final Set<String> allowedKeys = Set.of("notes", "addedAt", "createdAt", "updatedAt");
            final Pageable safePageable = Helper.getSafePageable(pageable, allowedKeys);

            // Getting the data
            final Page<PersonInList> lists = listItemService.getPersonListItems(userId, listId, safePageable);

            // Returning data
            return lists.map(personMapper::mapPersonInListToPersonInListResponse);
        }

        throw new IllegalArgumentException("Invalid list type");
    }

    @PostMapping("/{userId}/lists/{listId}/items")
    public ResponseEntity<ListResponse> addItemToList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @RequestBody final AddItemToListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        try {
            final User user = customOAuth2User.getUser();

            final List updatedList = listItemService.addItemToList(listId, request, user.getId());
            return ResponseEntity.ok(listMapper.mapListToListResponse(updatedList));
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
        verifyUserHasAccessToList(userId, customOAuth2User);

        try {
            final User user = customOAuth2User.getUser();

            final List updatedList = listItemService.editItemInList(listId, itemId, request, user.getId());
            return ResponseEntity.ok(listMapper.mapListToListResponse(updatedList));
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
        verifyUserHasAccessToList(userId, customOAuth2User);

        try {
            final User user = customOAuth2User.getUser();

            final List updatedList = listItemService.removeItemFromList(listId, itemId, user.getId());
            return ResponseEntity.ok(listMapper.mapListToListResponse(updatedList));
        } catch (final IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
