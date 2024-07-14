![image](https://github.com/user-attachments/assets/0cd16aa2-48c7-43a1-9c21-02ecec901a11)



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

