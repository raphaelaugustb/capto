<br />
<img src="documents/images/capto-logo.png" width="80"/>


### Capto - Administrate your finances on a simple way



#### Setup project
 Clone project 
```bash
    git clone https://github.com/raphaelaugustb/cstock.io/
```
Entering on project directory
  ```bash
    cd cstock.io
```
#### Setup back-end
  Go to API directory and run mvn dependecies install
  ```bash
    cd backend/cstock.io && mvn clean install package 
```
Start database on container
  ```bash
    docker compose up
```
Start app on localhost:8080
 ```bash
    mvn spring-boot:run 
```
Capto uses external api services you need to pass TOKEN on .env or on request params as token
  ```bash
   token=2U8gKfDMn6uaquVGTNYBwd
```
### Documents
[API Endpoints](https://github.com/raphaelaugustb/cstock.io/tree/main/documents/api)

[Documents](https://github.com/raphaelaugustb/cstock.io/tree/main/documents)

