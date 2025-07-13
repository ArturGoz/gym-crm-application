package com.gca.service;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;

public interface TraineeService {
    TraineeResponse createTrainee(TraineeCreateRequest request);

    TraineeResponse updateTrainee(TraineeUpdateRequest request);

    TraineeResponse getTraineeByUsername(String username);

    TraineeResponse updateTraineeTrainers(UpdateTraineeTrainersRequest request);

    void deleteTraineeByUsername(String username);
}
