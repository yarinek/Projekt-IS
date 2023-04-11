package com.laby.projektkrypto.dao;

import org.springframework.data.repository.CrudRepository;

import com.laby.projektkrypto.entity.Event;

public interface EventRepository extends CrudRepository<Event, Integer>
{
}