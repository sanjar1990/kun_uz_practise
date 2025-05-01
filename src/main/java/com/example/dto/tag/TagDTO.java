package com.example.dto.tag;

import com.example.dto.BaseStringDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO extends BaseStringDTO {
    private String tagName;
}
