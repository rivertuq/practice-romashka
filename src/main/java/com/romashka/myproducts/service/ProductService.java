package com.romashka.myproducts.service;

import com.romashka.myproducts.exception.ProductNotFoundException;
import com.romashka.myproducts.model.Product;
import com.romashka.myproducts.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Метод для получения товара по ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар под номером " + id + " не найден."));
    }

    // Метод для получения всех товаров с фильтрацией и сортировкой
    public List<Product> getFilteredAndSortedProducts(String name, Double minPrice, Double maxPrice, Boolean inStock,
                                                      String sortBy, String sortOrder, int page, int size) {

        // Сортировка
        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Order.desc(sortBy));
        }

        // Пагинация
        Pageable pageable = PageRequest.of(page, size, sort);

        if (name == null) name = "";  // Если имя не задано, ищем все товары
        if (minPrice == null) minPrice = 0.0;  // Минимальная цена
        if (maxPrice == null) maxPrice = Double.MAX_VALUE;  // Максимальная цена
        if (inStock == null) inStock = true;  // Фильтровать товары в наличии

        // Получаем товары с фильтрацией и пагинацией из репозитория
        return productRepository.findByNameContainingIgnoreCaseAndPriceBetweenAndInStock(
                name, minPrice, maxPrice, inStock, pageable);
    }

    // Метод для создания нового товара
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Метод для обновления товара
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setInStock(updatedProduct.isInStock());
        return productRepository.save(existingProduct);
    }

    // Метод для удаления товара
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
