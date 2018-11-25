# Typelevel Workshop Final Project

## Prerequisites

 - Scala
 - sbt

## Running the Server

The sbt-revolver plugin provides two commands to start and stop the application as a background process:

```
sbt:typelevel-workshop> reStart
sbt:typelevel-workshop> reStop
```

Of course you can also use `sbt`'s triggered execution to restart the server on file changes:

```
sbt:typelevel-workshop> ~reStart
```

## Sample solution

You can see a sample solution simply by switching to the `result` branch :)


## Assignments

Your assigment is to implement an Open Source Project Index!
Features should include creating, renaming and deleting users, as well as allowing users to create their own projects
and to give other projects a "star".

For now, only these features are only partially implemented:

```
GET http://localhost:8080/projects/Cats


HTTP/1.1 200 Ok
Content-Type: application/json
Date: Mon, 10 Sep 2018 12:11:41 GMT
Content-Length: 141

{
    "id": 0,
    "name": "Cats",
    "description": "Functional abstractions for Scala",
    "owner": {
        "id": 1,
        "username": "Typelevel",
        "email": "info@typelevel.org"
    }
}

```

```
DELETE http://localhost:8080/projects/Cats

HTTP/1.1 204 No Content
Content-Type: application/json
Date: Mon, 10 Sep 2018 12:12:26 GMT
Content-Length: 0
```

```
POST http://localhost:8080/users

{ "name" : "John", "email": "jd@example.com" }

HTTP/1.1 201 Created
Content-Type: application/json
Date: Mon, 10 Sep 2018 12:13:22 GMT
Content-Length: 51

{
    "id": 2,
    "username": "John",
    "email": "jd@example.com"
}
```

### Logging

 1. Inside the `algebra` package, you can find a `Logging` algebra that's currently empty define a few logging functions
 2. In the `ProjectService` and `UserService` add logging whenever you fetch data.

### Testing

 1. Right now there are no tests, however there's a `State` interpreter for `ProjectRepository` which can be used for testing purposes, define a `State` interpreter for `UserRepository` as well.
 2. Pull out as much of the logic from the `http` package as you can into a separate algebra. You should be able to write some tests for this one using the defined `State` interpreters.


### The projects resource

 1. Currently the API can only give us a specific project by name. We should also be able to get all existing projects.
 2. There should also be a way to change the name and description of a project. Implement an endpoint for that.

### The users resource

An integral part of Twitter is following other people.

 1. Create end points for querying all users or a single user by name.
 2. Add the ability to rename a user or change his email.
 3. Implement the deletion of users, remember that a user's projects should be deleted as well.
 4. Allow users to add a new project, for which they will be the owner. This can be done in several ways, e.g. a POST request to a users' projects subresource.
 5. Next, implement an endpoint to query all the projects a user owns.

### Stars

Now we need stars...

 1. Try to change the database schema so that users can give stars to different projects. (Keep in mind that deleting a project or a user should mean deleting their stars.)
 2. Implement end points for seeing all the projects a given user has starred and all the users (stargazers) that have starred a given project.
 3. Add a way to add a star to a project from a specific user, this might require you to access the `project` and `user` tables. If working with `IO` of `Option` becomes tedious consider looking into `OptionT`. 

### User management

Currently anybody can create and delete projects. Implement authentication and authorization so that each user can only create and delete projects that they themselves own.
