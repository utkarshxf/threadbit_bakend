package com.backend.threadbit.repository;

import com.backend.threadbit.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByCategoryId(String categoryId);
    List<Item> findBySellerId(String sellerId);
}