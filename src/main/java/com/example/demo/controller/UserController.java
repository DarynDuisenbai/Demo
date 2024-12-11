package com.example.demo.controller;

import com.example.demo.dto.request.user.*;
import com.example.demo.dto.responce.UserDto;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.spec.ImageService;
import com.example.demo.service.spec.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

import static com.mongodb.internal.authentication.AwsCredentialHelper.LOGGER;

@Tag(name = "User Controller", description = "Endpoints for managing user operations")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ImageService imageService;

    @Operation(summary = "Get user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile(@RequestParam String IIN)
            throws UserNotFoundException {
        UserDto userProf = userService.getProfile(IIN);
        return ResponseEntity.ok(userProf);
    }

    @Operation(summary = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password format"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestParam String email,
                                            @RequestParam String oldPass,
                                            @RequestParam String newPass)
            throws UserNotFoundException, InvalidPasswordException {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword(newPass);
        changePasswordRequest.setEmail(email);
        changePasswordRequest.setOldPassword(oldPass);
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

    @Operation(summary = "Delete user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestParam String email)
            throws UserNotFoundException {
        DeleteAccountRequest deleteAccountRequest = new DeleteAccountRequest();
        deleteAccountRequest.setEmail(email);
        userService.deleteAccount(deleteAccountRequest);
        return ResponseEntity.ok("User has successfully deleted.");
    }

    @Operation(summary = "Forgot password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset link sent to email"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/reset-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest)
            throws UserNotFoundException,InvalidPasswordException {
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

    @Operation(summary = "All names")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All names retrieved")
    })
    @GetMapping("/allNames")
    public ResponseEntity<List<String>> allNames() {
        List<String> allNames = userService.allNames();
        return ResponseEntity.ok(allNames);
    }

    @Operation(summary = "All users within same department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users retrieved")
    })
    @GetMapping("/depRelated")
    public ResponseEntity<List<UserDto>> depRelated(@RequestParam String department) {
        List<UserDto> allUsers = userService.getAllWithinDep(department);
        return ResponseEntity.ok(allUsers);
    }

    @Operation(summary = "All users that has same job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users retrieved")
    })
    @GetMapping("/jobRelated")
    public ResponseEntity<List<UserDto>> jobRelated(@RequestParam String jobTitle) {
        List<UserDto> allUsers = userService.getAllWithinJob(jobTitle);
        return ResponseEntity.ok(allUsers);
    }

    @Operation(summary = "Edit profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile successfully edited."),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/editProfile")
    public ResponseEntity<?> editProfiled(@RequestParam String IIN,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String surname)
            throws UserNotFoundException {
        EditProfileRequest editProfileRequest = new EditProfileRequest();
        editProfileRequest.setIIN(IIN);
        editProfileRequest.setName(name);
        editProfileRequest.setSurname(surname);
        userService.editProfile(editProfileRequest);
        return ResponseEntity.ok("Profile has successfully edited.");
    }

    @Operation(summary = "All users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users retrieved")
    })
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }


    @Operation(
            summary = "Add profile image",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(
                                    implementation = ProfileImageUploadRequest.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile image uploaded successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    @PatchMapping("/image")
    public ResponseEntity<?> uploadProfileImage(@ModelAttribute ProfileImageUploadRequest request) throws Exception {

        String imageUrl = imageService.uploadImageToStorage(request.getImage());
        userService.uploadProfileImage(request.getIin(), imageUrl);
        return ResponseEntity.ok("Profile image uploaded successfully.");
    }

    @Operation(
            summary = "Get profile image",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "Image not found")
            }
    )
    @GetMapping("/image/{fileId}")
    public ResponseEntity<?> getProfileImage(@PathVariable String fileId) {
        try {
            Resource image = imageService.getImage(fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404).body("Image not found");
        }
    }
}
