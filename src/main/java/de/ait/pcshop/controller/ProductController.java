package de.ait.pcshop.controller;

import de.ait.pcshop.model.Product;
import de.ait.pcshop.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/addProductToDB")
    public ResponseEntity<Product> addProductToDB(@RequestBody Product product) {
       return ResponseEntity.ok(productService.addProduct(product));
    }


}
