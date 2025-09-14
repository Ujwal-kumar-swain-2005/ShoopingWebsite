package com.example.billing.Controller;

import com.example.billing.Service.Item.ItemService;
import com.example.billing.io.ItemRequest;
import com.example.billing.io.ItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class ItemController {
    @Autowired
    private  ItemService itemService;
    @PostMapping("/admin/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse addItem(@RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("categoryId") String categoryId,
                                @RequestParam("price") String priceStr,
                                @RequestPart("file") MultipartFile file) {
        try {
            BigDecimal price = new BigDecimal(priceStr);

            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setName(name);
            itemRequest.setDescription(description);
            itemRequest.setCategoryId(categoryId);
            itemRequest.setPrice(price);

            return itemService.add(itemRequest, file);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price format");
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Error occurred while processing the request: " + ex.getMessage());
        }
    }

    @GetMapping("/items")
    public List<ItemResponse> readItems() {
        return itemService.fetchItems();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/items/{itemId}")
    public void removeItem(@PathVariable String itemId) {
        try {
            itemService.deleteItem(itemId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
    }
}