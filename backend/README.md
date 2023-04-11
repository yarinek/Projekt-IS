# Run
* Przez IntelliJ:
 
Wybrac JDK17.
W zakładce `maven` wybrac `projektkrypto` > `Lifecycle` > `compile`.
W `compose.yml` kliknac przycisk przy `services`.
Nastepnie w pliku `ProjektKryptoApplication` uruchomic program


* Przez cmd:

Zmienna srodowiskowa `JAVA_HOME` musi wskazywac na JDK 17:
```
docker-compose up -d
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

## REST
### Endpoints:
* http://localhost:8080/login
```
curl --location --request POST 'http://localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "admin",
    "password": "Admin12@"
}'
```
W responsie znajduje sie `Authorization` header np:
```
Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY3OTQyNDA0N30._XtS0MXr45U2nr0zY1mnO3Pja45r7n8WaoDgGOxn1M6I7NfQCr5-EDkJ-nyEDs9h2MKYW1sDTfZJZinq5oz8pg
```
Statusy: 200, 401

* http://localhost:8080/api/rest/db/export

Pobieranie pliku z .sql dumpem. Wymaga ROLE_ADMIN

```
curl --location --request GET 'http://localhost:8080/api/rest/db/export' \
--header 'Authorization: <TOKEN>'
```

* http://localhost:8080/api/rest/db/import

Wymaga ROLE_ADMIN

```
curl --location --request POST 'http://localhost:8080/api/rest/db/import' \
--header 'Authorization: <TOKEN>' \
--form 'file=@"/C:/Users/MateuszSęk/Downloads/db_dump2"'
```

* http://localhost:8080/api/rest/events/import
```
curl --location --request POST 'http://localhost:8080/api/rest/events/import' \
--header 'Authorization: <TOKEN>' \
--form 'file=@"/C:/Users/MateuszSęk/Downloads/test.json"'
```

* http://localhost:8080/api/rest/events/export
```
curl --location --request GET 'http://localhost:8080/api/rest/events/export' \
--header 'Authorization: <TOKEN>'
```
Response to plik .json

* http://localhost:8080/api/rest/register
```
curl --location --request POST 'http://localhost:8080/api/rest/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "user1",
    "password": "User12",
    "matchingPassword": "User123@",
    "email": "aa@mail"
}'
```
Sukces: 201

w przypadku bledu message = delimiter `.\n`
```
{
    "status": 400,
    "message": "Passwords do not match.\nPassword is not strong enough"
}
```

## SOAP
WSDL: http://localhost:8080/ws/events.wsdl
### Endpoints:
Authorization header nie potrzebny dla SOAPowych endpointow
* http://localhost:8080/ws
Musi byc podane `endDate` lub `startDate`, reszta opcjonalnie

#### Example:

request
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://localhost/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:getEventsRequest>
         <soap:endDate>2022-05-20</soap:endDate>
         <soap:category>ECONOMY</soap:category>
      </soap:getEventsRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
response
```
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:getEventsResponse xmlns:ns2="http://localhost/soap">
         <ns2:events>
            <ns2:endDate>2021-04-01</ns2:endDate>
            <ns2:startDate>2021-06-01</ns2:startDate>
            <ns2:url>https://www.parkiet.com/waluty/art36287341-luna-historia-spadajacej-gwiazdy-kryptowalut</ns2:url>
            <ns2:title>someTitle2</ns2:title>
            <ns2:category>ECONOMY</ns2:category>
            <ns2:coin>LUNC</ns2:coin>
         </ns2:events>
         <ns2:events>
            <ns2:endDate>2022-01-01</ns2:endDate>
            <ns2:startDate>2023-01-01</ns2:startDate>
            <ns2:url>https://pl.investing.com/economic-calendar/interest-rate-decision-168</ns2:url>
            <ns2:title>someTitle3</ns2:title>
            <ns2:category>ECONOMY</ns2:category>
            <ns2:coin>BTC</ns2:coin>
         </ns2:events>
      </ns2:getEventsResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```