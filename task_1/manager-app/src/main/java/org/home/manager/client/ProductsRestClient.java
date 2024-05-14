package org.home.manager.client;

import org.home.manager.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {
    List<Product> findAllProducts(String filter);

    Product createProduct(String title, String details);

    Optional<Product> findProduct(int productId);

    ResponseEntity<Void> updateProduct(int productId, String title, String details);

    void deleteProduct(int productId);

}
