package ru.scoring.context_api.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "context")
public class Context {
    @Id
    private String processId;
    private String individualTaxpayerNumber;
    private Integer regionalCode;
    private Long capital;
    private Boolean scoring;
    private List<String> description;
}
