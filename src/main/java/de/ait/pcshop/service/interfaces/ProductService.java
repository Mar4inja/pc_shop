package de.ait.pcshop.service.interfaces;

import de.ait.pcshop.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getProductsByType(String type);

    List<Product> getProductsByPrice(BigDecimal price);

    Product updateProduct(Product updatedProduct);

    Product deleteProduct(Long id);

    void disableProduct(Long id);

    void enableProduct(Long id);
    }
