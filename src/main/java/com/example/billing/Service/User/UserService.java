package com.example.billing.Service.User;

import com.example.billing.io.UserRequest;
import com.example.billing.io.UserResponse;

import java.util.List;

public interface UserService {
   UserResponse createUser(UserRequest request);
   String getUserRole(String email);
   List<UserResponse> readUsers();

   boolean emailExists(String email);

   void deleteUser(String id);
}
