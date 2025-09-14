package com.example.billing.Service.Item;

import com.example.billing.Entity.CategoryEntity;
import com.example.billing.Entity.ItemEntity;
import com.example.billing.Repository.CategoryRepository;
import com.example.billing.Repository.ItemRepository;
import com.example.billing.Service.File.FileUploadService;
import com.example.billing.io.ItemRequest;
import com.example.billing.io.ItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemService implements ItemImpl{
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public ItemResponse add(ItemRequest request, MultipartFile file) throws IOException {
        String imageUrl = fileUploadService.uploadFile(file);
        ItemEntity itemEntity = convertToEntity(request);
       CategoryEntity categoryEntity =  categoryRepository.
                findByCategoryId(request.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category are not found"+request.getCategoryId()));
        itemEntity.setCategory(categoryEntity);
        itemEntity.setImageUrl(imageUrl);
        itemRepository.save(itemEntity);
       return  convertToResponse(itemEntity);

    }
    public ItemEntity convertToEntity(ItemRequest request){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(request.getName());
        itemEntity.setItemId(UUID.randomUUID().toString());
        itemEntity.setDescription(request.getDescription());
        itemEntity.setPrice(request.getPrice());
        return itemEntity;
    }
    public ItemResponse convertToResponse(ItemEntity itemEntity){
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setItemId(itemEntity.getItemId());
        itemResponse.setName(itemEntity.getName());
        itemResponse.setDescription(itemEntity.getDescription());
        itemResponse.setPrice(itemEntity.getPrice());
        itemResponse.setImgUrl(itemEntity.getImageUrl());
        itemResponse.setCategoryName(itemEntity.getCategory().getName());
        itemResponse.setCategoryId(itemEntity.getCategory().getCategoryId());
        itemResponse.setCreatedAt(itemEntity.getCreatedAt());
        itemResponse.setUpdatedAt(itemEntity.getUpdatedAt());
        return itemResponse;
    }
    @Override
    public List<ItemResponse> fetchItems() {
        return
                itemRepository.findAll().stream()
                        .map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteItem(String itemId) {
    ItemEntity exisitingItem = itemRepository.findByItemId(itemId).orElseThrow(()->new RuntimeException("Item not found"+itemId));
     boolean isFileDelete = fileUploadService.deleteFile(exisitingItem.getImageUrl());
     if(isFileDelete){
         itemRepository.delete(exisitingItem);
     }else{
         throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Unble to delete items");
     }
    }
}
