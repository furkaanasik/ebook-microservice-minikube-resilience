package com.ebook.userservice.controller;

import com.ebook.userservice.controller.doc.UserControllerDoc;
import com.ebook.userservice.dto.LoginResponseDTO;
import com.ebook.userservice.dto.UserDTO;
import com.ebook.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

    private final UserService userService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        LoginResponseDTO resp = this.userService.login(email, password);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id) {
        UserDTO resp = this.userService.getUserById(id);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        UserDTO resp = this.userService.create(userDTO);
        return ResponseEntity.ok(resp);
    }
}
