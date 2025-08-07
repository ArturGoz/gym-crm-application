package com.gca.facade;

import com.gca.dto.PasswordChangeDTO;
import com.gca.dto.auth.AuthenticationRequestDTO;
import com.gca.dto.filter.TrainingTraineeCriteriaFilter;
import com.gca.dto.filter.TrainingTrainerCriteriaFilter;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeGetDTO;
import com.gca.dto.trainee.TraineeTrainersUpdateDTO;
import com.gca.dto.trainee.TraineeUpdateRequestDTO;
import com.gca.dto.trainee.TraineeUpdateResponseDTO;
import com.gca.dto.trainer.AssignedTrainerDTO;
import com.gca.dto.trainer.TrainerCreateDTO;
import com.gca.dto.trainer.TrainerGetDTO;
import com.gca.dto.trainer.TrainerUpdateRequestDTO;
import com.gca.dto.trainer.TrainerUpdateResponseDTO;
import com.gca.dto.training.TrainingCreateDTO;
import com.gca.dto.training.TrainingDTO;
import com.gca.dto.user.UserCredentialsDTO;
import com.gca.mapper.rest.RestTraineeMapper;
import com.gca.mapper.rest.RestTrainerMapper;
import com.gca.mapper.rest.RestTrainingMapper;
import com.gca.model.TrainingType;
import com.gca.openapi.model.AssignedTrainerResponse;
import com.gca.openapi.model.LoginChangeRequest;
import com.gca.openapi.model.LoginRequest;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateRequest;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateResponse;
import com.gca.openapi.model.TraineeCreateRequest;
import com.gca.openapi.model.TraineeCreateResponse;
import com.gca.openapi.model.TraineeGetResponse;
import com.gca.openapi.model.TraineeUpdateRequest;
import com.gca.openapi.model.TraineeUpdateResponse;
import com.gca.openapi.model.TrainerCreateRequest;
import com.gca.openapi.model.TrainerCreateResponse;
import com.gca.openapi.model.TrainerGetResponse;
import com.gca.openapi.model.TrainerUpdateRequest;
import com.gca.openapi.model.TrainerUpdateResponse;
import com.gca.openapi.model.TrainingCreateRequest;
import com.gca.openapi.model.TrainingGetResponse;
import com.gca.openapi.model.TrainingTypeResponse;
import com.gca.security.AuthenticationService;
import com.gca.security.jwt.JwtTokenProvider;
import com.gca.service.TraineeService;
import com.gca.service.TrainerService;
import com.gca.service.TrainingService;
import com.gca.service.UserService;
import com.gca.utils.GymTestProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingAppFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private RestTraineeMapper restTraineeMapper;

    @Mock
    private RestTrainerMapper restTrainerMapper;

    @Mock
    private RestTrainingMapper restTrainingMapper;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TrainingAppFacade facade;

    private final Long jwtDuration = 3600000L;

    @BeforeEach
    void setUp() {
        try {
            Field jwtDurationField = TrainingAppFacade.class.getDeclaredField("jwtDuration");
            jwtDurationField.setAccessible(true);
            jwtDurationField.set(facade, jwtDuration);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set jwtDuration", e);
        }
    }

    @Test
    void createTrainee_delegatesToService() {
        TraineeCreateDTO internalDto = GymTestProvider.createTraineeCreateDTO();
        UserCredentialsDTO userCredentialsDto = GymTestProvider.constructUserCreateDTO();
        TraineeCreateRequest restRequest = GymTestProvider.createTraineeCreateRequest();
        TraineeCreateResponse expectedResponse = GymTestProvider.createTraineeCreateResponse();

        when(restTraineeMapper.toDto(restRequest)).thenReturn(internalDto);
        when(traineeService.createTrainee(internalDto)).thenReturn(userCredentialsDto);
        when(restTraineeMapper.toRest(userCredentialsDto)).thenReturn(expectedResponse);

        TraineeCreateResponse actualResponse = facade.createTrainee(restRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(restTraineeMapper).toDto(restRequest);
        verify(traineeService).createTrainee(internalDto);
        verify(restTraineeMapper).toRest(userCredentialsDto);
    }

    @Test
    void updateTrainee_delegatesToService() {
        String username = "alexander.usyk";
        TraineeUpdateRequest restRequest = GymTestProvider.createTraineeUpdateRequest();
        TraineeUpdateRequestDTO dto = GymTestProvider.createTraineeUpdateRequestDTO();
        TraineeUpdateResponseDTO responseDTO = GymTestProvider.createTraineeUpdateResponse();
        TraineeUpdateResponse expected = GymTestProvider.createTraineeUpdateRestResponse();

        when(restTraineeMapper.toDto(username, restRequest)).thenReturn(dto);
        when(traineeService.updateTrainee(dto)).thenReturn(responseDTO);
        when(restTraineeMapper.toRest(responseDTO)).thenReturn(expected);

        TraineeUpdateResponse actual = facade.updateTrainee(username, restRequest);

        assertEquals(expected, actual);
        verify(restTraineeMapper).toDto(username, restRequest);
        verify(traineeService).updateTrainee(dto);
        verify(restTraineeMapper).toRest(responseDTO);
    }

    @Test
    void createTrainer_delegatesToService() {
        TrainerCreateRequest request = GymTestProvider.createTrainerCreateRequest();
        TrainerCreateDTO createDTO = GymTestProvider.createTrainerCreateDTO();
        UserCredentialsDTO credentialsDTO = GymTestProvider.constructUserCreateDTO();
        TrainerCreateResponse expectedResponse = GymTestProvider.createTrainerCreateResponse();

        when(restTrainerMapper.toDto(request)).thenReturn(createDTO);
        when(trainerService.createTrainer(createDTO)).thenReturn(credentialsDTO);
        when(restTrainerMapper.toRest(credentialsDTO)).thenReturn(expectedResponse);

        TrainerCreateResponse actual = facade.createTrainer(request);

        assertEquals(expectedResponse, actual);
        verify(restTrainerMapper).toDto(request);
        verify(trainerService).createTrainer(createDTO);
        verify(restTrainerMapper).toRest(credentialsDTO);
    }

    @Test
    void updateTrainer_delegatesToService() {
        String username = "arnold.schwarzenegger";
        TrainerUpdateRequest request = GymTestProvider.createTrainerUpdateRequest();
        TrainerUpdateRequestDTO requestDTO = GymTestProvider.createTrainerUpdateRequestDTO();
        TrainerUpdateResponseDTO responseDTO = GymTestProvider.createTrainerUpdateResponseDTO();
        TrainerUpdateResponse expected = GymTestProvider.createTrainerUpdateResponse();

        when(restTrainerMapper.toDto(username, request)).thenReturn(requestDTO);
        when(trainerService.updateTrainer(requestDTO)).thenReturn(responseDTO);
        when(restTrainerMapper.toRest(responseDTO)).thenReturn(expected);

        TrainerUpdateResponse actual = facade.updateTrainer(username, request);

        assertEquals(expected, actual);
        verify(restTrainerMapper).toDto(username, request);
        verify(trainerService).updateTrainer(requestDTO);
        verify(restTrainerMapper).toRest(responseDTO);
    }

    @Test
    void createTraining_delegatesToService() {
        TrainingCreateRequest restRequest = GymTestProvider.createTrainingCreateRequest();
        TrainingCreateDTO dto = GymTestProvider.createTrainingCreateRequestDTO();

        when(restTrainingMapper.toDto(restRequest)).thenReturn(dto);

        facade.createTraining(restRequest);

        verify(restTrainingMapper).toDto(restRequest);
        verify(trainingService).createTraining(dto);
    }

    @Test
    void changePassword_delegatesToUserService() {
        LoginChangeRequest request = new LoginChangeRequest("ronnie.coleman",
                "oldPassword", "newPassword");

        facade.changePassword(request);

        verify(userService).changeUserPassword(any(PasswordChangeDTO.class));
    }

    @Test
    void findFilteredTrainings_byTrainerCriteria_delegatesToService() {
        TrainingTrainerCriteriaFilter filter = GymTestProvider.buildTrainerCriteriaFilter();

        TrainingDTO trainingDTO = GymTestProvider.createTrainingDTO();
        TrainingGetResponse expectedResponse = GymTestProvider.createTrainingGetResponse();
        List<TrainingDTO> dtoList = List.of(trainingDTO);
        List<TrainingGetResponse> expected = List.of(expectedResponse);

        when(trainingService.getTrainerTrainings(filter)).thenReturn(dtoList);
        when(restTrainingMapper.toRest(trainingDTO)).thenReturn(expectedResponse);

        List<TrainingGetResponse> actual = facade.findFilteredTrainings(filter);

        assertEquals(expected, actual);
        verify(trainingService).getTrainerTrainings(filter);
        verify(restTrainingMapper).toRest(trainingDTO);
    }

    @Test
    void findFilteredTrainings_byTraineeCriteria_delegatesToService() {
        TrainingTraineeCriteriaFilter filter = GymTestProvider.buildTraineeCriteriaFilter();

        TrainingDTO trainingDTO = GymTestProvider.createTrainingDTO();
        TrainingGetResponse expectedResponse = GymTestProvider.createTrainingGetResponse();
        List<TrainingDTO> dtoList = List.of(trainingDTO);
        List<TrainingGetResponse> expected = List.of(expectedResponse);

        when(trainingService.getTraineeTrainings(filter)).thenReturn(dtoList);
        when(restTrainingMapper.toRest(trainingDTO)).thenReturn(expectedResponse);

        List<TrainingGetResponse> actual = facade.findFilteredTrainings(filter);

        assertEquals(expected, actual);
        verify(trainingService).getTraineeTrainings(filter);
        verify(restTrainingMapper).toRest(trainingDTO);
    }

    @Test
    void getTraineeByUsername_delegatesToService() {
        String username = "arnold.schwarzenegger";
        TraineeGetDTO dto = GymTestProvider.createTraineeGetDTO();
        TraineeGetResponse expectedResponse = GymTestProvider.createTraineeGetResponse();

        when(traineeService.getTraineeByUsername(username)).thenReturn(dto);
        when(restTraineeMapper.toRest(dto)).thenReturn(expectedResponse);

        TraineeGetResponse actualResponse = facade.getTraineeByUsername(username);

        assertEquals(expectedResponse, actualResponse);
        verify(traineeService).getTraineeByUsername(username);
        verify(restTraineeMapper).toRest(dto);
    }

    @Test
    void getTrainerByUsername_delegatesToService() {
        String username = "ronnie.coleman";
        TrainerGetDTO dto = GymTestProvider.createTrainerGetDTO();
        TrainerGetResponse expected = GymTestProvider.createTrainerGetResponse();

        when(trainerService.getTrainerByUsername(username)).thenReturn(dto);
        when(restTrainerMapper.toRest(dto)).thenReturn(expected);

        TrainerGetResponse actual = facade.getTrainerByUsername(username);

        assertEquals(expected, actual);
        verify(trainerService).getTrainerByUsername(username);
        verify(restTrainerMapper).toRest(dto);
    }

    @Test
    void deleteTraineeByUsername_delegatesToService() {
        String username = "ronnie.coleman";

        facade.deleteTraineeByUsername(username);

        verify(traineeService).deleteTraineeByUsername(username);
    }

    @Test
    void toggleUserActiveStatus_delegatesToService() {
        String username = "ronnie.coleman";
        boolean isActive = false;

        facade.toggleUserActiveStatus(username, isActive);

        verify(userService).toggleActiveStatus(username, isActive);
    }

    @Test
    void getUnassignedTrainers_delegatesToService() {
        String traineeUsername = "arnold.schwarzenegger";
        AssignedTrainerDTO dto1 = GymTestProvider.createAssignedTrainerDTO("trainer1");
        AssignedTrainerDTO dto2 = GymTestProvider.createAssignedTrainerDTO("trainer2");

        AssignedTrainerResponse rest1 = GymTestProvider.createTrainerResponse("trainer1");
        AssignedTrainerResponse rest2 = GymTestProvider.createTrainerResponse("trainer2");
        List<AssignedTrainerDTO> dtoList = List.of(dto1, dto2);
        List<AssignedTrainerResponse> expected = List.of(rest1, rest2);

        when(trainerService.getUnassignedTrainers(traineeUsername)).thenReturn(dtoList);
        when(restTrainerMapper.toRest(dto1)).thenReturn(rest1);
        when(restTrainerMapper.toRest(dto2)).thenReturn(rest2);

        List<AssignedTrainerResponse> actual = facade.getUnassignedTrainers(traineeUsername);

        assertEquals(expected, actual);
        verify(trainerService).getUnassignedTrainers(traineeUsername);
        verify(restTrainerMapper).toRest(dto1);
        verify(restTrainerMapper).toRest(dto2);
    }

    @Test
    void updateTraineeTrainers_delegatesToService() {
        String username = "arnold.schwarzenegger";
        TraineeAssignedTrainersUpdateRequest request = GymTestProvider.createTraineeAssignedTrainersUpdateRequest();
        TraineeTrainersUpdateDTO updateDTO = new TraineeTrainersUpdateDTO(username, request.getTrainerUsernames());

        AssignedTrainerDTO trainer1 = GymTestProvider.createAssignedTrainerDTO("t1");
        AssignedTrainerDTO trainer2 = GymTestProvider.createAssignedTrainerDTO("t2");
        AssignedTrainerResponse rest1 = GymTestProvider.createAssignedTrainerResponse("t1");
        AssignedTrainerResponse rest2 = GymTestProvider.createAssignedTrainerResponse("t2");
        List<AssignedTrainerDTO> dtoList = List.of(trainer1, trainer2);
        List<AssignedTrainerResponse> expectedList = List.of(rest1, rest2);

        when(traineeService.updateTraineeTrainers(updateDTO)).thenReturn(dtoList);
        when(restTrainerMapper.toRest(trainer1)).thenReturn(rest1);
        when(restTrainerMapper.toRest(trainer2)).thenReturn(rest2);

        TraineeAssignedTrainersUpdateResponse actual = facade.updateTraineeTrainers(username, request);

        assertEquals(expectedList, actual.getTrainers());
        verify(traineeService).updateTraineeTrainers(updateDTO);
        verify(restTrainerMapper).toRest(trainer1);
        verify(restTrainerMapper).toRest(trainer2);
    }

    @Test
    void getAllTrainingTypes_delegatesToService() {
        TrainingType type1 = GymTestProvider.createTrainingTypeYoga();
        TrainingType type2 = GymTestProvider.createTrainingTypeStrength();

        TrainingTypeResponse response1 =
                GymTestProvider.createTrainingTypeResponse(1, "Yoga");
        TrainingTypeResponse response2 =
                GymTestProvider.createTrainingTypeResponse(2, "Strength");

        List<TrainingType> typeList = List.of(type1, type2);
        List<TrainingTypeResponse> expectedResponses = List.of(response1, response2);

        when(trainingService.getAllTrainingTypes()).thenReturn(typeList);
        when(restTrainingMapper.toRest(type1)).thenReturn(response1);
        when(restTrainingMapper.toRest(type2)).thenReturn(response2);

        List<TrainingTypeResponse> actual = facade.getAllTrainingTypes();

        assertEquals(expectedResponses, actual);
        verify(trainingService).getAllTrainingTypes();
        verify(restTrainingMapper).toRest(type1);
        verify(restTrainingMapper).toRest(type2);
    }

    @Test
    void login_delegatesToAuthService() {
        LoginRequest request = new LoginRequest("ronnie.coleman", "123qweQWE!@#");
        AuthenticationRequestDTO authRequest =
                new AuthenticationRequestDTO("ronnie.coleman", "123qweQWE!@#");
        String expectedHeaderValue = String.format(
                "JWT=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=Strict",
                "token",
                (int) (jwtDuration / 1000)
        );

        when(jwtTokenProvider.createToken(authRequest.getUsername())).thenReturn("token");

        facade.login(request, httpServletResponse);

        verify(authenticationService).authenticate(authRequest);
        verify(httpServletResponse).setHeader("Set-Cookie", expectedHeaderValue);
    }
}