package com.example.demo.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Description;

import javax.validation.constraints.NotNull;

public class GeoDto {


    @Data
    @NoArgsConstructor
    public static class ReverseGeoDto {

        @NotNull
        private long lt;

        @NotNull
        private long ld;

    }

}
