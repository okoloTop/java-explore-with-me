package ru.practicum.evm.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evm.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByName(String name);
}
