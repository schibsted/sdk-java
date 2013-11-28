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

=== Hints on running ===
On ubuntu do sudo apt-get install maven2 to install maven2. Try running the explorer by doing mvn jetty:run inside the directory "example". If you get missing java sdk jar error, then go to the java-sdk root dir and run mvn install. On ubuntu I had to do apt-get install default-jdk to be able to build the sdk.

If running in a vm, this is an example apache virtualhost config /etc/apache2/sites-available/devspidsdk.lo:
LoadModule proxy_module /usr/lib/apache2/modules/mod_proxy.so
LoadModule proxy_http_module /usr/lib/apache2/modules/mod_proxy_http.so

<VirtualHost *:80>

    DocumentRoot "/var/www/devspidsdk.lo"
    ServerName devspidsdk.lo
    ErrorLog "/var/log/apache2/devspidsdk.lo/error_log"
 
    ProxyPreserveHost on

    ProxyPass / http://localhost:8080/
    ProxyPassReverse / http://devspidsdk.lo/
</VirtualHost>

Remember to add devspidsdk.lo in /etc/hosts in the vm (pointing to localhost) and on your host dev box pointing to vm ip.

CLIENT_ID = 558xxxxxxxxxxxxxxxxxxxxxxxxx
CLIENT_SECRET = 2ef3xxxxxxxxx
REDIRECT_DOMAIN = http://devspidsdk.lo
SPP_PROTOCOL_AND_DOMAIN = http://id.vgnett.no
API_VERSION=2
