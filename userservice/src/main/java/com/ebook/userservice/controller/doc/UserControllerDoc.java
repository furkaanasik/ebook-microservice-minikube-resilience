package com.ebook.userservice.controller.doc;

import com.ebook.userservice.dto.LoginResponseDTO;
import com.ebook.userservice.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "User")
public interface UserControllerDoc {

    @Operation(summary = "Login Service")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class)))
    ResponseEntity<LoginResponseDTO> login(String email, String password);

    @Operation(summary = "Get User By Id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    ResponseEntity<UserDTO> getById(Long id);

    @Operation(summary = "Create User")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    ResponseEntity<UserDTO> create(UserDTO userDTO);
}
