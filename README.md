# Gym crm application

## Prerequisites

To run this application, you should have the following installed:

- **Java Development Kit (JDK) 17**
- **Maven**
- **Git**

## Setup Instructions

Set Up Global Environment Variables

- DB_URL = "jdbc:postgresql://localhost:5432/gym"
- DB_USERNAME = "gca"
- DB_PASSWORD = "gca"

Run the following script to create the database and add a user:

```sql
CREATE DATABASE "gym";
CREATE USER gca WITH PASSWORD 'gca';
GRANT ALL PRIVILEGES ON DATABASE "gym" TO gca;
```

