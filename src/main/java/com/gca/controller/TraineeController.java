package com.gca.controller;

import com.gca.facade.TrainingAppFacade;
import com.gca.openapi.model.TraineeCreateRequest;
import com.gca.openapi.model.TraineeCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gca.controller.ApiConstant.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH + "/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TrainingAppFacade trainingAppFacade;

    @PostMapping("/register")
    public ResponseEntity<TraineeCreateResponse> registerTrainee(TraineeCreateRequest trainee) {
        TraineeCreateResponse response = trainingAppFacade.createTrainee(trainee);

        return ResponseEntity.ok(response);
    }
}
