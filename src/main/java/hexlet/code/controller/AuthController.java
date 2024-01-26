package hexlet.code.controller;

import hexlet.code.dto.LoginDto;
import hexlet.code.security.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    /**
     * Handles user authentication and generates a JWT token upon successful authentication.
     *
     * @param loginDto the login information provided by the user
     * @return the generated JWT token
     */
    @PostMapping("/login")
    public String createAuthenticationToken(@RequestBody LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        return jwtUtils.generateToken(loginDto.getUsername());
    }
}
