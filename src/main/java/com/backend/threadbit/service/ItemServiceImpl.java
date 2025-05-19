package com.backend.threadbit.service;


import com.backend.threadbit.repository.ItemRepository;
import com.backend.threadbit.dto.ItemDto;
import com.backend.threadbit.dto.PagedResponseDto;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Status;
import com.backend.threadbit.model.User;
import com.backend.threadbit.repository.CategoryRepository;
import com.backend.threadbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByCategory(String categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Item> getItemsBySeller(String sellerId) {
        return itemRepository.findBySellerId(sellerId);
    }

    @Override
    public Item getItemById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
    }

    @Override
    public Item createItem(ItemDto itemDto) {
        // Validate that seller exists
        userRepository.findById(itemDto.getSellerId())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        // Validate that category exists
        categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));

        // Create and save the item
        Item item = Item.builder()
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .brand(itemDto.getBrand())
                .size(itemDto.getSize())
                .condition(itemDto.getCondition())
                .color(itemDto.getColor())
                .startingPrice(itemDto.getStartingPrice())
                .currentPrice(itemDto.getStartingPrice()) // Initially set to starting price
                .imageUrls(itemDto.getImageUrls())
                .sellerId(itemDto.getSellerId())
                .categoryId(itemDto.getCategoryId())
                .originalPrice(itemDto.getOriginalPrice())
                .buyNowPrice(itemDto.getBuyNowPrice())
                .endTime(itemDto.getEndTime())
                .status(Status.ACTIVE) // Default status
                .build();

        return itemRepository.save(item);
    }

    @Override
    public Item updateItemStatus(String id, Status status) {
        Item item = getItemById(id);
        item.setStatus(status);
        return itemRepository.save(item);
    }

    @Override
    public PagedResponseDto<Item> getAllItems(int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.findAll(pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> getItemsByCategory(String categoryId, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.findByCategoryId(categoryId, pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> getItemsBySeller(String sellerId, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.findBySellerId(sellerId, pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> getItemsByStatus(Status status, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.findByStatus(status, pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> searchItems(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.searchByTitleOrDescription(keyword, pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> getItems(String keyword, String categoryId, Status status, String sellerId, 
                                          String sellerUsername, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage;

        // Handle search by seller username
        if (sellerUsername != null && !sellerUsername.isEmpty()) {
            List<User> sellers = userRepository.findByUsernameContaining(sellerUsername);
            if (sellers.isEmpty()) {
                // Return empty page if no sellers found
                return new PagedResponseDto<>(new ArrayList<>(), page, size, 0, 0, true);
            }

            List<String> sellerIds = sellers.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());

            itemPage = itemRepository.findBySellerIds(sellerIds, pageable);
            return createPagedResponse(itemPage);
        }

        // Handle combined search and filter
        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && !categoryId.isEmpty()) {
                itemPage = itemRepository.searchByTitleOrDescriptionAndCategory(keyword, categoryId, pageable);
            } else if (status != null) {
                itemPage = itemRepository.searchByTitleOrDescriptionAndStatus(keyword, status, pageable);
            } else {
                itemPage = itemRepository.searchByTitleOrDescription(keyword, pageable);
            }
        } else {
            // Handle filter only
            if (categoryId != null && !categoryId.isEmpty()) {
                itemPage = itemRepository.findByCategoryId(categoryId, pageable);
            } else if (status != null) {
                itemPage = itemRepository.findByStatus(status, pageable);
            } else if (sellerId != null && !sellerId.isEmpty()) {
                itemPage = itemRepository.findBySellerId(sellerId, pageable);
            } else {
                itemPage = itemRepository.findAll(pageable);
            }
        }

        return createPagedResponse(itemPage);
    }

    private Sort createSort(String sortBy, String sortDir) {
        Sort sort = Sort.by(sortBy);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        return sort;
    }

    private PagedResponseDto<Item> createPagedResponse(Page<Item> page) {
        return PagedResponseDto.<Item>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
