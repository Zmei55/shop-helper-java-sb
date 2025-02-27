package com.shophelperjavasb.shophelperjavasb.products.repositories;

import com.shophelperjavasb.shophelperjavasb.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    @Override
    Optional<Product> findById(Long id);

    List<Product> findByName(String productName);

    // не менять - получать продукты текущего пользователя
    List<Product> findAllByUser_Id(Long userId);
}
