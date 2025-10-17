package com.tekton.challenge.web;

import com.tekton.challenge.domain.History;
import com.tekton.challenge.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@Tag(name = "History", description = "Service call history")
public class HistoryController {

  private final HistoryService historyService;

  public HistoryController(HistoryService historyService) {
    this.historyService = historyService;
  }

  @Operation(
      summary = "Get call history",
      description = "Returns the list of history events",
      responses = {
              @ApiResponse(
                      responseCode = "200",
                      description = "List of history",
                      content = @Content(
                              mediaType = "application/json",
                              array = @ArraySchema(schema = @Schema(implementation = History.class)),
                              examples = @ExampleObject(
                                      value = """
                                    [
                                      {
                                        "id": 1,
                                        "endpoint": "/api/calc",
                                        "params": "{\"num1\":10,\"num2\":20}",
                                        "response": "{\"sum\":30,\"percentage\":15,\"total\":34.5}",
                                        "createdAt": "2025-10-15T03:01:02Z"
                                      }
                                    ]
                                    """
                              )
                      )
              ),
              @ApiResponse(responseCode = "500", description = "Internal Error")
      }
  )
  @GetMapping
  public List<History> getHistory() {
    return historyService.getAll();
  }
}
