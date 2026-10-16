package com.thomasvitale.instrumentservice.demo;

import java.util.List;

import com.thomasvitale.instrumentservice.instrument.Instrument;
import com.thomasvitale.instrumentservice.instrument.InstrumentRepository;

import io.arconia.multitenancy.core.context.TenantContext;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration(proxyBeanMethods = false)
class DataConfig {

	private final InstrumentRepository instrumentRepository;

	DataConfig(InstrumentRepository instrumentRepository) {
		this.instrumentRepository = instrumentRepository;
	}

	@EventListener(ApplicationReadyEvent.class)
	void loadTestData() {
        TenantContext.where("dukes").run(() -> {
            if (instrumentRepository.count() == 0) {
                var piano = new Instrument("Steinway", "piano");
                var cello = new Instrument("Cello", "string");
                var guitar = new Instrument("Gibson Firebird", "guitar");
                instrumentRepository.saveAll(List.of(piano, cello, guitar));
            }
        });

        TenantContext.where("beans").run(() -> {
            if (instrumentRepository.count() == 0) {
                var organ = new Instrument("Hammond B3", "organ");
                var viola = new Instrument("Viola", "string");
                var guitarFake = new Instrument("Gibson Firebird (Fake)", "guitar");
                instrumentRepository.saveAll(List.of(organ, viola, guitarFake));
            }
        });
	}

}
