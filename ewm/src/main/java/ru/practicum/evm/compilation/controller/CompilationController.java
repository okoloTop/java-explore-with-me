package ru.practicum.evm.compilation.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.compilation.dto.CompilationDto;
import ru.practicum.evm.compilation.service.CompilationService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "10", required = false) Integer size,
                                                @RequestParam(defaultValue = "0", required = false) Integer from,
                                                @RequestParam(defaultValue = "false", required = false) Boolean pinned) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }
}
