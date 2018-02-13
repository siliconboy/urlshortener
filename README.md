# URLShortener

How to start the test application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/URLShortener-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Note:
This will be a web service with an HTTP API. It will have two endpoints: one for creating short links and one for resolving a short link. You may use any language and any web frameworks (or none) to accomplish this. Below is the specification for the two endpoints
GET /:linkId Resolve short link

Response

    302 Found
        Location response header is the target URL
    404 Not Found if the specified link ID doesn't exist

POST / Create short link

Request (form data)

    url the URL to shorten
    id (optional) a friendly link ID instead of a randomly generated one

Response

    201 Created if creation is successful
        Location response header is the new short URL
        The ID of the link is either the ID specified or a randomly generated ID consisting of 6 alphanumeric characters.
    409 Conflict if id specified already exists
    
Bonus:
    links must persist upon app restart (using a MapDB )
    
    Missing:
    429 Too Many Requests if we have hit our global rate limit (see bounus)
