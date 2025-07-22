package com.gca.controller;

import com.gca.facade.TrainingAppFacade;
import com.gca.openapi.model.TrainingCreateRequest;
import com.gca.openapi.model.TrainingTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gca.controller.ApiConstant.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH +"/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingAppFacade facade;

    @PostMapping
    public ResponseEntity<Void> createTraining(@RequestBody TrainingCreateRequest request) {
        facade.createTraining(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/types")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> responses = facade.getAllTrainingTypes();

        return ResponseEntity.ok().body(responses);
    }
}
