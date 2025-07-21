package com.gca.controller;

import com.gca.facade.TrainingAppFacade;
import com.gca.openapi.model.TrainerCreateRequest;
import com.gca.openapi.model.TrainerCreateResponse;
import com.gca.openapi.model.TrainerGetResponse;
import com.gca.openapi.model.TrainerUpdateRequest;
import com.gca.openapi.model.TrainerUpdateResponse;
import com.gca.utils.GymTestProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.gca.controller.ApiConstant.BASE_PATH;
import static com.gca.utils.JsonUtils.asJsonString;
import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    private final String trainerApi = format("%s/trainers", BASE_PATH);
    private final String trainerRegisterApi = format("%s/register", trainerApi);
    private final String username = "ricardo.milos";

    @Mock
    private TrainingAppFacade facade;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TrainerController controller = new TrainerController(facade);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void registerTrainer_returnsOkResponse() throws Exception {
        TrainerCreateRequest request = GymTestProvider.createTrainerCreateRequest();
        TrainerCreateResponse response = GymTestProvider.createTrainerCreateResponse();

        when(facade.createTrainer(any())).thenReturn(response);

        mockMvc.perform(post(trainerRegisterApi)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(response.getUsername()))
                .andExpect(jsonPath("$.password").value(response.getPassword()));

        verify(facade).createTrainer(any());
    }

    @Test
    void getTrainerByUsername_returnsTrainer() throws Exception {
        TrainerGetResponse response = GymTestProvider.createTrainerGetResponse();

        when(facade.getTrainerByUsername(username)).thenReturn(response);

        mockMvc.perform(get(format("%s/%s", trainerApi, username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(response.getLastName()))
                .andExpect(jsonPath("$.specialization").value(response.getSpecialization()))
                .andExpect(jsonPath("$.isActive").value(response.getIsActive()));

        verify(facade).getTrainerByUsername(username);
    }

    @Test
    void updateTrainer_returnsUpdatedTrainer() throws Exception {
        TrainerUpdateRequest request = GymTestProvider.createTrainerUpdateRequest();
        TrainerUpdateResponse response = GymTestProvider.createTrainerUpdateResponse();

        when(facade.updateTrainer(eq(username), any())).thenReturn(response);

        mockMvc.perform(put(format("%s/%s", trainerApi, username))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(response.getUsername()))
                .andExpect(jsonPath("$.firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(response.getLastName()))
                .andExpect(jsonPath("$.specialization").value(response.getSpecialization()))
                .andExpect(jsonPath("$.isActive").value(response.getIsActive()))
                .andExpect(jsonPath("$.trainees.length()").value(response.getTrainees().size()));

        verify(facade).updateTrainer(eq(username), any());
    }
}