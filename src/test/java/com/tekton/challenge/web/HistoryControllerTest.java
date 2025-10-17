package com.tekton.challenge.web;

import com.tekton.challenge.domain.History;
import com.tekton.challenge.service.HistoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HistoryController.class)
class HistoryControllerTest {

  @Autowired MockMvc mvc;
  @MockBean HistoryService historyService;

  @Test
  void testGetHistoryOk() throws Exception {
    History h = new History()
        .setId(1L)
        .setEndpoint("/api/calculate")
        .setParams("{\"num1\":10,\"num2\":20}")
        .setResponse("{\"sum\":30}")
        .setCreatedAt(Instant.parse("2025-10-15T03:01:02Z"));
    Mockito.when(historyService.getAll()).thenReturn(List.of(h));

    mvc.perform(get("/api/history"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].endpoint").value("/api/calculate"));
  }
}
