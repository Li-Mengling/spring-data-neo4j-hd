package com.lml.dto;

import com.lml.utils.CSVField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyToInsRelationDTO {

    @CSVField(name = "startId")
    private String startId;

    @CSVField(name = "endId")
    private String endId;
}
