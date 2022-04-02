Tasks
    - clean up resources/config
        - have a dev/prod/default credentials
        - DONE

    - set up git connection
        - DONE

    - set up vault
        - FOR LATER

    - Fix findAll() databases query
        - DONE

    - on licensing-service
        - set up language locale
            - DONE
        - set up error messages
            - DONE
        - take out config parameter
            - DONE
    - finally, write kubernetes script
        - DONE
    - add confi on page 164 to git (first start git for organization service)
        - DONE
    - redundant organization id when sending GET request
      	- sending organization id on path and variable
      	- possible solution : use organization id on variable instead
    - clean up & find easier way of sending id
        - the UUID is too long
            - DONE (decided its okay for now)
    - Create tests for all classes (especially controllers)
    - Provision a vault (locally & on kube)
    - Learn how to turn off Eureka (for testing purposes)
    - Think about the flow. Fix update and delete requests. Look Below.
    - For POST and UPDATE, I am sending organizationID twice through @PathVariable & @RequestBody.
    - change .properties to .yml
    - implement circuit breaker monitor only
    - implement the fallback suggestions from page 194
    - Learn about health indicators. then show only CB info.
    - implement Liquibase Database migration
    - Write Docker files for all services

Credentials
    - vault
        - name = vault
        - ROOT_TOKEN_ID = myroot
        - port = 8200