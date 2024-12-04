package com.gym.crm.application.controller;

import com.gym.crm.application.rest.dto.Error;
import com.gym.crm.application.rest.dto.TrainingTypeListResponseInner;
import com.gym.crm.application.service.facade.ServiceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training-types")
@RequiredArgsConstructor
public class TrainingTypeController {
    private final ServiceFacade serviceFacade;

    /**
     * GET /training-types : Get all available training types
     *
     * @return A list of available training types. (status code 200)
     *         or Bad Request (status code 400)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
            operationId = "getTrainingTypes",
            summary = "Get all available training types",
            tags = { "Trainings" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "A list of available training types.", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrainingTypeListResponseInner.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @GetMapping
    public List<TrainingTypeListResponseInner> getTrainingTypes() {
        return serviceFacade.getTrainingTypes();
    }
}
