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
#### request to entity
```
curl --location --request POST 'localhost:8081/report/v1/sentiments' \
--header 'x-tenant: 98c2e7dc-b2c7-49d9-8cbb-93501a00220f' \
--header 'Content-Type: text/plain' \
--data-raw '{
    "author": "0fa7cc74-2808-4d61-ae6b-63acbcb38474",
    "subject": {
    	"id": "4ba7cc74-2808-4d61-ae6b-63acbcb38474",
    	"type": "Car"
    },
    "text": "Minivans have always had the stigma of the soccer mom image even though they’re incredibly practical"
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
            "totalFeedback": 1,
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

## Creating entity
### request
```
curl --location --request POST 'localhost:8081/report/v1/entity' \
--header 'x-tenant: 98c2e7dc-b2c7-49d9-8cbb-93501a00220f' \
--header 'Content-Type: text/plain' \
--data-raw '{
	"id": "4ba7cc74-2808-4d61-ae6b-63acbcb38474",
	"type": "Car",
	"name": "Mini Van",
	"ownerIds": ["5d3444c4-3798-4619-b644-9b2da1dd6bfa"]
}'
```

## Fetching sentiment data
### request
```
curl --location --request GET 'localhost:8081/report/v1/sentiments/0fa7cc74-2808-4d61-ae6b-63acbcb38474/to/5d3444c4-3798-4619-b644-9b2da1dd6bfa' \
--header 'x-tenant: 98c2e7dc-b2c7-49d9-8cbb-93501a00220f'
```
optional requestParam `entityType` to fetch sentiment to entity.
### response
```
[    
    {
        "id": "ceaea51f-c1c9-4665-b0e1-f66a2ac99a7e",
        "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f",
        "text": "Well, am I wrong? They say it's not different but it's a different sensation. When you use, something, to block I think everyone knows what I'm talking about. It's not necessarily different for the woman. But it is different for me",
        "score": -0.2,
        "magnitude": 1.3
    },
    {
        "id": "085785bd-b19c-43a9-8cb3-b5e3e4d6f18e",
        "businessId": "98c2e7dc-b2c7-49d9-8cbb-93501a00220f",
        "text": "I’m his boss actually. sadsadasds And I treat him well. I’m taking him out to lunch cause I can afford it and he can have whatever he wants.",
        "score": 0.5,
        "magnitude": 1.4
    }
]
```

## GCP service account
This service supporting GCP service account using `GOOGLE_APPLICATION_CREDENTIALS` as `JSON` file.

https://cloud.google.com/docs/authentication/production
