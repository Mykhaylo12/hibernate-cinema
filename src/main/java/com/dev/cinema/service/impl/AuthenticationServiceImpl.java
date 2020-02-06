package com.dev.cinema.service.impl;

import com.dev.cinema.lib.Inject;
import com.dev.cinema.lib.Service;
import com.dev.cinema.model.User;
import com.dev.cinema.service.AuthenticationService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import com.dev.cinema.util.HashUtil;
import javax.naming.AuthenticationException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private static UserService userService;
    @Inject
    private static ShoppingCartService shoppingCartService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User user = userService.findByEmail(email);
        if (user.getPassword().equals(HashUtil.hashPassword(password, user.getSalt()))) {
            return user;
        }
        throw new AuthenticationException("Login or password incorrect");
    }

    @Override
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        User userAfterRegistration = userService.add(user);
        shoppingCartService.registerNewShoppingCart(user);
        return userAfterRegistration;
    }
}
