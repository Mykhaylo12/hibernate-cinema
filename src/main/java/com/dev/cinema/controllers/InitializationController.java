package com.dev.cinema.controllers;

import com.dev.cinema.model.Role;
import com.dev.cinema.model.User;
import com.dev.cinema.service.RoleService;
import com.dev.cinema.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitializationController {
    private RoleService roleService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitializationController(RoleService roleService, UserService userService,
                                    PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Role userRole = new Role();
        userRole.setRoleName("USER");
        roleService.add(userRole);

        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        roleService.add(adminRole);

        User user = new User();
        user.setEmail("user@gmail.net");
        user.setPassword(passwordEncoder.encode("1234"));

        Role userRoleTest = roleService.getRoleByName("USER");
        user.addRole(userRoleTest);
        userService.add(user);

        User admin = new User();
        admin.setEmail("admin@gmail.net");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.addRole(adminRole);
        userService.add(admin);
    }
}
