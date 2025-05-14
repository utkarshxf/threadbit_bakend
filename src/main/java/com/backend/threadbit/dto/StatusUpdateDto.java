package com.backend.threadbit.dto;


import com.backend.threadbit.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDto {
    @NotNull(message = "Status is required")
    private Status status;
}