<br />
<img src="documents/images/capto-logo.png" width="80"/>


### Capto - Administrate your finances on a simple way



#### Install project
 Clone project 
```bash
    git clone https://github.com/raphaelaugustb/cstock.io/
```
Entering on project directory
  ```bash
    cd cstock.io
```
#### Test back-end
  Go to API directory and run mvn dependecies install
  ```bash
    cd backend/cstock.io && mvn clean install package 
```
Capto uses external api services you need to pass TOKEN on .env or on request params as token
  ```bash
   token=2U8gKfDMn6uaquVGTNYBwd
```
Start database on container
  ```bash
    docker compose up
```
Start app on localhost:8080
 ```bash
    mvn spring-boot:run 
```
    
#### API Documentation

You can get endpoints by downloading JSON on documents or using swagger by acessing http://localhost:8080/swagger-ui/index.html after running project

Supported by Bruno and Postman


### Documents

[Documents](https://github.com/raphaelaugustb/cstock.io/tree/main/documents)

