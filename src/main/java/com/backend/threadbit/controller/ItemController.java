package com.backend.threadbit.controller;


import com.backend.threadbit.dto.ItemDto;
import com.backend.threadbit.dto.PagedResponseDto;
import com.backend.threadbit.dto.PurchaseDto;
import com.backend.threadbit.dto.StatusUpdateDto;
import com.backend.threadbit.model.Bid;
import com.backend.threadbit.model.Purchase;
import com.backend.threadbit.service.ItemService;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Size;
import com.backend.threadbit.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(
        origins = { "https://threadbitwebsite-fqj4l.ondigitalocean.app" ,"https://threadbit.in" , "threadbit.in","www.threadbit.in" , "https://threadbid.in" , "threadbid.in","www.threadbid.in","http://192.168.32.1:5173", "https://secondhand-threads.vercel.app","http://10.244.72.46:8080"},
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
    public ResponseEntity<?> getItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Size ItemSize,
            @RequestParam(required = false) String sellerId,
            @RequestParam(required = false) String sellerUsername,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            // If pagination parameters are provided, use paginated response
            if (page >= 0 && size > 0) {
                PagedResponseDto<Item> pagedItems = itemService.getItems(
                    keyword, categoryId, status, ItemSize, sellerId, sellerUsername, page, size, sortBy, sortDir);
                return ResponseEntity.ok(pagedItems);
            }

            // Otherwise, use the original non-paginated response for backward compatibility
            List<Item> items;
            if (categoryId != null) {
                items = itemService.getItemsByCategory(categoryId);
            } else if (sellerId != null) {
                items = itemService.getItemsBySeller(sellerId);
            } else if(sellerUsername != null) {
                items = itemService.getItemByUsername(sellerUsername);
            }else{
                items = itemService.getAllItems();
            }

            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error getting items", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchItems(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PagedResponseDto<Item> pagedItems = itemService.searchItems(keyword, page, size, sortBy, sortDir);
            return ResponseEntity.ok(pagedItems);
        } catch (Exception e) {
            log.error("Error searching items", e);
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

    // Instant Buy Endpoints

    @PostMapping("/instant-buy")
    public ResponseEntity<?> createInstantBuyItem(@Valid @RequestBody ItemDto itemDto) {
        try {
            Item createdItem = itemService.createInstantBuyItem(itemDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating instant buy item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseItem(@Valid @RequestBody PurchaseDto purchaseDto) {
        try {
            Purchase purchase = itemService.purchaseItem(purchaseDto);

            // Add information about shipping in the response
            Map<String, Object> response = new HashMap<>();
            response.put("purchase", purchase);
            response.put("message", "Purchase successful! The seller will add shipping details soon.");
            response.put("nextStep", "The seller will add shipping details and send you a notification with tracking information.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error purchasing item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @GetMapping("/purchases/{buyerId}")
    public ResponseEntity<?> getPurchasesByBuyer(
            @PathVariable String buyerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size,
            @RequestParam(defaultValue = "purchaseDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            // If pagination parameters are provided, use paginated response
            if (page >= 0 && size > 0) {
                PagedResponseDto<Purchase> pagedPurchases = itemService.getPurchasesByBuyer(
                    buyerId, page, size, sortBy, sortDir);
                return ResponseEntity.ok(pagedPurchases);
            }

            // Otherwise, use the original non-paginated response
            List<Purchase> purchases = itemService.getPurchasesByBuyer(buyerId);
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            log.error("Error getting purchases", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/instant-buy/available")
    public ResponseEntity<?> getAvailableInstantBuyItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PagedResponseDto<Item> pagedItems = itemService.getAvailableInstantBuyItems(
                keyword != null ? keyword : "", page, size, sortBy, sortDir);
            return ResponseEntity.ok(pagedItems);
        } catch (Exception e) {
            log.error("Error getting available instant buy items", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
