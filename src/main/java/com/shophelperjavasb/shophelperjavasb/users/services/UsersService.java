package com.shophelperjavasb.shophelperjavasb.users.services;

import com.shophelperjavasb.shophelperjavasb.users.dto.ProfileDto;
import com.shophelperjavasb.shophelperjavasb.users.dto.UserDto;
import com.shophelperjavasb.shophelperjavasb.users.dto.UsersPage;
import com.shophelperjavasb.shophelperjavasb.users.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface UsersService {
    UsersPage getAll();

    ProfileDto getProfile(int currentUserId);

    UserDto getUser(int userId);


}
