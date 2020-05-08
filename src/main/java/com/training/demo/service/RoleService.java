package com.training.demo.service;

import com.training.demo.entity.Role;
import com.training.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

//import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class RoleService {

    private final RoleRepository roleRepository;


    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("cне можу знайти такої ролі"));

    }

    public List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

//    private Long getAdminAccount() {
//        Role admin = roleRepository.findByName("Адмін")
//                .orElseThrow(() -> new UsernameNotFoundException("немає адміна"));
//
//        return admin.getId();
//    }


}
