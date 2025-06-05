#!/bin/sh
curl -X POST -H 'Content-Type: application/json' -d '{"name":"name", "email":"a@example.org"}'  http://localhost:31081/users
curl -X GET http://localhost:31081/users/1
curl -X GET http://localhost:31081/users
curl -X POST -H 'Content-Type: application/json' -d '{"provider":"VK", "type":"basic"}' http://localhost:31081/users/1/subscriptions
curl -X GET http://localhost:31081/users/1/subscriptions

