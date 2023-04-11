package com.laby.projektkrypto.dto;

import java.time.LocalDate;

import com.laby.projektkrypto.entity.Coin;
import com.laby.projektkrypto.entity.EventCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto
{
    private LocalDate endDate;
    private LocalDate startDate;
    private String url;
    private String title;
    private EventCategory category;
    private Coin coin;
}