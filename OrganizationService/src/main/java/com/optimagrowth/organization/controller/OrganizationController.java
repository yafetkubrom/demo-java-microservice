package com.optimagrowth.organization.controller;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService service;

    @GetMapping
    public ResponseEntity<Iterable<Organization>> getOrganization() {
        return ResponseEntity.ok(service.findAllOrganizations());
    }

    @GetMapping(value = "/{organizationId}")
    public ResponseEntity<Organization> getOrganizationById(
            @PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(service.findById(organizationId));
    }

    @PostMapping
    public ResponseEntity<Organization> saveOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(service.create(organization));
    }

    @PutMapping
    public void updateOrganization(@RequestBody Organization organization) {
        service.update(organization);
    }

    @DeleteMapping
    public void deleteOrganization(@RequestBody Organization organization) {
        service.delete(organization);
    }

}
