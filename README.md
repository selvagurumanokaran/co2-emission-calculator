# Co2 Emission Calculator
This project calculates the amount of CO2-equivalent that will be caused when traveling between two cities using
a given transportation method. It exposes a shell command called co2-calculator to do the same. The command is as follows,

`co2-calculator --start Hamburg --end Berlin --transportation-method medium-diesel-car`

It is written leveraging the spring boot framework. It also uses Spring Shell for command lines.
For Spring Shell reference, https://docs.spring.io/spring-shell/docs/current/reference/pdf/index.pdf

The project used openrouteservice for calculating the distance between cities. 
To know more about openrouteservice, https://openrouteservice.org/

## Prerequisites

- You have `Java 8` or above version installed.
- openrouteservice API key is set in the environment variable named `ORS_TOKEN`.

## Running

1. Open the command line in your system.
2. Just run ` ./mvnw spring-boot:run` in case of Mac or Linux or `mvnw.cmd spring-boot:run` in case of Windows operating system from the root directory of this project.
3. You will see a command line like `shell:>`. Start entering your commands. 