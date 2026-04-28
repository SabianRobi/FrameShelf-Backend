package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.request.AddItemToListRequest;
import com.sabianrobi.frameshelf.entity.request.CreateListRequest;
import com.sabianrobi.frameshelf.entity.request.EditItemInListRequest;
import com.sabianrobi.frameshelf.entity.request.UpdateListRequest;
import com.sabianrobi.frameshelf.entity.request.params.GetListItemsParams;
import com.sabianrobi.frameshelf.entity.request.params.GetUserListsParams;
import com.sabianrobi.frameshelf.entity.response.ListResponse;
import com.sabianrobi.frameshelf.mapper.ListMapper;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import com.sabianrobi.frameshelf.mapper.PersonMapper;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.ListService;
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
public class ListController {

    @Autowired
    private ListService listService;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private PersonMapper personMapper;

    // ----- List Endpoints -----

    @GetMapping("/{userId}/lists")
    public Page getUserLists(
            @PathVariable("userId") final UUID userId,
            @ModelAttribute final GetUserListsParams params,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User,
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        // Sorting & Pagination
        final Set<String> allowedKeys = Set.of("name", "createdAt", "updatedAt");
        final Pageable safePageable = Helper.getSafePageable(pageable, allowedKeys);

        // Getting the data
        final Page lists = listService.getUserLists(userId, params, safePageable);

        // Returning data
        return lists.map(list -> listMapper.mapListToListResponse((List) list));
    }


    @PostMapping("/{userId}/lists")
    public ResponseEntity<ListResponse> createList(
            @PathVariable("userId") final UUID userId,
            @RequestBody final CreateListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        try {
            final User user = customOAuth2User.getUser();

            final List list = listService.createList(user, request.getName(), request.getType());
            return ResponseEntity.ok(listMapper.mapListToListResponse(list));
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
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User
    ) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        // Getting the data
        final List list = listService.getListById(listId, userId);

        // Returning data
        return ResponseEntity.ok(listMapper.mapListToListResponse(list));
    }

    @PatchMapping("/{userId}/lists/{listId}")
    public ResponseEntity<ListResponse> updateList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @RequestBody final UpdateListRequest request,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        try {
            final User user = customOAuth2User.getUser();

            final List updatedList = listService.updateList(listId, request, user.getId());
            return ResponseEntity.ok(listMapper.mapListToListResponse(updatedList));
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
        verifyUserHasAccessToList(userId, customOAuth2User);

        try {
            final User user = customOAuth2User.getUser();

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
            final Page<MovieInList> movies = listService.getMovieListItems(userId, listId, safePageable);

            // Returning data
            return movies.map(movieMapper::mapMovieInListToMovieInListResponse);
        } else if (params.getType() == ListType.PERSON) {
            // Sorting & Pagination
            final Set<String> allowedKeys = Set.of("notes", "addedAt", "createdAt", "updatedAt");
            final Pageable safePageable = Helper.getSafePageable(pageable, allowedKeys);

            // Getting the data
            final Page<PersonInList> lists = listService.getPersonListItems(userId, listId, safePageable);

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

            final List updatedList = listService.addItemToList(listId, request, user.getId());
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

            final List updatedList = listService.editItemInList(listId, itemId, request, user.getId());
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

            final List updatedList = listService.removeItemFromList(listId, itemId, user.getId());
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
