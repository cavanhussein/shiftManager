# Shift Manager
Welcome to my shift manager repository. The goal of this project is to allow a user manage their work shifts stored in a database (i.e. create, update, delete, and get work shifts)

Below you will find a UML diagram showing the flow for a given HTTP request.
```
[Client] -- HTTP Request --> ((Controller)) --> {Service} --> [[MongoDB respository]] --> [MongoDB]
```

This project is developed using Spring boot 2.4.1 should be built and ran using **Gradle 6.7.1** and **Java 8** respectively.

In order to run this application the user will need to clone this repository and run the following in the root of the repository: `./gradlew build`

Once the gradle process succeeds you will find the Shift Manager's jar file in: `REPO_ROOT/build/libs/`

Now you should be able to run the jar file using the following command from the root of the repository:
```
 $JAVA_HOME/java -Djdk.tls.client.protocols=TLSv1.2 -Dspring.config.location=src/main/resources/application.properties -jar ./build/libs/shift-0.0.1-SNAPSHOT.jar
```
Note, $JAVA_HOME will resolve to something like this: `C:\Program Files\Java\jdk-13.0.2\bin\`
Also note, that this project is currently connected to a remote MongoDB.

This Shift manager supports five different APIs and please note the datetime formats are the following for all request bodies and responses: `yyyy-MM-dd'T'HH:mm:ssZ` (e.g 2020-12-23T17:00:00+0600)

* GET /api/shift: 
	* This will return a list of all shifts stored in the database. You can filter on the startTime and endTime using the following search parameters:  `?startTime=2020-12-23T17:00:00+0600&endTime=2020-12-24T17:00:00+0600`
	* Example response:
```
[
  {
  "id": "1111",
  "startTime": "2020-12-24T13:00:00+0000",
  "endTime": "2020-12-24T14:00:00+0000",
  "userId": "grant"
  },
  ...
]
```
* GET /api/shift/{id}:
	* This will return a single shift filter by the shift ID.
	* Example response:
```
{
  "id": "1111",
  "startTime": "2020-12-24T13:00:00+0000",
  "endTime": "2020-12-24T14:00:00+0000",
  "userId": "grant"
}
```
* POST /api/shift:
	* This will attempt to create a new shift for given user. Note, if there is an existing shift for this user that conflicts with the new shift's startTime or endTime a Bad request error will be returned saying there is a conflicting shift.
	* Response will be similar to the GET by ID response.
	* The request body should look like the following: 
```
{
  "userId":  "testUser",
  "startTime":  "2020-12-24T17:00:00+0600",
  "endTime":  "2020-12-24T19:00:00+0600"
}
```
* PATCH /api/shift/{id}:
	* This will attempt to update an existing shift for given user. Note, if there is another existing shift for this user that conflicts with the given shift’s startTime or endTime a Bad request error will be returned saying there is a conflicting shift.
	* Response will be similar to the GET by ID response.
	* The request body should look like the following:
```
{
  "startTime":  "2020-12-24T7:00:00+0000",
  "endTime":  "2020-12-24T8:00:00+0000"
}
```

* DELETE /api/shift/{id}:
	* This will attempt to delete an existing shift by a given shift ID.
