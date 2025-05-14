package com.backend.threadbit.service;


import com.backend.threadbit.dto.ItemDto;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Status;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();
    List<Item> getItemsByCategory(String categoryId);
    List<Item> getItemsBySeller(String sellerId);
    Item getItemById(String id);
    Item createItem(ItemDto itemDto);
    Item updateItemStatus(String id, Status status);
}