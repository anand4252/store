package com.techopact.store.service;

import com.techopact.store.entities.Item;

import java.util.List;

public interface SurgePriceService {
    List<Item> calculateSurgePrice(List<Item> items);
}
