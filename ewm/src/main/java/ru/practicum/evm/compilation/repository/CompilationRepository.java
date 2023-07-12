package ru.practicum.evm.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

}
