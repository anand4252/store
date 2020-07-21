package com.techopact.store.IT;

import com.techopact.store.entities.AuthenticationResponse;
import com.techopact.store.entities.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private String port;

    private MultiValueMap<String, String> headers;

    @BeforeAll
    public void setUp() throws MalformedURLException {
        String authorizationJwtString = "Bearer " + generateJwt();
        headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", authorizationJwtString);
    }

    private String generateJwt() throws MalformedURLException {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Content-Type", "application/json");


        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                new URL(BASE_URL + port + "/store/login").toString(), HttpMethod.POST, new HttpEntity<>("{\"username\":\"anand\", \"password\":\"anand\"}", header), AuthenticationResponse.class);
        AuthenticationResponse requestBody = response.getBody();
        return StringUtils.isEmpty(requestBody) ? "" : requestBody.getJwt();
    }


    @Test
    void when_items_endpoint_invoked_should_return_allItems() {
        final int initialInventorySize = 4;
        ResponseEntity<List<Item>> response =
                restTemplate.exchange(BASE_URL + port + "/store/v1/items",
                        HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
                        });
        List<Item> actualItems = response.getBody();

        assertAll(
                () -> assertNotNull(actualItems),
                () -> assertEquals(initialInventorySize, actualItems.size()),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );

    }

    @Test
    void when_itemId_is_passed_the_appropriate_item_should_be_returned() throws MalformedURLException {
        final Item expectedItem = Item.builder()
                .name("Protein bar")
                .description("28gm protein bar")
                .price(20)
                .build();

        ResponseEntity<Item> response =
                restTemplate.exchange(BASE_URL + port + "/store/v1/item/2",
                        HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
                        });

        assertAll(
                () -> assertEquals(expectedItem, response.getBody()),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }

    @Test
    void when_invalid_itemId_is_passed_should_return_404_status_code() {
        ResponseEntity<Item> response =
                restTemplate.exchange(BASE_URL + port + "/store/v1/item/200",
                        HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
                        });

        final Item emptyItem = response.getBody();
        assertAll(
                () -> assertNotNull(emptyItem),
                () -> assertNull(emptyItem.getName()),
                () -> assertNull(emptyItem.getDescription()),
                () -> assertEquals(0, emptyItem.getPrice()),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode())
        );
    }

    @Test
    void when_itemId_passed_and_item_available_to_the_buy_endpoint_should_return_success_message() throws MalformedURLException {
        final String expectedMessage = "ORDER_PLACED";

        ResponseEntity<String> response = restTemplate.exchange(
                new URL(BASE_URL + port + "/store/v1/item/2/order").toString(), HttpMethod.PUT, new HttpEntity<>(headers), String.class);
        assertAll(
                () -> assertEquals(expectedMessage, response.getBody()),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }

    @Test
    void when_itemId_passed_and_item_not_available_to_Order_endpoint_should_return_out_of_stock_message() throws MalformedURLException {
        final String expectedMessage = "OUT_OF_STOCK";

        ResponseEntity<String> response = restTemplate.exchange(
                new URL(BASE_URL + port + "/store/v1/item/4/order").toString(), HttpMethod.PUT, new HttpEntity<>(headers), String.class);
        assertAll(
                () -> assertEquals(expectedMessage, response.getBody()),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }

    @Test
    void when_invalid_itemId_passed_to_the_order_endpoint_should_return_invalid() throws MalformedURLException {
        final String expectedMessage = "INVALID";

        ResponseEntity<String> response = restTemplate.exchange(
                new URL(BASE_URL + port + "/store/v1/item/20/order").toString(), HttpMethod.PUT, new HttpEntity<>(headers), String.class);
        assertAll(
                () -> assertEquals(expectedMessage, response.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode())
        );
    }
}
