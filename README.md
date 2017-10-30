
# API Challenge

Design and create a RESTful API that could be used to manage a list of dog images. The API is meant to act as the backend to the [Dog Breed App]
(https://gist.github.com/maxdisney/26e421c68f5d543be5b2), which is being built by a different team.

# Requirements

The operations we expect to see would be:

* List all of the available dog pictures grouped by breed
* List all of the available dog pictures of a particular breed
* Vote up and down a dog picture
* The details associated with a dog picture

The information the Dog Breed App needs to function is:

* A URL to a dog picture
* The number of time the picture was favorited
* The dog's breed
* Any other information required to identify a specific dog

The Dog Breed App expects the response to be sorted by the number of times the picture was favorited. The pictures may be sorted

The API responses must be in JSON.

## Additional Voting Requirements

Each client is allowed to vote once for any particular dog picture.

# Setup

We provide you with a starter Spring Boot project. The project is already configured go use Spring MVC and talks to an in memory HSQLDB to store the results.
Jackson is already included to provide JSON serialization and deserialization.

The Spring Boot Started Test module is included to facilitate both unit and integration tests.

Feel free to pick a different framework if you feel more comfortable doing so.

## Dependencies

This project uses Maven for builds.
You need Java 8 installed.

You need a way to expose and demo the end point you are building. [ngrok](https://ngrok.com/) is a fine way to achieve this goal.

## Checking Out the Project

The project is hosted here on Gitlab.com.

## Building

```
$ mvn package
```

## Running

You can run the app through Maven:

```
$ mvn spring-boot:run
```

or you can run the jar file from the build:

```
$ java -jar target/api_interview-0.1.0.jar
```

# Build, Deployment and Running

While this falls outside of the challenge, please consider the following as you get ready to discuss your solution with the team:

* How would you package this for deployment?
* How and where would you deploy this app?
* How can you tell that the app is up and running?
* How would you configure the app as it goes from dev, to qa and finally to production?
* How would you insulate the app from a downstream API if it had one.

# Submitting the Project

You will also receive an invitation to your own private repository on gitlab.com. Push your code to that repository as
you go. Instructions are provided by gitlab.com on how to push your code to a new repo.

# Duration

You will have until the end of the day to complete the challenge. In our tests, we found it to take us about 3 hours to build.
We encourage you to spend any leftover time to polish/document/test your app. Please send us a zip file of your challenge when completed.

The timestamp of your last commit will be used to gage how long you worked on this project.


