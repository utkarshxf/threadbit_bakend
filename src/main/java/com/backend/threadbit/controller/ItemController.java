package com.backend.threadbit.controller;


import com.backend.threadbit.dto.ItemDto;
import com.backend.threadbit.dto.StatusUpdateDto;
import com.backend.threadbit.service.ItemService;
import com.backend.threadbit.model.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(
        origins = {"*","http://192.168.32.1:5173", "https://secondhand-threads.vercel.app","http://10.244.72.46:8080"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getItems(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String sellerId) {
        try {
            List<Item> items;
            
            if (categoryId != null) {
                items = itemService.getItemsByCategory(categoryId);
            } else if (sellerId != null) {
                items = itemService.getItemsBySeller(sellerId);
            } else {
                items = itemService.getAllItems();
            }
            
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error getting items", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable String id) {
        try {
            Item item = itemService.getItemById(id);
            return ResponseEntity.ok(item);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Item not found"));
        } catch (Exception e) {
            log.error("Error getting item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createItem(@Valid @RequestBody ItemDto itemDto) {
        try {
            Item createdItem = itemService.createItem(itemDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateItemStatus(
            @PathVariable String id,
            @Valid @RequestBody StatusUpdateDto statusUpdateDto) {
        try {
            Item updatedItem = itemService.updateItemStatus(id, statusUpdateDto.getStatus());
            return ResponseEntity.ok(updatedItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Item not found"));
        } catch (Exception e) {
            log.error("Error updating item status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}