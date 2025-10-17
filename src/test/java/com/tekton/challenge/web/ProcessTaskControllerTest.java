package com.tekton.challenge.web;

import com.tekton.challenge.service.processtask.ProcessTaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcessTaskController.class)
class ProcessTaskControllerTest {

  @Autowired MockMvc mvc;
  @MockBean ProcessTaskService processTaskService;

  @Test
  void testProcessOk() throws Exception {
    mvc.perform(post("/api/task/process"))
      .andExpect(status().isOk());
    Mockito.verify(processTaskService).process();
  }
}
