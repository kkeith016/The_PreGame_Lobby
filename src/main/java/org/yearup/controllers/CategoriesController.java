package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    // Constructor injection (preferred over field injection)
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao)
    {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // GET http://localhost:8080/categories
    @GetMapping
    public List<Category> getAll()
    {
        return categoryDao.getAllCategories();
    }

    // GET http://localhost:8080/categories/{id}
    @GetMapping("/{id}")
    public Category getById(@PathVariable int id)
    {
        return categoryDao.getById(id);
    }

    // GET http://localhost:8080/categories/{categoryId}/products
    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsByCategoryId(@PathVariable int categoryId)
    {
        return productDao.listByCategoryId(categoryId);
    }

    // POST http://localhost:8080/categories
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category addCategory(@RequestBody Category category)
    {
        return categoryDao.create(category);
    }

    // PUT http://localhost:8080/categories/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        categoryDao.update(id, category);
    }

    // DELETE http://localhost:8080/categories/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable int id)
    {
        categoryDao.delete(id);
    }
}