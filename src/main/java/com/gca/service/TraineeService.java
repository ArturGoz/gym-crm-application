package com.gca.service;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;

public interface TraineeService {
    TraineeDTO createTrainee(TraineeCreateRequest request);

    TraineeDTO updateTrainee(TraineeUpdateRequest request);

    TraineeDTO getTraineeByUsername(String username);

    TraineeDTO updateTraineeTrainers(UpdateTraineeTrainersRequest request);

    void deleteTraineeByUsername(String username);
}
