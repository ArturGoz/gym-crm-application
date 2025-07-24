package com.gca.controller;

import com.gca.facade.TrainingAppFacade;
import com.gca.openapi.model.LoginChangeRequest;
import com.gca.openapi.model.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gca.controller.ApiConstant.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH + "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TrainingAppFacade facade;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        facade.login(request, response);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/login")
    public ResponseEntity<Void> changeLogin(@RequestBody LoginChangeRequest request) {
        facade.changePassword(request);

        return ResponseEntity.ok().build();
    }
}
