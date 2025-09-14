package com.example.billing.Service.User;

import com.example.billing.Entity.UserEntity;
import com.example.billing.Repository.UserRepository;
import com.example.billing.io.UserRequest;
import com.example.billing.io.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserResponse createUser(UserRequest request) {

        UserEntity newUser = convertToEntity(request);
        newUser = userRepository.save(newUser);
        return convertToResponse(newUser);
    }
    private UserResponse convertToResponse(UserEntity newUser) {
        UserResponse userResponse = new UserResponse();
         userResponse.setEmail(newUser.getEmail());
         userResponse.setName(newUser.getName());
         userResponse.setCreatedAt(newUser.getCreatedAt());
            userResponse.setUpdatedAt(newUser.getUpdatedAt());
            userResponse.setRole(newUser.getRole());
            return userResponse;
    }

    private UserEntity convertToEntity(UserRequest request) {
       UserEntity userEntity = new UserEntity();
       userEntity.setUserId(UUID.randomUUID().toString());
       userEntity.setEmail(request.getEmail());
       userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
       userEntity.setRole(request.getRole());
       userEntity.setName(request.getName());
       return userEntity;
    }

    @Override
    public String getUserRole(String email) {
        UserEntity userEntity =  userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User is not found "+email));
        return userEntity.getRole();
    }

    @Override
    public List<UserResponse> readUsers() {
        return userRepository.findAll().stream()
                .map(user -> convertToResponse(user))
                .collect(Collectors.toList());
    }
    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    @Override
    public void deleteUser(String id) {
        UserEntity user = userRepository.findByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
        userRepository.delete(user);
    }


}
