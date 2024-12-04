package com.gym.crm.application.controller;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.rest.dto.Error;
import com.gym.crm.application.rest.dto.LoginChangeRequest;
import com.gym.crm.application.rest.dto.LoginCredentials;
import com.gym.crm.application.security.UserAuthenticator;
import com.gym.crm.application.service.facade.ServiceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final ServiceFacade serviceFacade;
    private final UserAuthenticator userAuthenticator;

    /**
     * POST /login : User login
     *
     * @param loginCredentials  (required)
     * @return Login successful (status code 200)
     *         or Bad Request (status code 400)
     *         or Unathorized (status code 401)
     *         or Trainer not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "login",
            summary = "User login",
            tags = { "Auth" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unathorized", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Trainer not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PostMapping("/login")
    public String login(@NonNull @RequestBody LoginCredentials loginCredentials) {
        AuthenticationInfo auth = new AuthenticationInfo(loginCredentials.getUsername(), loginCredentials.getPassword());

        return userAuthenticator.authenticate(auth);
    }

    /**
     * PUT /login-change : Change user login credentials
     *
     * @param loginChangeRequest  (required)
     * @return Login successfully changed (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or User not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "changeLoginCredentials",
            summary = "Change user login credentials",
            tags = { "Auth" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successfully changed"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "User not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PutMapping("/login-change")
    public void loginChange(@RequestBody LoginChangeRequest loginChangeRequest) {
        serviceFacade.changeUserLogin(loginChangeRequest);
    }

    @Operation(
            operationId = "logout",
            summary = "User logout",
            tags = { "Auth" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout successful"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unathorized", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        return userAuthenticator.userLogout(authorizationHeader);
    }

}
