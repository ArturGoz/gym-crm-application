package com.gca.service;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.dto.trainee.TraineeUpdateResponse;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;
import com.gca.dto.user.UserCreationResponse;

public interface TraineeService {
    UserCreationResponse createTrainee(TraineeCreateRequest request);

    TraineeUpdateResponse updateTrainee(TraineeUpdateData request);

    TraineeResponse getTraineeByUsername(String username);

    TraineeResponse updateTraineeTrainers(UpdateTraineeTrainersRequest request);

    void deleteTraineeByUsername(String username);
}
