=== Running the explorer ===
Copy /WEB-INF/config-dist.properties to /WEB-INF/config.properties and 
replace the tokens with your details.   

Run mvn jetty:run.

=== Running jUnit-tests ===
Copy the config-dist.properties to config.properties in /test/resources/ 
in the spp-sdk-project and replace the tokens with your details. 

By default integration tests are excluded from maven test phase since they 
depend on the server state.  
Include integration tests by running the 'integrationtest'-profile. (mvn test -Pintegrationtest)

=== NOTE! ===
Never commit your local config.properties-files!
