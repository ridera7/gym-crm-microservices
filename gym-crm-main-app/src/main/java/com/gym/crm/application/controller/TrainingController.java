package com.gym.crm.application.controller;

import com.gym.crm.application.rest.dto.AddTrainingRequest;
import com.gym.crm.application.rest.dto.Error;
import com.gym.crm.application.service.facade.ServiceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
public class TrainingController {
    private final ServiceFacade serviceFacade;

    /**
     * POST /training/add : Add a new training
     *
     * @param addTrainingRequest  (required)
     * @return Training successfully added (status code 200)
     *         or Bad Request (status code 400)
     *         or Forbidden operation (status code 403)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "createTraining",
            summary = "Add a new training",
            tags = { "Trainings" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training successfully added"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden operation", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addTraining(@RequestBody AddTrainingRequest addTrainingRequest) {
        serviceFacade.createTraining(addTrainingRequest);
    }
}
