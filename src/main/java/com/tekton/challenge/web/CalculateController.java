package com.tekton.challenge.web;

import com.tekton.challenge.web.dto.CalculateResponse;
import com.tekton.challenge.service.CalculateService;
import com.tekton.challenge.web.dto.CalculateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculate")
@Tag(name = "Calculate", description = "Dynamic percentage calculation operations")
public class CalculateController {

  private final CalculateService calculateService;

  public CalculateController(CalculateService calculateService) {
    this.calculateService = calculateService;
  }

  @Operation(
          summary = "Calculate the total with dynamic percentage",
          description = "Add `num1` and `num2` and apply the current percentage.",
          requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  required = true,
                  content = @Content(mediaType = "application/json",
                          examples = @ExampleObject(value = "{ \"num1\": 10, \"num2\": 20 }"))
          ),
          responses = {
                  @ApiResponse(responseCode = "200", description = "OK",
                          content = @Content(schema = @Schema(implementation = CalculateResponse.class),
                                  examples = @ExampleObject(value = "{ \"sum\":30, \"percentage\":15, \"total\":34.5 }"))),
                  @ApiResponse(responseCode = "400", description = "Invalid Params")
          }
  )
  @PostMapping
  public ResponseEntity<CalculateResponse> calculate(@RequestBody @Valid CalculateRequest body) {
    CalculateResponse resp = calculateService.calculate(body);
    return ResponseEntity.ok(resp);
  }
}
