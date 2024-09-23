package com.project.demo.rest.product;

import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    // Listar todos los productos
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Registrar un nuevo producto
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setNombre(product.getNombre());
                    existingProduct.setDescripcion(product.getDescripcion());
                    existingProduct.setPrecio(product.getPrecio());
                    existingProduct.setCantidadEnStock(product.getCantidadEnStock());
                    existingProduct.setCategory(product.getCategory());
                    return productRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return productRepository.save(product);
                });
    }

    // Borrar un producto por ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
