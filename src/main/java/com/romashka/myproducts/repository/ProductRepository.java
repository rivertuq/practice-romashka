package com.romashka.myproducts.repository;

import com.romashka.myproducts.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCaseAndPriceBetweenAndInStock(
            String name, Double minPrice, Double maxPrice, Boolean inStock, Pageable pageable);

}
