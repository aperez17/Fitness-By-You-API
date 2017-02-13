#!/bin/bash

curl -XPUT 'http://localhost:9200/workouts' -d '{
    "settings": {
        "number_of_shards": 1
    },
    "mappings": {
        "workout": {
            "properties": {
                "workoutId" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "name" : {
                    "type": "string",
                    "index": "analyzed",
                    "fields": {
                        "raw" : {
                            "type": "string",
                            "index": "not_analyzed"
                        }
                    }
                },
                "steps": {
                	"properties": {
                		"workout" : { "type": "string" },
                		"iterations": { "type": "integer" }
                	}
                },
                "time": {
                	"type": "integer"
                },
                "imageURL": {
                    "type": "string"
                }
            }
        }
    }
}'
