# Holt's Personal Project

## Overview

This project (https://holtwywproject.me) is a full-stack web application built as a personal learning exercise to explore modern web development technologies. The application features a Spring Boot backend connected to a MySQL database for managing and storing data, and modifies a React frontend template provided by Minimal UI.

The project is designed to simulate a real-ward scenario where users are able to search product information and administrators are able to perform user and product management CRUD (Create, Read, Update, Delete) operations. The website is deployed with Heroku and can be potentially deployed with AWS or Azure for future needs.

## Backend Features
- Java Springboot framework (e.g. Spring JPA, Spring MVC)
- Image storage with Amazon Web Service(AWS) S3
- CRUD for user managements
- JWT interceptor
- Heroku deployment with MySQL

## Usage

Please refer to https://github.com/HoltWeiyangWu/project-frontend for detailed information.

## Quick Start

- **Set up:** Download Java. Use Oracle OpenJDK 22.0.1.
- **Environment variable:** Add environment variables specified  in application.yaml (e.g. url, AWS S3)
- **Build:** Build Maven dependencies and build java application.
- **Start:** Run "BackendApplication" main function with environment variables.


## Reference and License
### Frontend application

> https://github.com/HoltWeiyangWu/project-frontend

### License
Distributed under the MIT License.
