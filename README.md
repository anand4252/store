# Generic Store API
The application exposes the below endpoints. Have used H2 database with some sample values 
loaded for the sake of simplicity. 

#### Surge logic:
There are 3 parameters to consider:
- hit limit
- window period
- price percentage

***NOTE:*** The above fields are configurable and can be passed while running the app. 
***Logic:*** 
- For an item, when the number of hits is more than the _hit limit_ parameter in a 
given _window period_ then the price of the item would be increased by _price percentage_
- For an item, when the number of hits is less than the _hit limit_ parameter in a 
given _window period_, and the price has already been increase for that item, 
then its price will be decreased by _price percentage_

#### A couple questions to consider: 
- How do we know if a user is authenticated? 
    - Only when the user is authenticated a valid jwt is generated. 
    Otherwise, the request will be rejected. 
- Is it always possible to buy an item? 
    - You can only view or but the item only when the user is authenticated. 
    If required the view endpoints can be made accessible without the need to authorize.

  
#### POST /store/login
- This is the first endpoint to be invoked. 
- Used Spring security for Authentication and JWT for Authorization. 
- The username and password are hardcoded.
- Returns a _JWT_ which should be used to access other endpoints

***Request:***
```bash
{
    "username":"anand",
    "password":"pass5"
}
```
***Sample Response:***
```bash
{
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmFuZCIsImV4cCI6MTU5NTI5MDY5OCwiaWF0IjoxNTk1MjU0Njk4fQ.y6YyqE1x1f_zPGEuuJGZEFYCqtVeH1sMh2TiKTg5TJ4"
}
```

#### GET /store/v1/items
- Returns all the items in the store. 
- A valid JWT should be passed in the request header.

***Sample Response:***
```
[
    {
        "name": "Creatine",
        "description": "Naked creatine",
        "price": 10.0
    },
    {
        "name": "Protein bar",
        "description": "28gm protein bar",
        "price": 20.0
    },
    {
        "name": "Whey",
        "description": "optimum nutrition 100% Gold standard",
        "price": 30.0
    },
    {
        "name": "Casein",
        "description": "Slowly Release amino acid",
        "price": 40.0
    }
]
```
#### GET /store/v1/item/{id}
- Return the item with the id mentioned in the URL.
- A valid JWT should be passed in the request header.

***Sample Response:***
```
{
    "name": "Protein bar",
    "description": "28gm protein bar",
    "price": 20.0
}
```

#### PUT /store/v1/item/{id}/order
- Returns a one of the following message when an order is placed.
    - OUT_OF_STOCK
    - ORDER_PLACED
    - INVALID
- URL holds the item id.    
- A valid JWT should be passed in the request header.

***Sample Response:***
```
ORDER_PLACED
```
## Key features to consider
- Have externalized the key attributes i.e the surge window time, price increase percentage and hits threshold. 
This makes the app more flexible and configurable as per the demands.
- Have created my own fixed size CircularArrayList data structure(Unit tested). It is used to implement Surge logic 
rather than to clutter the DB with viewed times for each item.
- For the most parts, followed TDD approach, especially for the Surge logic
- Used Lombok to reduce boilerplate code

## Testing
- Have used JUNIT 5 to implemented all the test cases
- Integration test cases alone use Springboot runner. 
- Unit tests use Mockito runner. It is quick as it doesn't have to start the Spring container.
- Surefire reports can be found under the surefire-reports once you build the app

## Instructions to Build

From the root folder run the below [maven](https://maven.apache.org/) command to create the jar and run test cases.

```bash
mvn clean package
```

## Instructions to Run
#### Using plain java -jar command.
First, change to _target_ directory and then run the following command.

```bash
java -jar store-0.0.1-SNAPSHOT.jar
```

#### Using [maven](https://maven.apache.org/) command from the _root_ directory.

```bash
mvn spring-boot:run
```

The app should run in the port 8000. To change the port use the following command.
```bash
java -jar store-0.0.1-SNAPSHOT.jar --server.port=9001 
```
#### Using [docker](https://docs.docker.com/) 
***Command to Build:***
```bash
docker build --tag store:1.0 .
```
***Command to Run:***
```bash
docker run --publish 8000:8000 --name store_container store:1.0
```

## Improvements
- The user details to login are hard coded. The user details can be maintained in the DB.
An ideal case would be to have a separate Authentication server and follow Oauth2 flow.
- Introduce API gateway (Like Spring Cloud Zuul) to intercept all the request have the 
Authentication and Authorization implemented there. 
- Hashing(with salting) can be implemented for better protection 
- Roles can be added to the Users thereby restricting access to a few endpoints for some users
- Error handling using controller advice.
- Implement shopping cart feature


## Feedback
Please share your valuable feedback as that's how I learn. :)
