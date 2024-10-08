# Manager view Backend Java

This application serves as the backend for the manager view of C4Enhanced. It enables users to create and manage teams, and link those teams to C4 elements for streamlined project coordination.

## Technologies Used

### Application

* Java 17
* Spring Boot
* Spring Web
* Spring JPA
* Spring Security
* Gradle
* Apache Tika
* Hibernate
* MySQL

### Testing
* JUnit
* Mockito
* Jqwik
* H2

## Running the Application
Before running the application, follow these steps:

- Create the application.properties file
    1. Navigate to the `src/main/resources` directory.
    2. Locate the example file provided.
    3. Follow the instructions in the example file to create your own application.properties.


- Set up an empty MySQL database with the name: management_view, on port 3306. The ORM will add the necessary tables automatically.

To run the application, simply execute the `ManagerViewApplication` class. The application will start on the default port 8080.

## Building the Application

To build the application, run the following command: `./gradlew build`

This will create an executable JAR file in the `build/libs` directory.

## Testing the Application

To test the application, run the following command:
`./gradlew test`

This will run all tests in the `src/test/java` directory.

## Implemented API

<table>
    <tr>
        <th>Resource</th>
        <th>GET</th>
        <th>POST</th>
        <th>PUT</th>
        <th>DELETE</th>
    </tr>
    <tr>
        <td>/users</td>
        <td>--</td>
        <td>Posts a new user

Requestbody:

{"firstname": string, "prefixes": string, "lastname": string, "email": string, "password": string, "securityQuestion1Id": string, "securityAnswer1": string, "securityQuestion2Id": string, "securityAnswer2": string}[]
</td>
<td>--</td>
<td>--</td>
</tr>
<tr>
<td>/users/{id}</td>
<td>Gets a specific user

Responsebody:

{"firstname": string, "prefixes": string, "lastname": string, "email": string}
</td>
<td>--</td>
<td>Updates a specific user

Requestbody:

{"firstname": string, "prefixes": string, "lastname": string, "email": string, "role: Role}
</td>
<td>--</td>
<tr>
<tr>
<td>/users/{id}/password</td>
<td>--</td>
<td>--</td>
<td>Updates the password of a specific user

Requestbody:

{"oldPassword": string, "newPassword": string}
</td>
<td>--</td>
</tr>
<tr>
<td>/users?name={name}</td>
<td>Gets users by their name

Responsebody:

{"firstname": string, "prefixes": string, "lastname": string, "email": string}[]
</td>
<td>--</td>
<td>--</td>
<td>--</td>
<tr>
<td>/teams</td>
<td>
Gets all teams

Responsebody:

{"id": string, "name": string}[]
</td>
<td>
Posts a new team

Requestbody:

{ "name": string, "members": int[], "createdBy": int}
</td>
<td>--</td>
<td>--</td>
</tr>
<tr>
<td>/teams/{id}</td>
<td>
Gets a specific team

Responsebody:

{"name": string, "members": {"id": int, "firstName": string, "prefixes": string, "lastName": string}[], "createdAt":
Date}
</td>
<td>--</td>
<td>
Updates a specific team

Requestbody:

{ "name": string, "members": int[]}
</td>
<td>--</td>
</tr>
<tr>
<td>/teams/{id}/members</td>
<td>
Gets all team members of a specific team

Responsebody:

{"id": int, "firstname": string, "prefixes": string, "lastname": string}[]
</td>
<td>--</td>
<td>--</td>
<td>--</td>
</tr>
<tr>
<td>/teams/{id}/avatar</td>
<td>
Gets the avatar of a specific team

Responsebody:

{"type": String, "image": byte[]}
</td>
<td>
Posts a new avatar and links it to the team.

Requestbody:

Form:

"image": MultipartFile
</td>
<td>--</td>
<td>--</td>
</tr>
<tr>
<td>/teams/{id}/status</td>
<td>--</td>
<td>--</td>
<td>Toggles the team status (active/inactive).</td>
<td>--</td>
</tr>
</table>

