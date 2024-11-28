package com.romashka.myproducts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romashka.myproducts.controller.ProductController;
import com.romashka.myproducts.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetProductById() throws Exception {
        // Создаем товар
        Product product = new Product("Товар 1", "Описание товара 1", 100.0, true);
        product.setId(1L);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated());

        // Теперь обновляем товар
        Product updatedProduct = new Product("Обновленный товар", "Новое описание товара", 75.0, false);
        updatedProduct.setId(1L);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedProduct)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Обновленный товар"))
                .andExpect(jsonPath("$.price").value(75.0))
                .andExpect(jsonPath("$.inStock").value(false));
    }


    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product("Товар 1", "Описание товара 1", 100.0, true);
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())  // Ожидаем успешное создание
                .andExpect(jsonPath("$.name").value("Товар 1"))  // Проверяем имя
                .andExpect(jsonPath("$.price").value(100.0))  // Проверяем цену
                .andExpect(jsonPath("$.inStock").value(true))  // Проверяем наличие товара
                .andExpect(jsonPath("$.id").value(1));  // Проверяем ID, который будет равен 1
    }

    @Test
    public void testUpdateProduct() throws Exception {

        Product product = new Product("Товар 1", "Описание товара 1", 100.0, true);
        product.setId(1L);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated());

        // Обновляем товар
        Product updatedProduct = new Product("Обновленный товар", "Новое описание товара", 75.0, false);
        updatedProduct.setId(1L);

        String updatedProductJson = new ObjectMapper().writeValueAsString(updatedProduct);

        // Обновляем товар и проверяем новый статус и данные
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Обновленный товар"))
                .andExpect(jsonPath("$.price").value(75.0))
                .andExpect(jsonPath("$.inStock").value(false))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}