#/bin/sh

#  delete existing emails
curl -X DELETE http://127.0.0.1:38081/smtpMails
# create a vacancy
curl -H 'Content-Type: application/json' -X PUT \
	-d '{"name":"name","description":"description","position":"position","sallary":123,"required_experience":"not required", "city":"city"}' \
	http://127.0.0.1:38080/vacancy
# subscribe
curl -H 'Content-Type: application/json' -X PUT \
	-d '{"email":"a@example.org","fullname":"Full Name","position":"position", "city":"city"}' \
	http://127.0.0.1:38080/subscriber
#trigger a notification
curl -X POST http://127.0.0.1:38080/notify

# check emails
sleep 2
curl --GET http://127.0.0.1:38081/smtpMails
