package com.urmila.ecommerce.Repository;

import com.urmila.ecommerce.Model.AppRole;
import com.urmila.ecommerce.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
