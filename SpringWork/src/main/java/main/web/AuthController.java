package main.web;

import main.entity.User;
import main.exception.InvalidJwtAuthenticationException;
import main.repository.UserRepository;
import main.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/wc/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder pwdEncoder;

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody AuthRequest request) {
        try {
            String name = request.getUsername();
            User user = userRepository.findUserByUsername(name)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = "";
            if (pwdEncoder.matches(request.getPassword(), user.getPassword())) {
                token = jwtTokenProvider.createToken(
                        name,
                        user.getRoles()
                );
            } else {
                throw new BadCredentialsException("Invalid password");
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("username", name);
            model.put("token", token);

            return ResponseEntity.ok(model);

        } catch (AuthenticationException exception) {
            throw new BadCredentialsException("Invalid data");
        }
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody AuthRequest request) {
        String username = request.getUsername();

        if (!userRepository.findUserByUsername(username).isPresent() || !username.isEmpty()) {
            String password = request.getPassword();
            List<String> role = Collections.singletonList("ROLE_USER");
            userRepository.save(new User(username, pwdEncoder.encode(password), role));

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("password", password);

            return ResponseEntity.ok(model);
        } else {
            throw new BadCredentialsException("Incorrect username");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/checkToken/{token}")
    public ResponseEntity checkValidateToken(@PathVariable String token) {
        Map<Object, Object> model = new HashMap<>();
        try {
            boolean check = jwtTokenProvider.validateToken(token);
            model.put("validate", check);
            return ResponseEntity.ok(model);

        } catch (InvalidJwtAuthenticationException exception) {
            model.put("validate", false);
            return ResponseEntity.ok(model);
        }
    }

    @GetMapping("/getRoleCurrentUser/{token}")
    public ResponseEntity getRoleCurrentUser(@PathVariable String token) {
        String role;
        try {
            Optional<User> user = userRepository.findUserByUsername(jwtTokenProvider.getUserName(token));
            role = user.get().getRoles().get(0);
        } catch(Exception exception) {
            role = "ROLE_ANONYMOUS";
        }
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
