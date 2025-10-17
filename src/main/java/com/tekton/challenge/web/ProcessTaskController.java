package com.tekton.challenge.web;

import com.tekton.challenge.service.processtask.ProcessTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
@Tag(
        name = "FailedTaskProcessing",
        description = "Operations to reprocess failed asynchronous tasks"
)
public class ProcessTaskController {

  private final ProcessTaskService processTaskService;

  public ProcessTaskController(ProcessTaskService processTaskService) {
    this.processTaskService = processTaskService;
  }

  @Operation(
          summary = "Reprocess failed asynchronous tasks",
          description = "Triggers the reprocessing of tasks that previously failed during asynchronous execution. "
                  + "No request body is required. Returns 200 OK with no content on successful trigger.",
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Reprocessing triggered (no content)",
                          content = @Content
                  ),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Invalid state to reprocess or preconditions not met",
                          content = @Content
                  ),
                  @ApiResponse(
                          responseCode = "500",
                          description = "Unexpected server error",
                          content = @Content
                  )
          }
  )
  @PostMapping("/process")
  public ResponseEntity<Void> process() {
    processTaskService.process();
    return ResponseEntity.ok().build();
  }
}
