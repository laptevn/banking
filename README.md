# Banking Services
Scope of work is covered in [PDF](/homework.pdf). It's vague and contains some confusion, some assumptions were made to mitigate it.

## Components
1. Account service.
2. Bank service.
3. Service Discovery.
4. API Gateway.
5. Configuration server.<br />
Account and Bank services store their configurations there. Other services have nothing to store there now.
6. Monitoring service.<br />
There is no dedicated monitoring service. Service Discovery solves this need.<br />
Rationale is that monitoring is useful not by itself but within automation suit (e.g. having load balancer to choose healthy service).<br />
Eureka is used as Service Discovery tool. It displays health status of each service and provides this status to load balancers (there is at least one used implicitly by Eureka).<br />
7. Authorization service.<br />
Auth0(https://auth0.com/) is used as authorization service, SaaS based.

Central part of a network is Service Discovery. All other services are registered there.

## How to build?
All projects can be build with `mvn clean install` command

## How to run?
Project deliverables are packed into Docker containers and run with Docker Compose.<br />
In order to build all projects and their containers run `build_containers.sh` script.<br />
Then run `docker-compose up -d` command in root directory to run all containers. Don't forget to run `docker-compose down` when you don't need them anymore.<br />

You need Docker demon to run them locally.<br />
You can run them on any Docker host too, detailed instructions for this are out of scope of this document. However you just need to add one parameter to Docker Compose for this - `-H`.<br />

Please note that it takes some time to make Account and Bank services up and running.<br />
Rationale is that they use Configuration service using DNS name from service discovery. So to start using them we need to wait until container with service discovery is started, then until Configuration service is registered there, then until they find Configuration service in the registry (they have a timer for this), then until they are started and registered in service discovery. All these are done automatically but takes time.<br />
On my slow MacBook Pro it takes about 5 minutes to get Account and Bank services up and running assuming 5 Docker containers are running. Configuration service discovery is a bottleneck here, without it all services are up and running within 1 minute on my laptop.

## Using API
You can see URL+Port from Docker Compose file for each service. For example API Gateway is available via http://localhost:8083<br />

Add the following header to make your request authorized:<br />
**key**: Authorization<br />
**value**: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlFUUkVNVGhCTmprMU9FTTJORGhETnpZMk56QTVPVVl5TlRjek5EVTBRME5GTTBSQ05UQTBPUSJ9.eyJpc3MiOiJodHRwczovL2xhcHRldi5ldS5hdXRoMC5jb20vIiwic3ViIjoiRkR1eW11WXF5ZkNCdTB3OUlmSENZR0lucEdaWE42akxAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vYmFua2luZy5sYXB0ZXYuY29tIiwiaWF0IjoxNTUyNTY0MzY0LCJleHAiOjE1NTUxNTYzNjQsImF6cCI6IkZEdXltdVlxeWZDQnUwdzlJZkhDWUdJbnBHWlhONmpMIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.Q3LqFBhrxulfSQH2O0KNXZSPhnulOT0S5MDPxXBODpEr6d900lRvdTHIy-GkZR_l5pd0xLKJFk4x0hWh29IuqwEpPwjrKacffYJTtoCIv5Nh3PDjv-azSC6ygWu-yTu1dvi7BUptwfGKEOnDIeIHsmdu9ogonxv66CHR2YqF7jRoM3IKkh46-tWMwyh9ARxIw2LDJBvG2wtU8gO2qH6VNW8Bwlro-FThe1ckQt-DTME4D1oqY9_H_Ws6LM_8vw2KwKx6lHM5Fqhau03QRzhYasHQ_LLs62bl5uMimnx_vgkEH-2IbT_qtKpU9QCzyAkwlNfZTMuEjtLKN8McgyNvJA

Please note that Circuit Breaker is used by Bank service explicitly and API Gateway implicitly. So don't worry if you invoked some endpoint without baking service running, just wait for some time for Circuit Breaker to restore connection with this service.

## Services state
You can see health status of all services here http://localhost:8761/<br />
If some service is not listed there, other services will not see it.<br />
Please note if some service is displayed on this page, that doesn't mean other services will immediately see it. They need some time to update service state from Service Discovery. It takes tens of seconds usually but can be configured.

## Further improvements
1. Replace Auth0 with mock server to use in integration tests. Auth0 doesn't support anything specific for test purposes and usage of real services will be expensive.
2. Add integration test for bank service with working account service and then drop it to make cache working.
3. Speed up Configuration service discovery.