package com.gca.controller;

import com.gca.openapi.model.AssignedTrainerResponse;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateRequest;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateResponse;
import com.gca.openapi.model.TraineeGetResponse;
import com.gca.openapi.model.TraineeUpdateRequest;
import com.gca.openapi.model.TraineeUpdateResponse;
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

import java.util.List;

import static com.gca.controller.ApiConstant.BASE_PATH;
import static com.gca.utils.JsonUtils.asJsonString;
import static com.gca.utils.JsonUtils.readJson;
import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
                .andExpect(jsonPath("$.username").value(response.getUsername()))
                .andExpect(jsonPath("$.password").value(response.getPassword()));

        verify(trainingAppFacade).createTrainee(any());
    }

    @Test
    void getTraineeByUsername_returnsTrainee() throws Exception {
        String username = "john.doe";
        TraineeGetResponse response = GymTestProvider.createTraineeGetResponse();

        when(trainingAppFacade.getTraineeByUsername(username)).thenReturn(response);

        mockMvc.perform(get(format("%s/%s", traineeApi, username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(response.getLastName()))
                .andExpect(jsonPath("$.address").value(response.getAddress()))
                .andExpect(jsonPath("$.isActive").value(response.getIsActive()));

        verify(trainingAppFacade).getTraineeByUsername(username);
    }

    @Test
    void updateTrainee_returnsUpdatedTrainee() throws Exception {
        String username = "john.doe";
        TraineeUpdateRequest request = GymTestProvider.createTraineeUpdateRequest();
        TraineeUpdateResponse response = GymTestProvider.createTraineeUpdateRestResponse();

        when(trainingAppFacade.updateTrainee(eq(username), any())).thenReturn(response);

        mockMvc.perform(put(format("%s/%s", traineeApi, username))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(response.getUsername()))
                .andExpect(jsonPath("$.firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.isActive").value(response.getIsActive()));

        verify(trainingAppFacade).updateTrainee(eq(username), any());
    }

    @Test
    void deleteTrainee_returnsOk() throws Exception {
        String username = "john.doe";

        mockMvc.perform(delete(traineeApi + "/" + username))
                .andExpect(status().isOk());

        verify(trainingAppFacade).deleteTraineeByUsername(username);
    }

    @Test
    void updateTraineeAssignedTrainers_returnsUpdatedTrainers() throws Exception {
        String username = "john.doe";
        TraineeAssignedTrainersUpdateRequest request = GymTestProvider.createTraineeAssignedTrainersUpdateRequest();
        TraineeAssignedTrainersUpdateResponse response =
                readJson("trainee-assigned-trainers-update-response.json", TraineeAssignedTrainersUpdateResponse.class);

        when(trainingAppFacade.updateTraineeTrainers(eq(username), any())).thenReturn(response);

        mockMvc.perform(put(format("%s/%s/%s", traineeApi, username, "trainers"))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainers.length()").value(response.getTrainers().size()))
                .andExpect(jsonPath("$.trainers[0].username").value(response.getTrainers().get(0).getUsername()));

        verify(trainingAppFacade).updateTraineeTrainers(eq(username), any());
    }

    @Test
    void getAvailableTrainers_returnsTrainerList() throws Exception {
        String username = "john.doe";
        List<AssignedTrainerResponse> trainers = List.of(GymTestProvider.createAssignedTrainerResponse());

        when(trainingAppFacade.getUnassignedTrainers(username)).thenReturn(trainers);

        mockMvc.perform(get(format("%s/%s/%s", traineeApi, username, "available-trainers")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(trainers.size()))
                .andExpect(jsonPath("$[0].username").value(trainers.get(0).getUsername()));

        verify(trainingAppFacade).getUnassignedTrainers(username);
    }
}