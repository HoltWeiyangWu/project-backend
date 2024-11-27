# Holt's Personal Project

## Overview

This project is a full-stack web application built as a personal learning exercise to explore modern web development technologies. The application features a Spring Boot backend connected to a MySQL database for managing and storing data, and modifies a React frontend template provided by Minimal UI.

The project is designed to simulate a real-ward scenario where users are able to search product information and administrators are able to perform user and product management CRUD (Create, Read, Update, Delete) operations. The website is deployed with Heroku and can be potentially deployed with AWS or Azure for future needs.

## Backend Features
- Java Springboot framework (e.g. Spring JPA, Spring MVC)
- CRUD for user managements
- JWT interceptor
- Heroku deployment with MySQL

## Usage

Please refer to https://github.com/HoltWeiyangWu/project-frontend for detailed information.

## Quick Start

- **Set up:** Download Java. Use Oracle OpenJDK 22.0.1.
- **Environment variable:** 
  - Change MySQL database connection details (i.e. url, username, password) in application.yaml
  - Change "@CrossOrigin" in "UserController" to avoid CORS issue
- **Build:** Build Maven dependencies and build java application.
- **Start:** Run "BackendApplication" main function.


## Reference and License
### Frontend application

> https://github.com/HoltWeiyangWu/project-frontend

### License
Distributed under the MIT License.