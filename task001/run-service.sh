#!/bin/sh

mvn package -D skipTest
# mock smtp
docker run --name mocksmtp --rm -p 38025:25 -p 38081:8000 linagora/mock-smtp-server > targer/smtp.log 2>&1 &
# service using mock smtp connection
${JAVA_HOME}/bin/java -jar target/task001-0.0.1-SNAPSHOT.jar \
	--server.port=38080 \
	--smtp.server=localhost:38025 \
	--processing.uri=direct:send-smtp > target/service.log 2>&1 &
echo $! > service.pid
