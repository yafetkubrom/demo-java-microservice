package com.optimagrowth.organization.service;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository repository;

    public Organization findById(String organizationId) {
        Optional<Organization> opt = repository.findById(organizationId);
        return opt.orElse(null);
    }

    public Iterable<Organization> findAllOrganizations() {
        return repository.findAll();
    }

    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organization = repository.save(organization);
        return organization;
    }

    public void update(Organization organization) {
        repository.save(organization);
    }

    public void delete(Organization organization) {
        repository.deleteById(organization.getId());
    }
}