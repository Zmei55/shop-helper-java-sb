package com.shophelperjavasb.shophelperjavasb.products.services.impl;

import com.shophelperjavasb.shophelperjavasb.exceptions.NotFoundException;
import com.shophelperjavasb.shophelperjavasb.products.dto.ProductDTO;
import com.shophelperjavasb.shophelperjavasb.products.dto.ProductPage;
import com.shophelperjavasb.shophelperjavasb.products.dto.ProductProfileDTO;
import com.shophelperjavasb.shophelperjavasb.products.model.Image;
import com.shophelperjavasb.shophelperjavasb.products.model.Product;
import com.shophelperjavasb.shophelperjavasb.products.repositories.ProductsRepository;
import com.shophelperjavasb.shophelperjavasb.products.services.ProductsService;
import com.shophelperjavasb.shophelperjavasb.users.model.User;
import com.shophelperjavasb.shophelperjavasb.users.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;

    @Override
    public List<Product> listProducts (String productName) {
        if (productName != null) return productsRepository.findByName(productName);
        return productsRepository.findAll();
    }

    @Override
    public ProductPage getAll() {
        List<Product> products = productsRepository.findAll();

        return ProductPage.builder()
                .products(ProductDTO.from(products))
                .build();
    }
    @Override
    public ProductProfileDTO getProfile(Long currentProductId){
        Product product = productsRepository.findById(currentProductId)
                .orElseThrow(IllegalArgumentException::new);

        return ProductProfileDTO.from(product);
    }

    @Override
    public ProductDTO getProduct(Long productId) {
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product with id <" + productId+ "> not found"));

        return ProductDTO.from(product);
    }

    @Override
    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        log.info("Saving new Product. Title: {}; Author email: {}", product.getName(), product.getUser().getEmail());
        Product productFromDb = productsRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productsRepository.save(product);
    }

    @Override
    public User getUserByPrincipal (Principal principal) {
        if (principal == null) return new User();
        return usersRepository.findByEmail(principal.getName());
    }

    @Override
    public Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Override
    public void deleteProduct(User user, Long id) {
        Product product = productsRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().equals(user.getId())) {
//                productRepository.delete(product);
                productsRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }    }

    @Override
    public Product getProductById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }

}
