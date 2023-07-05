
# CVQS Back-End Project

The CVQS Backend project allows to record defects that occur during the vehicle manufacturing process. It is developed using microservice architecture.




## DATA MODELS

VEHICLE - DEFECT - VEHICLE DEFECT


![VEHICLE - DEFECT - VEHICLE DEFECT](https://i.imgur.com/htWEPzb.png)

USER - ROLE


![USER - ROLE](https://i.imgur.com/qb60P6a.png)


MICROSERVICE ARCHITECTURE


![MICROSERVICE](https://i.imgur.com/TR2lEpl.png)
## API

Requests and Examples are shared in the Postman Collection.

https://www.postman.com/material-candidate-82552888/workspace/my-workspace/collection/21648995-29c11bc8-7379-49b4-a056-f0647827a649?action=share&creator=21648995
## LOG

- With AOP, the logs of the inputs and outputs of the functions are kept by using "annotations".

 ![LOG](https://i.imgur.com/P4uA3GV.png)

 
 Sample Output :

 ![OUTPUT](https://i.imgur.com/vmBGiBO.png)




  
## MICROSERVICE ARCHITECTURE


5 Containers are started while the project is being created. These are:

- postgresql (Database)
- auth (authentication and authorization)
- management (user and role definitions)
- cvqs (Vehicle - Defect - Vehicle Defect operations)
- eurekaserver (communication of containers with each other)
  
## IMAGE PROCESSING

- Pictures are taken from the user while the Defect is being registered or updated.

- Pictures are taken from the user as strings in base64 format. The received image is converted into a byte array and saved in the database. When the user requests the picture back, the picture is sent to the user by doing the reverse of these operations.

- While the vehicle is recording the Defect, the coordinates of the parts on the picture with the error are taken from the user. Received coordinates are saved (the operation on the image is not saved). When the user requests the picture with the errors marked, the positions in the picture are instantly marked and the picture is returned to the user.

![IMAGE PROCESSING](https://i.imgur.com/eIepi2B.png)
  
## JWT

JWT is returned to logged in or registered users. This token contains 3 parts:

- User name
- Creation Date
- End Date

When the user sends a request to the Services with JWT, the duration of the JWT is checked. If the duration is still valid, the user's roles are retrieved from the database and thus role-based access is made.
## ROLE-BASED ACCESS

First, the role requirements of the address that the user wants to access are determined. If the address that the user wants to access requires any role authorization, JWT is sent to the auth container and the user's information is requested. The authorization of the brought user information is checked and rejected or accepted.

Example role authorization requirements:

![ROLE AUTHORIZATION](https://i.imgur.com/Rcf34W1.png)

## SORTING, FILTERING AND PAGEING

During the listing process, the number of pages requested by the user, how many values ​​each page will contain, the sorting type (DESC, ASC), and the keywords to be searched are taken.

Example Request:

GET http://localhost:8080/api/v1/listing/getvehicles?modelNo=&vehicleCode=&page=0&pageSize=4&sortType=DESC

RESPONSE
 ```json
[
    {
        "id": 4,
        "modelNo": "i10",
        "vehicleCode": "i10-z23sz22-y",
        "vehicleDefectDtos": []
    },
    {
        "id": 3,
        "modelNo": "i20",
        "vehicleCode": "i20-z23sd2-y",
        "vehicleDefectDtos": []
    },
    {
        "id": 2,
        "modelNo": "A6",
        "vehicleCode": "A6-XTR523XZ",
        "vehicleDefectDtos": []
    },
    {
        "id": 1,
        "modelNo": "A4",
        "vehicleCode": "Z23S-1PK334",
        "vehicleDefectDtos": [
            {
                "id": 1,
                "defect": {
                    "id": 1,
                    "imageDto": {
                        "id": 1,
                        "name": "Araç kapı çizik",
                        "data": "",
                        "type": "jpg"
                    },
                    "name": "Araç kapı çizik"
                },
                "defectLocations": [
                    {
                        "id": 1,
                        "coordX": "50",
                        "coordY": "50",
                        "deleted": false
                    },
                    {
                        "id": 2,
                        "coordX": "450",
                        "coordY": "300",
                        "deleted": false
                    },
                    {
                        "id": 3,
                        "coordX": "230",
                        "coordY": "210",
                        "deleted": false
                    }
                ]
            }
        ]
    }
]
```
  
## DOCKER-COMPOSE VE DOCKERFILE

DOCKERFILE :

![DOCKERFILE](https://i.imgur.com/iZ3dXvz.png)

DOCKER-COMPOSE :

![DOCKER-COMPOSE](https://i.imgur.com/7wi7TP2.png)

### 🔨 Run the App

### Docker Compose

1. Download your project from this link `[https://github.com/Rapter1990/cqrs-example](https://github.com/ErenBicakci/cvqsfinal)`
2. Go to the management folder :  `cd management`
3. Run this command : `mvn clean install`
4. Go back to previous folder : `cd ..`
5. Go to the main folder :  `cd main`
6. Run this command : `mvn clean install`
7. Go back to previous folder : `cd ..`
8. Go to the authservice folder :  `cd authservice`
9. Run this command : `mvn clean install`
10. Go back to previous folder : `cd ..`
11. Run docker-compose though this command `docker-compose up`
12. Send any request via postman address
13. Show postgresql tables
```
    -> docker exec -it postgres psql -U cvqstestdb
    -> \c cvqstestdb
    -> \dt
    Sample output
    Schema |      Name       | Type  |  Owner
    --------+-----------------+-------+----------
    public | defect          | table | postgres
    public | defect_location | table | postgres
    public | image           | table | postgres
    public | role_table      | table | postgres
    public | user_role       | table | postgres
    public | user_table      | table | postgres
    public | vehicle         | table | postgres
    public | vehicle_defect  | table | postgres
```
    
