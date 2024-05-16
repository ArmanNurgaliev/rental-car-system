package ru.arman.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arman.dto.AuthenticationRequest;
import ru.arman.dto.UserDto;
import ru.arman.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest request) {
        return userService.authenticate(request);
    }

    @PostMapping("/make-admin")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> makeUserAnAdmin(@RequestParam Long user_id) {
        return ResponseEntity.ok(userService.makeAdmin(user_id));
    }
}
