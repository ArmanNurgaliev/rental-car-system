package ru.arman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.arman.dto.AuthenticationRequest;
import ru.arman.dto.AuthenticationResponse;
import ru.arman.dto.UserDto;
import ru.arman.entity.AccountStatus;
import ru.arman.entity.Role;
import ru.arman.entity.User;
import ru.arman.exception.UserAlreadyExistsException;
import ru.arman.exception.UserNotFoundException;
import ru.arman.repository.RoleRepository;
import ru.arman.repository.UserRepository;
import ru.arman.utils.JwtTokenUtil;
import ru.arman.utils.UserMapper;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public String createUser(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
                });

        User user = userMapper.mapDtoToUser(userDto);
        user.setStatus(AccountStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role customer_role = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> {
                    Role createRole = new Role();
                    createRole.setName("ROLE_CUSTOMER");
                    return roleRepository.save(createRole);
                });
        user.setRoles(Set.of(customer_role));

        User savedUser = userRepository.save(user);

        return "User with name: " + savedUser.getName() + " is saved";
    }

    public String makeAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Role admin = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role createRole = new Role();
                    createRole.setName("ROLE_ADMIN");
                    return roleRepository.save(createRole);
                });
        user.getRoles().add(admin);
        userRepository.save(user);

        return "User " + user.getName() + " is admin";
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request)  {
        try {
            User user = (User) userDetailsService.loadUserByUsername(request.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            final String token = jwtTokenUtil.generateToken(user);

            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .email(user.getUsername())
                    .access_token(token)
                    .build());
        } catch (UsernameNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid Credentials!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something wrong!");
        }
    }
}
