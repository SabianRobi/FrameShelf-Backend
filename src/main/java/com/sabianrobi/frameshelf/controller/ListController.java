package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.List;
import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.entity.request.CreateListRequest;
import com.sabianrobi.frameshelf.entity.request.UpdateListRequest;
import com.sabianrobi.frameshelf.entity.request.params.GetUserListsParams;
import com.sabianrobi.frameshelf.entity.response.ListResponse;
import com.sabianrobi.frameshelf.mapper.ListMapper;
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

        final User user = customOAuth2User.getUser();

        final List list = listService.createList(user, request.getName(), request.getType());
        return ResponseEntity.ok(listMapper.mapListToListResponse(list));
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

        final User user = customOAuth2User.getUser();

        final List updatedList = listService.updateList(listId, request, user.getId());
        return ResponseEntity.ok(listMapper.mapListToListResponse(updatedList));

    }

    @DeleteMapping("/{userId}/lists/{listId}")
    public ResponseEntity<Void> deleteList(
            @PathVariable("userId") final UUID userId,
            @PathVariable("listId") final UUID listId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        verifyUserHasAccessToList(userId, customOAuth2User);

        final User user = customOAuth2User.getUser();

        listService.deleteList(listId, user.getId());

        return ResponseEntity.noContent().build();
    }
}
