package com.plurasight.data;

import com.plurasight.models.Category;

import java.util.List;

public interface CategoryDao
{
    List<Category> getAllCategories();
    Category getById(int categoryId);
    Category create(Category category);
    Category update(int categoryId, Category category);
    void delete(int categoryId);
}