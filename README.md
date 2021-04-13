# image-processor
Event Driven Model for Image Processing

#### Description

The processor-service , worker-service and storage-service are springboot microservices. In between, there is a Kafka brokers. When concurrent binary data is being sent to processor service , it would scale to meet the needs and send the job message to the Kafka topic . Based on the number of messages , worker service will start scaling . Storage-service is meant to be a service which can connect to any cloud storage .   ![image](https://user-images.githubusercontent.com/7227769/114632505-bc2d5300-9cb6-11eb-8bdc-b507a45b1ecf.png)

### Design 

![image](https://user-images.githubusercontent.com/7227769/114632461-a750bf80-9cb6-11eb-94b9-f30bb415a521.png)


1.	User sends the binary data in the request body to processor-service. 
2.	Processor-service calls storage-service to upload the binary-data.
3.	Processor-service create a Job id in the Job entity table and update the status to Running.
4.	Processor-service decodes the JWT token , and create a Job Message to send the JWT token info and storage url to Kafka Producer Topic (data_stream).
5.	Worker-service consumes the Job Message from the consumer topic (data_stream)
6.	Worker-service pulls the data from the storage-service and processes the data and uploads the data back to the storage-service
7.	Worker-service sends the message as Success or Failed to the producer topic(status_stream)
8.	Processor-service consumes the message from the consumer topic (status_stream)
9.	Processor-service  retrieves the record for that jobid and update the database entity with respective status.


### Build and Deploy Using Docker 

* 	Git clone repo https://github.com/jokumar/image-processor.git

* 	Go Inside the build-files folder and run the below commands: 
* 	Command to build all the spring boot applications and create images.
  ./build.sh.    
* 	Command to start the docker-compose file so as to start the kafka broker, mysql db and all 3 microservices 
       ./start-server.sh

* 	 To stop all the services . Run : 
     ./stop-server
     
### Test 

Testing the application :



#### Processor-service 

Spring boot microservice which exposes endpoint to take the image binary and validate the incoming message 


#### API 

The API specification can be found in the ptc-file-processor project (openapi3-application.yaml). Copy the yaml to a swagger editor .
There is validation for md5 and the binary data in place 
There is basic validation for the JWT token in place . (format and encoding)



1.	Create a Job 

URL : http://localhost:8080/ptc-file-processor/job/image
MethodType: POST
Header : Authorization eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0.CcTapGbWX0UEMovUwC8kAcWMUxmbOeO0qhsu-wqHQH0

Request : 
'''

{
  "encoding": "base64",
  "md5": "123456",
  "content": “<binarydata>”

}
'''

Response: 
'''

{
    "jobId": "2",
    "payloadLocation": "http://ptc-storage:8081/api/v1/blob/file6544",
    "payloadSize": "254048"
}
'''
2.	Get Job Details 

URL: http://localhost:8080/ptc-file-processor/job/{jobId}

MethodType: GET

Response: 

'''
{
    "id": "2",
    "tenantId": "1",
    "clientId": "1",
    "payloadLocation": "http://ptc-storage:8081/api/v1/blob/file6544",
    "payloadSize": "12345",
    "status": "SUCCESS"
}
'''


3.	Get Job Status :

URL: http://localhost:8080/ptc-file-processor/job/1/status
      MethodType: GET

Response: 
'''
{
    "status": "SUCCESS"
}
'''

