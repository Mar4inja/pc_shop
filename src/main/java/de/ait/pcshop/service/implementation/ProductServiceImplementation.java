package de.ait.pcshop.service.implementation;

import de.ait.pcshop.model.Product;
import de.ait.pcshop.repository.ProductRepository;
import de.ait.pcshop.repository.UserRepository;
import de.ait.pcshop.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    @Override
    public Product addProduct(Product product) {
        product.setId(null);

        if (product.getType() == null || product.getType().isEmpty()
                || product.getModel() == null || product.getModel().isEmpty()
                || product.getDescription() == null || product.getDescription().isEmpty()
                || product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("All fields are required");
        }
        if (productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product already exists");
        }
        product.setProductRegistrationDate(LocalDateTime.now());
        product.setIsActiv(true);
        return productRepository.save(product);
    }


    @Override
    public Product getProductById(Long id) {
        if (productRepository.existsById(id)) {
            return productRepository.findById(id).get();
        }
        return null;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @Override
    public List<Product> getProductsByType(String type) {
        return productRepository.findAll().stream()
                .filter(product -> product.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByPrice(BigDecimal price) {
        return productRepository.findAll().stream()
                .filter(product -> product.getPrice().compareTo(price) == 0)
                .collect(Collectors.toList());
    }

    @Override
    public Product updateProduct(Product updatedProduct) {
        Optional<Product> optionalCurrentProduct = productRepository.findById(updatedProduct.getId());

        if (!optionalCurrentProduct.isPresent()) {
            throw new IllegalArgumentException("Product with ID: " + updatedProduct.getId() + " is not found");
        }

        Product currentProduct = optionalCurrentProduct.get();

        if (updatedProduct.getType() != null) {
            currentProduct.setType(updatedProduct.getType());
        }
        if (updatedProduct.getModel() != null) {
            currentProduct.setModel(updatedProduct.getModel());
        }
        if (updatedProduct.getDescription() != null) {
            currentProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null) {
            currentProduct.setPrice(updatedProduct.getPrice());
        }

        return productRepository.save(currentProduct);
    }

    @Override
    public Product deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            Product productToDelete = productRepository.findById(id).orElse(null);
            productRepository.deleteById(id);
            return productToDelete;
        } else {
            return null;
        }
    }

    @Override
    public void disableProduct(Long id) {
        Product currentProduct = productRepository.findById(id).orElse(null);
        if (currentProduct != null) {
            currentProduct.setIsActiv(false);
            productRepository.save(currentProduct);
        }
    }

    @Override
    public void enableProduct(Long id) {
        Product currentProduct = productRepository.findById(id).orElse(null);
        if (currentProduct != null) {
            currentProduct.setIsActiv(true);
            productRepository.save(currentProduct);
        }
    }
}