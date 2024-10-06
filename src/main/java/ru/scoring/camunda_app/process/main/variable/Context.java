package ru.scoring.camunda_app.process.main.variable;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@Builder(toBuilder = true)
public class Context {
    /**
     * ИНН
     */
    String individualTaxpayerNumber;

    /**
     * Код региона
     */
    Integer regionalCode;

    /**
     * Капитал компании
     */
    Long capital;

    /**
     * Текстовое описание результата скоринг-оценки
     */
    @Builder.Default
    List<ScoringResult> scoringResult = new ArrayList<>();
}
