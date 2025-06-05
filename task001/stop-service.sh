#!/bin/sh

if [ -f service.pid ]; then
	kill $(cat service.pid)
	rm service.pid
fi
docker stop mocksmtp
