package com.laby.projektkrypto.dao;

import java.time.LocalDate;

import com.laby.projektkrypto.entity.Coin;
import com.laby.projektkrypto.entity.EventCategory;
import lombok.Builder;

@Builder
public record FindEventRequest(LocalDate endDate, LocalDate startDate, EventCategory category, Coin coin)
{
}
