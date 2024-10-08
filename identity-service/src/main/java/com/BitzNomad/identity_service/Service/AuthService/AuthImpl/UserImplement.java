package com.BitzNomad.identity_service.Service.AuthService.AuthImpl;

import com.BitzNomad.identity_service.DtoReponese.ApiResponse;
import com.BitzNomad.identity_service.DtoReponese.UserReponese;
import com.BitzNomad.identity_service.DtoRequest.MailInfo;
import com.BitzNomad.identity_service.DtoRequest.UserCreateRequest;
import com.BitzNomad.identity_service.DtoRequest.UserUpdateRequest;
import com.BitzNomad.identity_service.Exception.AppException;
import com.BitzNomad.identity_service.Exception.ErrorCode;
import com.BitzNomad.identity_service.Mapper.Auth.UserMapper;
import com.BitzNomad.identity_service.Respository.httpclient.CreatedInterfaceClient;
import com.BitzNomad.identity_service.Service.AuthService.UserService;
import com.BitzNomad.identity_service.Service.MailerService;

import com.BitzNomad.identity_service.contant.PredefineRole;
import com.BitzNomad.identity_service.Entity.Auth.Role;
import com.BitzNomad.identity_service.Entity.Auth.User;
import com.BitzNomad.identity_service.Respository.RoleRepository;
import com.BitzNomad.identity_service.Respository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
class UserImplement implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CreatedInterfaceClient createdInterfaceClient;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MailerService mailerService;

    @Override
    public UserReponese createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.UserExitsted);
        // Convert the UserRequest to a User entity
        User user = userMapper.UserCreateconvertToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefineRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        createdInterfaceClient.createUserProfile(request);
        // Return the saved user entity

        return userMapper.convertUserToReponese(user);
    }

    @Override
    public UserReponese getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.convertUserToReponese(user);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not Found !"));
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public User updateUser(UserUpdateRequest request) {
        User user = userMapper.UserUpdateconvertToEntity(request);
        return userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(String id) {
        User u = userRepository.findByEmail(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        u.setDeleted(true);
        userRepository.save(u);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasAuthority('WRITE_DATA')")
    public List<UserReponese> getAllUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::convertUserToReponese).toList();
    }

    @Override
    public boolean existsByUsername(String email) {
        return userRepository.existsByEmail(email);
    }


}
