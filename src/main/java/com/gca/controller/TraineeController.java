package com.gca.controller;

import com.gca.facade.TrainingAppFacade;
import com.gca.openapi.model.AssignedTrainerResponse;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateRequest;
import com.gca.openapi.model.TraineeAssignedTrainersUpdateResponse;
import com.gca.openapi.model.TraineeCreateRequest;
import com.gca.openapi.model.TraineeCreateResponse;
import com.gca.openapi.model.TraineeGetResponse;
import com.gca.openapi.model.TraineeUpdateRequest;
import com.gca.openapi.model.TraineeUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gca.controller.ApiConstant.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH + "/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TrainingAppFacade trainingAppFacade;

    @PostMapping("/register")
    public ResponseEntity<TraineeCreateResponse> registerTrainee(@RequestBody TraineeCreateRequest trainee) {
        TraineeCreateResponse response = trainingAppFacade.createTrainee(trainee);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeGetResponse> getTraineeByUsername(@PathVariable(name = "username") String username) {
        TraineeGetResponse response = trainingAppFacade.getTraineeByUsername(username);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeUpdateResponse> updateTrainee(@PathVariable(name = "username") String username,
                                                               @RequestBody TraineeUpdateRequest trainee) {
        TraineeUpdateResponse response = trainingAppFacade.updateTrainee(username, trainee);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable(name = "username") String username) {
        trainingAppFacade.deleteTraineeByUsername(username);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<TraineeAssignedTrainersUpdateResponse> updateTraineeAssignedTrainers(
            @PathVariable(name = "username") String username, @RequestBody TraineeAssignedTrainersUpdateRequest request
    ) {
        TraineeAssignedTrainersUpdateResponse response
                = trainingAppFacade.updateTraineeTrainers(username, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}/available-trainers")
    public ResponseEntity<List<AssignedTrainerResponse>> getAvailableTrainers(@PathVariable(name = "username") String username) {
        List<AssignedTrainerResponse> response = trainingAppFacade.getUnassignedTrainers(username);

        return ResponseEntity.ok(response);
    }
}
