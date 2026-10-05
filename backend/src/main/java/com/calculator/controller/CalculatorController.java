package com.calculator.controller;

import com.calculator.model.Calculation;
import com.calculator.repository.CalculationRepository;
import com.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CalculatorController {

    private final CalculatorService calculatorService;
    private final CalculationRepository calculationRepository;

    public CalculatorController(CalculatorService calculatorService,
                                CalculationRepository calculationRepository) {
        this.calculatorService = calculatorService;
        this.calculationRepository = calculationRepository;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok", "backend", "Spring Boot");
    }

    @PostMapping("/calculate")
    public Map<String, Object> calculate(@Valid @RequestBody CalculationRequest request) {
        double result = calculatorService.calculate(request.expression());
        return Map.of("expression", request.expression().trim(), "result", result);
    }

    @PostMapping("/calculations")
    @ResponseStatus(HttpStatus.CREATED)
    public Calculation save(@Valid @RequestBody CalculationRequest request) {
        double result = calculatorService.calculate(request.expression());
        return calculationRepository.save(new Calculation(request.expression().trim(), result));
    }

    @GetMapping("/calculations")
    public List<Calculation> history() {
        return calculationRepository.findTop50ByOrderByCreatedAtDescIdDesc();
    }

    @DeleteMapping("/calculations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearHistory() {
        calculationRepository.deleteAllInBatch();
    }

    public record CalculationRequest(
            @NotBlank(message = "Expression is required")
            @Size(max = 255, message = "Expression must be 255 characters or less")
            String expression
    ) {}
}
