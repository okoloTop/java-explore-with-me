package ru.practicum.evm.category.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.category.dto.CategoryDto;
import ru.practicum.evm.category.dto.SavedCategoryDto;
import ru.practicum.evm.category.service.CategoryService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @ResponseStatus(CREATED)
    @PostMapping("/categories")
    public CategoryDto saveCategory(@Valid @RequestBody SavedCategoryDto savedCategoryDto) {
        return categoryService.saveCategory(savedCategoryDto);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                      @PathVariable Long catId) {
        return categoryService.updateCategory(catId, categoryDto);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }
}
