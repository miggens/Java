package com.miggens.sitebackend.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, String> {
    AdminUser findByUsername(String username);
}
