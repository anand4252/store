package com.techopact.store.IT;

import com.techopact.store.entities.Item;
import com.techopact.store.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private String port;


    @BeforeEach
    void setUp() {
        List<Item> items = new ArrayList<>();
    }


    @Test
    void when_items_endpoint_invoked_should_return_allItems() throws MalformedURLException {
        final int initialInventorySize = 4;
        ResponseEntity<List<Item>> response =
                restTemplate.exchange(BASE_URL + port + "/store/v1/items",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Item>>() {
                        });
        assertEquals(initialInventorySize, response.getBody().size());
    }

    @Test
    void when_itemId_is_passed_the_appropriate_item_should_be_returned() throws MalformedURLException {
        final Item expectedItem = Item.builder()
                .name("Protein bar")
                .description("28gm protein bar")
                .price(new BigDecimal("20"))
                .build();

        ResponseEntity<Item> response = restTemplate.getForEntity(
                new URL(BASE_URL + port + "/store/v1/item/2").toString(), Item.class);
        assertEquals(expectedItem, response.getBody());
    }

    @Test
    void when_itemId_passed_and_item_available_to_the_buy_endpoint_should_return_success_message() throws MalformedURLException {
        final String expectedMessage = "ORDER_PLACED";

        ResponseEntity<String> response = restTemplate.exchange(
                new URL(BASE_URL + port + "/store/v1/item/2/order").toString(), HttpMethod.PUT, null, String.class);
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void when_itemId_passed_and_item_not_available_to_Order_endpoint_should_return_out_of_stock_message() throws MalformedURLException {
//        final Optional<Item> optionalItem = Optional.of(Item.builder().quantity(0).build());
//        final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
//        Mockito.when(itemRepository.findById(any())).thenReturn(optionalItem);
//        System.out.println("!!!!!!!! Quantity "+ itemRepository.findById(1L).get().getQuantity());

        final String expectedMessage = "OUT_OF_STOCK";
        ResponseEntity<String> response = restTemplate.exchange(
                new URL(BASE_URL + port + "/store/v1/item/4/order").toString(), HttpMethod.PUT, null, String.class);
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void when_invalid_itemId_passed_to_the_order_endpoint_should_return_invalid() throws MalformedURLException {
        final String expectedMessage = "INVALID";

        ResponseEntity<String> response = restTemplate.exchange(
                new URL(BASE_URL + port + "/store/v1/item/20/order").toString(), HttpMethod.PUT, null, String.class);
        assertEquals(expectedMessage, response.getBody());
    }


}
