![image](https://github.com/raphaelaugustb/cstock.io/assets/66183690/7a493963-d113-4126-a3b3-f672487f8728)

Here you can store and administrate your finances on a simple way. You can test it by yourself following this steps




## Install project


 Clone project 
```bash
    git clone https://github.com/raphaelaugustb/cstock.io/
```
Entering on project directory
  ```bash
    cd cstock.io
```
## Init back-end
  Go to API directory and run mvn dependecies install
  ```bash
    cd backend/cstock.io && mvn clean install package 
```
Start MySql database
  ```bash
    docker compose up
```
Start app on localhost:8080
 ```bash
    mvn spring-boot:run 
```
    
## API Documentation

You can acess all of it methods downloading JSON endpoints on documents or using swagger by acessing http://localhost:8080/swagger-ui/index.html after running project

Supported by Bruno and Postman


## Documents

[Documents](https://github.com/raphaelaugustb/cstock.io/tree/main/documents)

