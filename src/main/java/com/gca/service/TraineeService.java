package com.gca.service;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainer.TrainerResponse;

public interface TraineeService {
    TraineeResponse createTrainee(TraineeCreateRequest request);

    TraineeResponse updateTrainee(TraineeUpdateRequest request);

    TraineeResponse getTraineeById(Long id);

    TraineeResponse getTraineeByUsername(String username);

    void deleteTrainee(Long id);
}
