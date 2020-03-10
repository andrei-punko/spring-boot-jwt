
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

You'll receive responses similar to below

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4uYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiLCJmb28iXSwiZXhwIjoxNTgzODY4MjkwLCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiIzYzdmYmM3Ni1mM2VhLTRkYTAtYmRlOC1jYTFlNWY0MzYxMzUiLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.ki0W0_jLX4RqhnqTIdBg1j14yfJOyeipHH9W_7d-WTA",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4uYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiLCJmb28iXSwiYXRpIjoiM2M3ZmJjNzYtZjNlYS00ZGEwLWJkZTgtY2ExZTVmNDM2MTM1IiwiZXhwIjoxNTg2NDE3MDkwLCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiIwMzQyYzJmOC05MDI0LTQxNzItYWZjZC0wZDhhZjJkMjIxYzQiLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.wEg_JSEn97KulZVbqd2gq-7piacX6JZ_KOilIy0W_Pk",
    "expires_in": 43199,
    "scope": "read write foo",
    "jti": "3c7fbc76-f3ea-4da0-bde8-ca1e5f436135"
}
```

### Check existing token

To check existing token use:
```bash
curl -X POST client:secret@localhost:9090/oauth/check_token?token=TOKEN_HERE
```

You'll receive a response similar to below
```json
{
    "aud": ["testjwtresourceid"],
    "user_name": "admin.admin",
    "scope": ["read", "write", "foo"],
    "exp": 1583866486,
    "authorities": ["STANDARD_USER", "ADMIN_USER"],
    "jti": "6b4ebc1b-ea20-4f5a-a0a4-280f48ce41cc",
    "client_id": "testjwtclientid"
}
```
or next
```json
{
    "error": "invalid_token",
    "error_description": "Encoded token is a refresh token"
}
```

### Get new token pair using refresh token

To get new token pair using existing refresh token use:
```bash
curl -X POST -d refresh_token=REFRESH_TOKEN_HERE -d grant_type=refresh_token client:secret@localhost:9090/oauth/token
```

You'll receive a response similar to below
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4uYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiLCJmb28iXSwiZXhwIjoxNTgzODcwMjg5LCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiI2OTVmZmVmMi01YzQ1LTRmNTAtYmJhNy1kZTMyMWY3OWM5NjUiLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.1aB-ke4dLX1_AdBGzVPtWcG9oOEt3Gptsyz8MrSEp0I",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4uYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiLCJmb28iXSwiYXRpIjoiNjk1ZmZlZjItNWM0NS00ZjUwLWJiYTctZGUzMjFmNzljOTY1IiwiZXhwIjoxNTg2NDE4Mzg0LCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiJjYTNjN2VkMS0wYmE4LTRhMDUtYTUwZC0wNzNkZmMyN2M0YzIiLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.hXv1I-t1u_eZ3JSv-3tHSkaTzfXb-jRHG3W5sNIfIoc",
    "expires_in": 43199,
    "scope": "read write foo",
    "jti": "695ffef2-5c45-4f50-bba7-de321f79c965"
}
```

### Use tokens to access resources through your RESTful API

#### Access content available for all authenticated users:

Use the generated tokens as the value of the Bearer in the Authorization header as follows:
```bash
curl http://localhost:9091/foos/1 -H "Authorization: Bearer ACCESS_TOKEN_HERE"
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
curl http://localhost:9091/cities -H "Authorization: Bearer ACCESS_TOKEN_HERE"
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
