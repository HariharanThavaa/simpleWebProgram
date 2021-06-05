package microservices.book.multiplication.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public class MultiplicationResultAttemptController {

    private final MultiplicationService multiplicationService;

    @Autowired
    MultiplicationResultAttemptController(MultiplicationService multiplicationService){
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    ResponseEntity<MultiplicationResultAttempt> postResult(
            @RequestBody MultiplicationResultAttempt multiplicationResultAttempt){
        boolean isCorrect = multiplicationService.checkAttempt(multiplicationResultAttempt);
        MultiplicationResultAttempt attemptCopy = new MultiplicationResultAttempt(multiplicationResultAttempt.getUser(),
                multiplicationResultAttempt.getMultiplication(),
                multiplicationResultAttempt.getResultAttempt(),
                isCorrect);

        return ResponseEntity.ok(attemptCopy);

    }

    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
    }
}
