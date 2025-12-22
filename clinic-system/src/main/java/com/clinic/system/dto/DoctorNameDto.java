package com.clinic.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorNameDto {
    private String name;
    private Long id;
    private Double consultationFee;
}
