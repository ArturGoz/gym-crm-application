package com.gca.service;

import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.dto.trainee.TraineeUpdateDTO;
import com.gca.dto.trainee.UpdateTraineeTrainersRequest;
import com.gca.dto.user.UserCreateDTO;

public interface TraineeService {
    UserCreateDTO createTrainee(TraineeCreateDTO request);

    TraineeUpdateDTO updateTrainee(TraineeUpdateData request);

    TraineeDTO getTraineeByUsername(String username);

    TraineeDTO updateTraineeTrainers(UpdateTraineeTrainersRequest request);

    void deleteTraineeByUsername(String username);
}
