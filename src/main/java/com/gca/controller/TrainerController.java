package com.gca.controller;

import com.gca.facade.TrainingAppFacade;
import com.gca.openapi.model.TrainerCreateRequest;
import com.gca.openapi.model.TrainerCreateResponse;
import com.gca.openapi.model.TrainerGetResponse;
import com.gca.openapi.model.TrainerUpdateRequest;
import com.gca.openapi.model.TrainerUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gca.controller.ApiConstant.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH + "/trainers")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainingAppFacade trainingAppFacade;

    @PostMapping("/register")
    public ResponseEntity<TrainerCreateResponse> registerTrainer(@RequestBody TrainerCreateRequest trainer) {
        TrainerCreateResponse response = trainingAppFacade.createTrainer(trainer);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerGetResponse> getTrainerByUsername(@PathVariable(name = "username") String username) {
        TrainerGetResponse response = trainingAppFacade.getTrainerByUsername(username);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerUpdateResponse> updateTrainer(@PathVariable(name = "username") String username,
                                                               @RequestBody TrainerUpdateRequest trainer) {
        TrainerUpdateResponse response = trainingAppFacade.updateTrainer(username, trainer);

        return ResponseEntity.ok(response);
    }
}
