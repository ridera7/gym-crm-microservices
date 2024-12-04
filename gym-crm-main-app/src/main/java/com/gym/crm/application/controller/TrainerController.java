package com.gym.crm.application.controller;

import com.gym.crm.application.dto.request.TrainerTrainingsListRequest;
import com.gym.crm.application.rest.dto.ActivateDeactivateRequest;
import com.gym.crm.application.rest.dto.Error;
import com.gym.crm.application.rest.dto.LoginCredentials;
import com.gym.crm.application.rest.dto.SuccessResponse;
import com.gym.crm.application.rest.dto.TrainerProfile;
import com.gym.crm.application.rest.dto.TrainerProfileResponse;
import com.gym.crm.application.rest.dto.TrainerTrainingsListItem;
import com.gym.crm.application.rest.dto.TrainerUpdateProfileRequest;
import com.gym.crm.application.rest.dto.UpdateTrainerProfile200Response;
import com.gym.crm.application.service.facade.ServiceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final ServiceFacade serviceFacade;

    /**
     * POST /trainer/register : Register a new trainer
     *
     * @param request  (required)
     * @return Trainer profile information (status code 200)
     *         or Bad Request (status code 400)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "createTrainer",
            summary = "Register a new trainer",
            tags = { "Trainers" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile information", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerProfileResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PostMapping("/register")
    public LoginCredentials createTrainer(@RequestBody TrainerProfile request) {
        return serviceFacade.createTrainer(request);
    }

    /**
     * GET /trainer/{username} : Get trainer profile
     *
     * @param username  (required)
     * @return Trainer profile information (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainer not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "getTrainerProfile",
            summary = "Get trainer profile",
            tags = { "Trainers" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile information", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerProfileResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
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
    @GetMapping("/{username}")
    public TrainerProfileResponse getTrainerProfile(@PathVariable String username) {
        return serviceFacade.findTrainer(username);
    }

    /**
     * PUT /trainer/{username} : Update trainer profile
     *
     * @param username  (required)
     * @param request  (required)
     * @return Trainer profile successfully updated (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainer not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "updateTrainerProfile",
            summary = "Update trainer profile",
            tags = { "Trainers" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile successfully updated", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateTrainerProfile200Response.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
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
    @PutMapping("/{username}")
    public UpdateTrainerProfile200Response updateTrainerProfile(
            @PathVariable String username,
            @RequestBody TrainerUpdateProfileRequest request) {
        return serviceFacade.updateTrainerProfile(username, request);
    }

    /**
     * GET /trainer/{username}/trainings : Get trainer&#39;s trainings
     *
     * @param username  (required)
     * @param periodFrom  (optional)
     * @param periodTo  (optional)
     * @param traineeName  (optional)
     * @return List of trainer&#39;s trainings (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainer not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "findTrainersTrainings",
            summary = "Get trainer's trainings",
            tags = { "Trainers", "Trainings" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of trainer's trainings", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrainerTrainingsListItem.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
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
    @GetMapping("/{username}/trainings")
    public List<TrainerTrainingsListItem> findTrainersTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String traineeName) {
        TrainerTrainingsListRequest trainingsListRequest =
                new TrainerTrainingsListRequest(username, periodFrom, periodTo, traineeName);

        return serviceFacade.getTrainerTrainingsList(trainingsListRequest);
    }

    /**
     * PATCH /trainer/{username}/activate : Activate or Deactivate a Trainer
     *
     * @param username  (required)
     * @param isActive  (required)
     * @return Successfully activated or deactivated the trainer. (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainer not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "activateDeactivateTrainer",
            summary = "Activate or Deactivate a Trainer",
            tags = { "Trainers" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully activated or deactivated the trainer.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
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
    @PatchMapping("/{username}/activate")
    public void activateDeactivateTrainer(@PathVariable String username,
                                          @RequestBody ActivateDeactivateRequest isActive) {
        serviceFacade.activateUser(username, isActive);
    }

}
