package com.tekton.challenge.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekton.challenge.service.CalculateService;
import com.tekton.challenge.web.dto.CalculateResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CalculateController.class)
class CalculateControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;

  @MockBean
  CalculateService calculateService;

  @Test
  void testCalculateOk() throws Exception {
    CalculateResponse resp = new CalculateResponse(
        new BigDecimal("30"), new BigDecimal("15"), new BigDecimal("34.50"));
    Mockito.when(calculateService.calculate(any())).thenReturn(resp);

    mvc.perform(post("/api/calculate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(Map.of("num1", 10, "num2", 20))))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.sum").value(30))
      .andExpect(jsonPath("$.percentage").value(15))
      .andExpect(jsonPath("$.total").value(34.50));
  }

  @Test
  void testCalculateBadRequestEmptyBody() throws Exception {
    mvc.perform(post("/api/calculate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
      .andExpect(status().isBadRequest());
  }

  @Test
  void testCalculateBadRequestMissingNum2() throws Exception {
    mvc.perform(post("/api/calculate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(Map.of("num1", 10))))
      .andExpect(status().isBadRequest());
  }
}
