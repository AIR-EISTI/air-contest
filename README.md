# air-contest
Code contest manager

# /exercice

##### @GET
```
{
    "id": 1,
    "title": "Exercice 1",
    "description": "Description de l'exercice n°1",
    "state": "ACTIVE",
    "points": 10,
    "tournament": false,
    "dateStart": 1511866473000,
    "dateEnd": 1511866473000
}
```


##### @Post
```
{
    "title": "Exercice 1",
    "description": "Description de l'exercice n°1",
    "state": "ACTIVE",
    "points": 10,
    "tournament": false,
    "dateStart": 1511866473705,
    "dateEnd": 1511866473705
}
```

Type de State : 
```
ACTIVE,
WRITING,
PASSIF,
WAITING
```
