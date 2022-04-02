package com.optimagrowth.licensingservice.service;

import com.optimagrowth.licensingservice.config.ServiceConfig;
import com.optimagrowth.licensingservice.model.License;
import com.optimagrowth.licensingservice.model.Organization;
import com.optimagrowth.licensingservice.repository.LicenseRepository;
import com.optimagrowth.licensingservice.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.licensingservice.service.client.OrganizationFeignClient;
import com.optimagrowth.licensingservice.service.client.OrganizationRestTemplateClient;
import com.optimagrowth.licensingservice.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


@Service
public class LicenseService {

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    final MessageSource messages;
    private final LicenseRepository licenseRepository;
    final ServiceConfig config;
    final OrganizationFeignClient organizationFeignClient;
    final OrganizationRestTemplateClient organizationRestClient;
    final OrganizationDiscoveryClient organizationDiscoveryClient;

    public LicenseService(
            MessageSource messages,
            LicenseRepository licenseRepository,
            ServiceConfig config,
            OrganizationFeignClient organizationFeignClient,
            OrganizationRestTemplateClient organizationRestClient,
            OrganizationDiscoveryClient organizationDiscoveryClient) {
        this.messages = messages;
        this.licenseRepository = licenseRepository;
        this.config = config;
        this.organizationFeignClient = organizationFeignClient;
        this.organizationRestClient = organizationRestClient;
        this.organizationDiscoveryClient = organizationDiscoveryClient;
    }


    public License getLicense(String licenseId, String organizationId, String clientType) {
        if (licenseId.isEmpty() || organizationId.isEmpty()) return null;

        License license = licenseRepository.
                findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            throw new IllegalArgumentException(
                    String.format(messages.getMessage(
                            "license.search.error.message", null, null),
                            licenseId, organizationId));
        }

        Organization organization = retrieveOrganization(organizationId, clientType);
        if (organization != null) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }

        return license.withComment(config.getProperty());
    }

    // Testing Purposes *******************************
    private void randomlyRunLong() throws TimeoutException {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;    // gives us 1-in-3 chance of sleep call
        if (randomNum != 3) sleep();
    }

    private void sleep() throws TimeoutException {
        try {
            System.out.println("sleep");
            Thread.sleep(2000);
            throw new java.util.concurrent.TimeoutException();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
    // *************************************************

    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
    @Bulkhead(name = "bulkheadLicenseService", fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLicenseByOrganization(String organizationId) throws TimeoutException {
        logger.debug("getLicensesByOrganization Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        //randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    // fallback methods must be written in same class as circuit breaker that calls it
    private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName(
                "Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }


    public License createLicense(License license /*,Locale locale*/) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId) {
        String responseMessage;
        if (licenseRepository.existsById(licenseId)) {
            licenseRepository.deleteById(licenseId);
            return null;
        }

        responseMessage = String.format(messages.getMessage(
                "license.delete.message", null, null), licenseId);

        return responseMessage;
    }

    private Organization retrieveOrganization(String organizationId, String clientType) {
        Organization organization;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);
        }

        return organization;
    }
}
