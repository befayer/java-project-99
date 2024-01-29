package hexlet.code.controller;

import hexlet.code.dto.UserRequestDto;
import hexlet.code.dto.UserResponseDto;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Deletes a user by its ID.
     *
     * @param id The ID of the user to be deleted.
     * @return ResponseEntity indicating the success of the deletion (HTTP 204 No Content).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "@userService.findById(#id).getEmail() == authentication.name")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Finds all users.
     *
     * @return ResponseEntity with a list of user response DTOs and headers including total count.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> users = userService.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(users.size()));
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }

    /**
     * Finds a user by ID.
     *
     * @param id The ID of the user to be retrieved.
     * @return The user response DTO.
     */
    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    /**
     * Creates a new user.
     *
     * @param userRequestDto The request DTO containing user information.
     * @return The created user response DTO.
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> save(@RequestBody @Valid UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.save(userRequestDto), HttpStatus.CREATED);
    }

    /**
     * Updates a user by ID.
     *
     * @param id             The ID of the user to be updated.
     * @param userRequestDto The request DTO containing updated user information.
     * @return The updated user response DTO.
     */
    @PutMapping("/{id}")
    @PreAuthorize(value = "@userService.findById(#id).getEmail() == authentication.name")
    public ResponseEntity<UserResponseDto> updateById(@PathVariable Long id,
                                                      @Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.updateById(id, userRequestDto));
    }
}
