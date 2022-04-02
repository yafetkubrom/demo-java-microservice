package com.optimagrowth.licensingservice.controller;

import com.optimagrowth.licensingservice.model.License;
import com.optimagrowth.licensingservice.service.LicenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.Locale;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {
    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping(value = "/{licenseId}")
    public ResponseEntity<License> getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {

        License license = licenseService.getLicense(licenseId, organizationId, "");

        // HATEOAS
        license.add(
                linkTo(methodOn(LicenseController.class)
                        .getLicense(organizationId, license.getLicenseId()))
                        .withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .createLicense(license))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicense(license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(license.getLicenseId()))
                        .withRel("deleteLicense")
        );

        return ResponseEntity.ok(license);
    }

    @GetMapping(value = "/{licenseId}/{clientType}")
    public ResponseEntity<License> getLicensesWithClient(@PathVariable("organizationId") String organizationId,
                                                        @PathVariable("licenseId") String licenseId,
                                                        @PathVariable("clientType") String clientType) {
        return ResponseEntity.ok(licenseService.getLicense(licenseId, organizationId, clientType));
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<License>> getAllLicenses(
            @PathVariable("organizationId") String organizationId) throws TimeoutException {
        return ResponseEntity.ok(licenseService.getLicenseByOrganization(organizationId));
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License request
            /*@RequestHeader(value = "Accept-Language", required = false) Locale locale*/) {
        return ResponseEntity.ok(licenseService.createLicense(request/*, locale*/));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));

    }
}
