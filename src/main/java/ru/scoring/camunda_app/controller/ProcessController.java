package ru.scoring.camunda_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.NativeWebRequest;
import ru.scoring.api_specifications.process_controller._1_0_0.server.api.ProcessV10ApiController;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartResponse;
import ru.scoring.camunda_app.service.ScoringService;


@Controller
public class ProcessController extends ProcessV10ApiController {
    private final ScoringService scoringService;

    public ProcessController(NativeWebRequest request,
                             ScoringService scoringService) {
        super(request);
        this.scoringService = scoringService;
    }


    public ResponseEntity<ScoringStartResponse> scoringStart(@RequestBody ScoringStartRequest scoringStartRequest) {
        return ResponseEntity.ok(scoringService.startScoring(scoringStartRequest));
    }
}
