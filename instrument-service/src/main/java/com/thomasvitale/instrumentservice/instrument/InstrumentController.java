package com.thomasvitale.instrumentservice.instrument;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("instruments")
public class InstrumentController {
  
	private static final Logger log = LoggerFactory.getLogger(InstrumentController.class);
	private final InstrumentRepository instrumentRepository;

	InstrumentController(InstrumentRepository instrumentRepository) {
    	this.instrumentRepository = instrumentRepository;
	}

  	@GetMapping
  	List<Instrument> getInstruments() {
    	log.info("Returning all instruments");
    	return instrumentRepository.findAll();
  	}

	@GetMapping("{type}")
	@Cacheable(cacheNames = "instrumentTypes", keyGenerator = "tenantKeyGenerator")
	List<Instrument> getInstrumentByType(@PathVariable String type) {
    	log.info("Returning instrument of type: {}", type);
    	return instrumentRepository.findByType(type);
	}

	@PostMapping
	Instrument addInstrument(@RequestBody Instrument instrument) {
    	log.info("Adding instrument: {}", instrument.getName());
    	return instrumentRepository.save(instrument);
	}

}
