package com.gym.crm.application.controller;

import com.gym.crm.application.dto.request.TraineeTrainingsListRequest;
import com.gym.crm.application.rest.dto.ActivateDeactivateRequest;
import com.gym.crm.application.rest.dto.Error;
import com.gym.crm.application.rest.dto.LoginCredentials;
import com.gym.crm.application.rest.dto.SuccessResponse;
import com.gym.crm.application.rest.dto.TraineeProfile;
import com.gym.crm.application.rest.dto.TraineeProfileResponse;
import com.gym.crm.application.rest.dto.TraineeTrainerListUpdateRequestInner;
import com.gym.crm.application.rest.dto.TraineeTrainingsListItem;
import com.gym.crm.application.rest.dto.TraineeUpdateProfileRequest;
import com.gym.crm.application.rest.dto.TrainerItem;
import com.gym.crm.application.rest.dto.UpdateTraineeProfile200Response;
import com.gym.crm.application.service.facade.ServiceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import java.util.Set;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {
    private final ServiceFacade serviceFacade;

    /**
     * POST /trainee/register : Register a new trainee
     *
     * @param request  (required)
     * @return Trainee successfully registered (status code 200)
     *         or Bad Request (status code 400)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "createTrainee",
            summary = "Register a new trainee",
            tags = { "Trainees" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee successfully registered", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LoginCredentials.class))
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
    public LoginCredentials createTrainee(@RequestBody TraineeProfile request) {
        return serviceFacade.createTrainee(request);
    }

    /**
     * GET /trainee/{username} : Get trainee profile
     *
     * @param username  (required)
     * @return Trainee profile information (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainee not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "getTraineeProfile",
            summary = "Get trainee profile",
            tags = { "Trainees" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile information", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TraineeProfileResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @GetMapping("/{username}")
    public TraineeProfileResponse getTraineeProfile(@PathVariable String username) {
        return serviceFacade.findTrainee(username);
    }

    /**
     * PUT /trainee/{username} : Update trainee profile
     *
     * @param username  (required)
     * @param request  (required)
     * @return Trainee profile successfully updated (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainee not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "updateTraineeProfile",
            summary = "Update trainee profile",
            tags = { "Trainees" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile successfully updated", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateTraineeProfile200Response.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PutMapping("/{username}")
    public UpdateTraineeProfile200Response updateTraineeProfile(
            @PathVariable String username,
            @RequestBody TraineeUpdateProfileRequest request) {
        return serviceFacade.updateTraineeProfile(username, request);
    }

    /**
     * DELETE /trainee/{username} : Delete trainee profile
     *
     * @param username  (required)
     * @return Trainee profile successfully deleted (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainee not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "deleteTrainee",
            summary = "Delete trainee profile",
            tags = { "Trainees" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @DeleteMapping("/{username}")
    public void deleteTrainee(@PathVariable String username) {
        serviceFacade.deleteTrainee(username);
    }

    /**
     * GET /trainee/{username}/active-trainers : Get active trainers not assigned to the trainee
     *
     * @param username  (required)
     * @return List of active trainers (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainee not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "getActiveTrainers",
            summary = "Get active trainers not assigned to the trainee",
            tags = { "Trainees" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of active trainers", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrainerItem.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @GetMapping("/{username}/active-trainers")
    public List<TrainerItem> getActiveTrainers(@PathVariable String username) {
        return serviceFacade.getTrainersListThatNotAssigned(username);
    }

    /**
     * PUT /trainee/{username}/trainers : Update trainee&#39;s trainer list
     *
     * @param username  (required)
     * @param trainerList  (required)
     * @return Trainee&#39;s trainer list successfully updated (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainee not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "updateTrainersList",
            summary = "Update trainee's trainer list",
            tags = { "Trainees" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee's trainer list successfully updated", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrainerItem.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PutMapping("/{username}/trainers")
    public List<TrainerItem> updateTrainersList(
            @PathVariable String username,
            @RequestBody Set<TraineeTrainerListUpdateRequestInner> trainerList) {
        return serviceFacade.updateTrainersList(username, trainerList);
    }

    /**
     * GET /trainee/{username}/trainings : Get trainee&#39;s trainings
     *
     * @param username  (required)
     * @param periodFrom  (optional)
     * @param periodTo  (optional)
     * @param trainerName  (optional)
     * @param trainingType Training type name parameter (optional)
     * @return List of trainee&#39;s trainings (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainee not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "findTraineesTrainings",
            summary = "Get trainee's trainings",
            tags = { "Trainees", "Trainings" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of trainee's trainings", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TraineeTrainingsListItem.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @GetMapping("/{username}/trainings")
    public List<TraineeTrainingsListItem> findTraineesTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType) {
        TraineeTrainingsListRequest trainingsListRequest =
                new TraineeTrainingsListRequest(username, periodFrom, periodTo, trainerName, trainingType);

        return serviceFacade.getTraineeTrainingsList(trainingsListRequest);
    }

    /**
     * PATCH /trainee/{username}/activate : Activate or Deactivate a Trainee
     *
     * @param username  (required)
     * @param isActive  (required)
     * @return Successfully activated or deactivated the trainee. (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Trainee not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "activateDeactivateTrainee",
            summary = "Activate or Deactivate a Trainee",
            tags = { "Trainees" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully activated or deactivated the trainee.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Trainee not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PatchMapping("/{username}/activate")
    public void activateDeactivateTrainee(@PathVariable String username,
                                          @RequestBody ActivateDeactivateRequest isActive) {
        serviceFacade.activateUser(username, isActive);
    }
}
