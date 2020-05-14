# Graph Sentiment Service

A basic spring and neo4j based service to show a people sentiment based on feedback comment.

The sentiment engine in this example using GCP NLP API, mock sentiment also provided.

## Build and Start
### Configuration

All these steps are done using docker.

Your GCP account credential or (service account cred) should be in JSON format and stored
under `src/resources`

And defined the path on `gcp.account.credential`


Default port is 8081, change `application.yaml` to match what you need.
```
./gradlew build

docker build -t nickom/reporting-service .

```

And then run.
```
docker-compose up -d
```
## Operations example
### Adding a person
#### request
```
curl --location --request POST 'localhost:8081/report/v1/person' \
--header 'Content-Type: application/json' \
--header 'x-tenant: 98c2e7dc-b2c7-49d9-8cbb-93501a00220f' \
--data-raw '{
	"email": "Jim_Halpert@office.com",
	"name": "Jim Halpert"
}'
```
#### response
```
{
    "id": "1ff55355-52ec-42af-b45b-defd8c7a8f87",
    "timestamp": "2020-05-14T03:23:24.663Z[GMT]"
}
```

### Adding a subordinate relation
#### request
```
curl --location --request PATCH 'localhost:8081/report/v1/person/9dc6bfff-1173-4906-bab5-7af00a49463e' \
--header 'Content-Type: application/json' \
--header 'x-tenant: 98c2e7dc-b2c7-49d9-8cbb-93501a00220f' \
--data-raw '{
    "subordinateIds": [
        "1ff55355-52ec-42af-b45b-defd8c7a8f87"
    ]
}'
```
#### response
```
{
    "id": "9dc6bfff-1173-4906-bab5-7af00a49463e",
    "email": "Michael_Scott@office.com",
    "name": "Michael Scott",
    "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f",
    "managers": [],
    "subordinates": [
        {
            "id": "1ff55355-52ec-42af-b45b-defd8c7a8f87",
            "email": "Jim_Halpert@office.com",
            "name": "Jim Halpert",
            "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f",
            "sentiments": null
        }
    ],
    "sentiments": null
}
```

### Giving a feedback comment
#### request
```
curl --location --request POST 'localhost:8081/report/v1/sentiments' \
--header 'Content-Type: application/json' \
--header 'x-tenant: 98c2e7dc-b2c7-49d9-8cbb-93501a00220f' \
--header 'Content-Type: text/plain' \
--data-raw '{
    "author": "9dc6bfff-1173-4906-bab5-7af00a49463e",
    "subject": "1ff55355-52ec-42af-b45b-defd8c7a8f87",
    "text": "I’m his boss actually. And I treat him well. I’m taking him out to lunch cause I can afford it and he can have whatever he wants."
}'
```
#### response
```
{
    "id": "9dc6bfff-1173-4906-bab5-7af00a49463e",
    "email": "Michael_Scott@office.com",
    "name": "Michael Scott",
    "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f",
    "managers": [],
    "subordinates": [
        {
            "id": "1ff55355-52ec-42af-b45b-defd8c7a8f87",
            "email": "Jim_Halpert@office.com",
            "name": "Jim Halpert",
            "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f"
        }
    ],
    "sentiments": [
        {
            "id": 13,
            "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f",
            "quantifiedScore": 0.4,
            "totalFeedback": 2,
            "categories": [
                "[]"
            ],
            "subject": {
                "id": "1ff55355-52ec-42af-b45b-defd8c7a8f87",
                "email": "Jim_Halpert@office.com",
                "name": "Jim Halpert",
                "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f",
                "managers": [
                    {
                        "id": "9dc6bfff-1173-4906-bab5-7af00a49463e",
                        "email": "Michael_Scott@office.com",
                        "name": "Michael Scott",
                        "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f"
                    }
                ],
                "subordinates": [],
                "sentiments": null
            }
        }
    ]
}
```

## GCP service account
This service supporting GCP service account using `GOOGLE_APPLICATION_CREDENTIALS` as `JSON` file.

https://cloud.google.com/docs/authentication/production
