package com.souvik.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuery {

    private String firstName;
    private String lastName;
    private String email;
}
