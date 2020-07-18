package com.techopact.store.repository;

import com.techopact.store.entities.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ItemRepository extends CrudRepository<Item, Long> {

    List<Item> findAll();
}
