package de.ait.pcshop.repository;

import de.ait.pcshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByTitle(String roleUser);
}
