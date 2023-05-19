package com.thomasvitale.instrumentservice.instrument;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentRepository extends JpaRepository<Instrument,UUID> {
	List<Instrument> findByType(String type);
}
