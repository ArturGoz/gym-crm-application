package com.gca.controller;

import com.gca.utils.GymTestProvider;
import com.gca.facade.TrainingAppFacade;
import com.gca.openapi.model.TraineeCreateRequest;
import com.gca.openapi.model.TraineeCreateResponse;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    private final String traineeApi = format("%s/trainees", BASE_PATH);
    private final String traineeRegisterApi = format("%s/register", traineeApi);

    @Mock
    private TrainingAppFacade trainingAppFacade;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TraineeController controller = new TraineeController(trainingAppFacade);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void registerTrainee_returnsOkResponse() throws Exception {
        TraineeCreateRequest request = GymTestProvider.createTraineeCreateRequest();
        TraineeCreateResponse response = GymTestProvider.createTraineeCreateResponse();

        when(trainingAppFacade.createTrainee(any())).thenReturn(response);

        mockMvc.perform(post(traineeRegisterApi)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe"))
                .andExpect(jsonPath("$.password").value("password"));

        verify(trainingAppFacade).createTrainee(any());
    }
}