package com.thomasvitale.instrumentservice;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContextHolder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TenantController {

    @GetMapping("tenant")
    String getCurrentTenant() {
        return TenantContextHolder.getTenantIdentifier();
    }

}
