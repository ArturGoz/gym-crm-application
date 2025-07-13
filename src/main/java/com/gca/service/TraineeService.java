package com.gca.service;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;

public interface TraineeService {
    TraineeResponse createTrainee(TraineeCreateRequest request);

    TraineeResponse updateTrainee(TraineeUpdateRequest request);

    TraineeResponse getTraineeByUsername(String username);

    void deleteTraineeByUsername(String username);
}
