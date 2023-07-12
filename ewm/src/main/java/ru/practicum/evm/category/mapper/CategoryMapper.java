package ru.practicum.evm.category.mapper;

import org.mapstruct.Mapper;
import ru.practicum.evm.category.dto.CategoryDto;
import ru.practicum.evm.category.dto.SavedCategoryDto;
import ru.practicum.evm.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(SavedCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtos(List<Category> categoryList);
}
