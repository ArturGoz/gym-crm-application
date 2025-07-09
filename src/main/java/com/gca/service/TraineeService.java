package com.gca.service;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;

public interface TraineeService {
    TraineeResponse createTrainee(TraineeCreateRequest request);

    TraineeResponse updateTrainee(TraineeUpdateRequest request);

    TraineeResponse getTraineeById(Long id);

    TraineeResponse getTraineeByUsername(String username);

    void deleteTrainee(Long id);

    void deleteTraineeByUsername(String username);
}
