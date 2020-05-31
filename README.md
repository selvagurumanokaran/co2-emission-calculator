# Co2 Emission Calculator
This project calculates the amount of CO2-equivalent that will be caused when traveling between two cities using
a given transportation method. It exposes a shell command called co2-calculator to do the same. The command is as follows,

`co2-calculator --start Hamburg --end Berlin --transportation-method medium-diesel-car`

## Prerequisites

- You have `Java 8` or above version installed.
- You have maven `3.6.1` installed.
- openrouteservice API key is set in the environment variable named `ORS_TOKEN`.

## Running

1. Open the command line in your system.
2. Build the project by running `mvn clean package`.
2. Just run ` ./co2-calculator` in case of Mac or Linux or `co2-calculator.cmd` in case of Windows operating system from the root directory of this project.

## Running test cases

1. Open the command line in your system.
2. Just run `mvn test` to run the test cases.
