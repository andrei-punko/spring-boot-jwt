
# An example of Spring Boot application for securing a REST API with JSON Web Token (JWT)

## Main building blocks
 * Spring Boot 2.2.5.RELEASE go to http://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/ to learn more about spring boot
 * JSON Web Token go to https://jwt.io/ to decode your generated token and learn more
 * H2 Database Engine - used for rapid prototyping and development, but not suitable for production at least in most cases. Go to www.h2database.com to learn more

## To run the application

```bash
./gradlew build
java -jar ./oauth-server/build/libs/oauth-server-0.0.1-SNAPSHOT.jar
java -jar ./resource-server/build/libs/resource-server-0.0.1-SNAPSHOT.jar
```

Or import the project into your IDE and run `AuthServerApplication` & `ResourceServerApplication` from there.

## To test the application

### First you will need the following basic pieces of information:

 * client: `testjwtclientid`
 * secret: `XY7kmzoNzl100`
 * Non-admin username and password: `john.doe` and `jwtpass`
 * Admin user: `admin.admin` and `jwtpass`
 * Example of resource accessible to all authenticated users: http://localhost:9091/foo/123 (any number here)
 * Example of resource accessible to only an admin user: http://localhost:9091/cities

### Generate an access token

Use the following generic command to generate an access token:

```bash
curl client:secret@localhost:9090/oauth/token -d grant_type=password -d username=user -d password=pwd
```

For this specific application, to generate an access token for the non-admin user john.doe, run:

```bash
curl testjwtclientid:XY7kmzoNzl100@localhost:9090/oauth/token -d grant_type=password -d username=john.doe -d password=jwtpass
curl testjwtclientid:XY7kmzoNzl100@localhost:9090/oauth/token -d grant_type=password -d username=admin.admin -d password=jwtpass
```

You'll receive a responses similar to below

```json
    {
      "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4uYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNDk0NDU0MjgyLCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiIwYmQ4ZTQ1MC03ZjVjLTQ5ZjMtOTFmMC01Nzc1YjdiY2MwMGYiLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.rvEAa4dIz8hT8uxzfjkEJKG982Ree5PdUW17KtFyeec",
      "token_type": "bearer",
      "expires_in": 43199,
      "scope": "read write",
      "jti": "0bd8e450-7f5c-49f3-91f0-5775b7bcc00f"
    }
```

### Use tokens to access resources through your RESTful API

#### Access content available for all authenticated users:

Use the generated tokens as the value of the Bearer in the Authorization header as follows:

```bash
curl http://localhost:9091/foos/1 -H "Authorization: Bearer TOKEN_HERE"
```

The response will be:

```json
{
    "id": 68,
    "name": "rRun"
}
```

#### Access content available only for an admin user:

```bash
curl http://localhost:9091/cities -H "Authorization: Bearer TOKEN_HERE"
```

The result will be:

```json
[{
        "id": 1,
        "name": "Bamako"
    }, {
        "id": 2,
        "name": "Nonkon"
    }, {
        "id": 3,
        "name": "Houston"
    }, {
        "id": 4,
        "name": "Toronto"
    }, {
        "id": 5,
        "name": "New York City"
    }, {
        "id": 6,
        "name": "Mopti"
    }, {
        "id": 7,
        "name": "Koulikoro"
    }, {
        "id": 8,
        "name": "Moscow"
    }
]
```
