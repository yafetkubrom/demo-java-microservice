package com.optimagrowth.licensingservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LicensingServiceApplicationTests {

    //@Test
    @Disabled
    void contextLoads() {
    }

}

// Cant test database related classes with junit. aim for integration testing.
// might have to write a database script for testing