package ru.practicum.evm.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.evm.category.dto.CategoryDto;
import ru.practicum.evm.category.dto.SavedCategoryDto;
import ru.practicum.evm.category.exception.CategoryNotEmptyException;
import ru.practicum.evm.category.exception.CategoryNotExistException;
import ru.practicum.evm.category.mapper.CategoryMapper;
import ru.practicum.evm.category.repository.CategoryRepository;
import ru.practicum.evm.event.repository.EventRepository;
import ru.practicum.evm.user.exception.NameExistException;

import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto saveCategory(SavedCategoryDto savedCategoryDto) {
        if (categoryRepository.existsByName(savedCategoryDto.getName()))
            throw new NameExistException("Категория с именем " + savedCategoryDto.getName() + " не может быть добавлена");
        var entity = categoryMapper.toCategory(savedCategoryDto);
        var saved = categoryRepository.save(entity);
        return categoryMapper.toCategoryDto(saved);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        var category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotExistException("Категория#" + id + " не существует"));
        var categoryName = category.getName();
        if (categoryRepository.existsByName(categoryDto.getName()) && !categoryName.contains(categoryDto.getName()))
            throw new NameExistException("Категория с именем " + categoryDto.getName() + " не может быть обновлена");
        category.setName(categoryDto.getName());
        var saved = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(saved);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (eventRepository.existsByCategoryId(id))
            throw new CategoryNotEmptyException("Категория#" + id + " не пустая");
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryMapper.toCategoryDtos(categoryRepository.findAll(of(from / size, size)).toList());
    }

    @Override
    public CategoryDto getCategory(Long id) {
        var category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotExistException("Категория#" + id + " не существует"));
        return categoryMapper.toCategoryDto(category);
    }
}
