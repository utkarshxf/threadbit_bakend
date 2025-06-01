package com.backend.threadbit.service;


import com.backend.threadbit.dto.ItemDto;
import com.backend.threadbit.dto.PagedResponseDto;
import com.backend.threadbit.dto.PurchaseDto;
import com.backend.threadbit.model.Bid;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Purchase;
import com.backend.threadbit.model.Size;
import com.backend.threadbit.model.Status;

import java.util.List;

public interface ItemService {
    // Original methods
    List<Item> getAllItems();
    List<Item> getItemsByCategory(Integer categoryId);
    List<Item> getItemsBySeller(String sellerId);
    List<Item> getItemByUsername(String sellerUsername);
    Item getItemById(String id);
    Item createItem(ItemDto itemDto);
    Item updateItemStatus(String id, Status status);

    // Pagination methods
    PagedResponseDto<Item> getAllItems(int page, int size, String sortBy, String sortDir);
    PagedResponseDto<Item> getItemsByCategory(Integer categoryId, int page, int size, String sortBy, String sortDir);
    PagedResponseDto<Item> getItemsBySeller(String sellerId, int page, int size, String sortBy, String sortDir);
    PagedResponseDto<Item> getItemsByStatus(Status status, int page, int size, String sortBy, String sortDir);

    // Search methods
    PagedResponseDto<Item> searchItems(String keyword, int page, int size, String sortBy, String sortDir);

    // Combined search and filter
    PagedResponseDto<Item> getItems(String keyword, Integer categoryId, Status status, Size itemSize, String sellerId,
                                   String sellerUsername, int page, int size, String sortBy, String sortDir);

    // Instant buy methods
    Item createInstantBuyItem(ItemDto itemDto);
    Purchase purchaseItem(PurchaseDto purchaseDto);
    List<Purchase> getPurchasesByBuyer(String buyerId);
    PagedResponseDto<Purchase> getPurchasesByBuyer(String buyerId, int page, int size, String sortBy, String sortDir);
    PagedResponseDto<Item> getAvailableInstantBuyItems(String keyword, int page, int size, String sortBy, String sortDir);

}
