package com.techopact.store.service;

import com.techopact.store.entities.Item;
import com.techopact.store.enums.ItemStatus;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> fetchAllItems();

    Optional<Item> fetchItemById(Long id);

    ItemStatus placeOrder(Long id);
}
