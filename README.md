# SmartHome

This project is the implementation of my thesis for graduating as Computer Engineer (MSc), Technical University of
Sofia, branch Plovdiv, Computer Systems and Technologies.

## Description:

A simple client-server application for managing IoT devices.

## Table of Contents:

- Installation
    - Project structure
    - Get up and running - how to install project locally?
- Usage
    - How to use the project after installing it?
    - REST APIs
- Tests
- Dependencies

## Installation:

1. Execute `docker-compose -f /docker/docker-compose.yaml up` in the project's root directory to spin up instances of
   PostgreSQL, InfluxDB, Eclipse Mosquitto, PgAdmin, Fake-smtp server and Apache HTTPD.
2. Start the server application `./gradlew bootRun`
3. Access the UI at `http://localhost:8888/smarthome`

## Usage:

#### API docs:
You can find the API docs [here](docs/README.md).

## Tests:

## Dependencies:
