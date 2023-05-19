package com.thomasvitale.instrumentservice.demo;

import java.util.List;

import com.thomasvitale.instrumentservice.instrument.Instrument;
import com.thomasvitale.instrumentservice.instrument.InstrumentRepository;
import com.thomasvitale.instrumentservice.multitenancy.context.TenantContext;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration(proxyBeanMethods = false)
public class DataConfig {

	private final InstrumentRepository instrumentRepository;

	public DataConfig(InstrumentRepository instrumentRepository) {
		this.instrumentRepository = instrumentRepository;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void loadTestData() {
		TenantContext.setTenantId("dukes");
		if (instrumentRepository.count() == 0) {
			var piano = new Instrument("Steinway", "piano");
			var cello = new Instrument("Cello", "string");
			var guitar = new Instrument("Gibson Firebird", "guitar");
			instrumentRepository.saveAll(List.of(piano, cello, guitar));
		}
		TenantContext.clear();

		TenantContext.setTenantId("beans");
		if (instrumentRepository.count() == 0) {
			var organ = new Instrument("Hammond B3", "organ");
			var viola = new Instrument("Viola", "string");
			var guitarFake = new Instrument("Gibson Firebird (Fake)", "guitar");
			instrumentRepository.saveAll(List.of(organ, viola, guitarFake));
		}
		TenantContext.clear();
	}

}
