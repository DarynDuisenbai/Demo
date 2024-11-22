# Module "Automation of Subpoenas and Summons"

The **"Automation of Subpoenas and Summons"** module is part of a system designed to manage subpoenas and summons efficiently, streamlining investigative actions related to criminal cases. This module supports creation, management, and approval workflows for conclusion cards.

---

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Module Structure](#module-structure)
   - [Login Page](#login-page)
   - [Profile Page](#profile-page)
   - [Creating Cards](#creating-cards)
   - [Journal of Conclusions](#journal-of-conclusions)
4. [Roles and Permissions](#roles-and-permissions)
5. [Database Configuration](#database-configuration)
6. [Installation](#installation)
7. [Usage](#usage)
8. [API Documentation](#api-documentation)
9. [Build and Deployment](#build-deployment)

---

## Overview

The **"Automation of Subpoenas and Summons"** module is a key component of a larger system that facilitates:
- Managing subpoenas and summons for investigative actions.
- Automated workflows for creating, reviewing, and approving conclusion cards.
- Role-based access control for efficient operation.

---

## Features

### General Functionalities
- User login and profile management.
- Creation and editing of conclusion cards.
- Approval workflows for conclusions.
- Comprehensive journal for managing conclusion records.
- Exporting records to Excel and PDF formats.

### Module Capabilities
- Automatic card registration and unique numbering.
- Pre-filled fields based on criminal case numbers.
- Filters for advanced searches in the journal.
- Role-specific access control and UI elements.
- Highlighting tasks for specific users for better focus.

---

## Module Structure

### Login Page
Provides user authentication with the following:
- Fields: Email/Username, Password.
- Buttons: "Login".
- Links: "Forgot Password?", "Register".
- Validation: 
  - Email format.
  - Minimum 6 characters for the password.
- Error Messages:
  - Invalid credentials.
  - Empty fields.

---

### Profile Page
Enables users to manage their profiles:
- View: Name, Email, Registration Date, Profile Picture.
- Edit: Name, Email, Change Password.
- Account Deletion: Confirm before deletion.

---

### Creating Cards
Supports creating conclusion cards with the following features:
- Automatic unique registration number (`Z001` format).
- Auto-filling fields such as:
  - Case details, defendant's information, and prior summons history.
- Mandatory fields:
  - National ID (IIN, 12 digits), case details, and justification.

---

### Journal of Conclusions
Manages conclusion records in a tabular format with:
- Filters for advanced searches (e.g., date range, region, status).
- Role-based access:
  - Employee: Access own records.
  - Analyst: Access department records.
  - Moderator: Full access.
- Export options to Excel and PDF formats.

---

## Roles and Permissions
- **Employee (SU Staff):** 
  - Create and edit their own records.
- **Analyst (SD):**
  - View all records in their department and region.
- **Moderator:**
  - Full read access to all records.

---

## Database Configuration


This system uses **MongoDB Atlas** as its database solution. Configure the connection in `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://Daryn:1234@myatlasclusteredu.z25a02h.mongodb.net/?retryWrites=true&w=majority&appName=myAtlasClusterEDU
```

## Installation
1. Clone the repository
   ```bash
   git clone https://github.com/your-repo/your-project.git
   cd your-project
   ```
2. Set up the database connection in application.yml.
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
  java -jar target/your-application.jar
  ```

## Usage 
1. Access the application at http://localhost:8080 after starting the server.
2. Navigate to the Login page and authenticate.
3. Access various modules via the sidebar menu:
   * Profile
   * Create new conclusions
   * View and manage records in the Journal.

## API Documenatation
* Swagger UI is available at http://localhost:8080/swagger-ui.html for API exploration and testing.

## Build and Deployment
1. Build and Deployment
   ```bash
   docker build -t your-application.
   ```
2. Run the application using Docker:
   ```bash
   docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod your-application
   ```
