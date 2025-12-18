package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductsController
{
    private final ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao)
    {
        this.productDao = productDao;
    }

    // GET /products?cat=&minPrice=&maxPrice=&subCategory=
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Product> search(
            @RequestParam(name = "cat", required = false) Integer categoryId,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "subCategory", required = false) String subCategory)
    {
        try
        {
            return productDao.search(categoryId, minPrice, maxPrice, subCategory);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Oops... our bad.",
                    ex
            );
        }
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> getById(@PathVariable int id)
    {
        try
        {
            Product product = productDao.getById(id);

            if (product == null)
            {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(product);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Oops... our bad.",
                    ex
            );
        }
    }

    // POST /products
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody Product product)
    {
        try
        {
            Product created = productDao.create(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Oops... our bad.",
                    ex
            );
        }
    }

    // PUT /products/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @RequestBody Product product)
    {
        try
        {
            Product updated = productDao.update(id, product);

            if (updated == null)
            {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(updated);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Oops... our bad.",
                    ex
            );
        }
    }

    // DELETE /products/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id)
    {
        try
        {
            Product product = productDao.getById(id);

            if (product == null)
            {
                return ResponseEntity.notFound().build();
            }

            productDao.delete(id);
            return ResponseEntity.noContent().build();
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Oops... our bad.",
                    ex
            );
        }
    }
}