package ru.scoring.camunda_app.process.main.variable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScoringResult {
    SCORING_SUCCESS("Компания успешно прошла скоринг-оценку"),
    CAPITAL_FAILURE("Капитал компании меньше 5000000"),
    INDIVIDUAL_UNDERTAKER_FAILURE("Компания является ИП"),
    PROHIBITED_REGION_FAILURE("Компания находится в запрещенном регионе (Красноярский край)"),
    RESIDENTIAL_FAILURE("Компания не является резидентом РФ");

    private final String description;
}
