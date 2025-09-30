package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.AccountRole;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
}
