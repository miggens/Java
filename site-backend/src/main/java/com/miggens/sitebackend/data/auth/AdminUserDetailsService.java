package com.miggens.sitebackend.data.auth;

import com.miggens.sitebackend.data.AdminUser;
import com.miggens.sitebackend.data.AdminUserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AdminUserDetailsService implements UserDetailsService{

    private AdminUserRepository adminUserRepository;

    public AdminUserDetailsService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AdminUser au = this.adminUserRepository.findByUsername(s);

        if (au == null) {
            throw new UsernameNotFoundException("Cannot find user "+s);
        }

        return new AdminUserPrincipal(au);
    }
}
