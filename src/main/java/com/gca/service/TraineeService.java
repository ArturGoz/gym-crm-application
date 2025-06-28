package com.gca.service;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;

import java.util.List;

public interface TraineeService {
    TraineeResponse createTrainee(TraineeCreateRequest request);
    TraineeResponse updateTrainee(TraineeUpdateRequest request);
    void deleteTrainee(Long id);
    TraineeResponse getTraineeById(Long id);
    TraineeResponse getTraineeByUsername(String username);
    List<TraineeResponse> getAllTrainees();
}
