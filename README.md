# StudentAttendance

#### Made by Sofie Amalie Landt, Amanda Juhl Hansen & Benjamin Aizen Kongshaug

To make docker container with volume in project folder run:

```{r, engine='bash', count_lines}
docker run --rm -v "[YOUR_PATH_TO_PROJECT_FOLDER]/StudentAttendance/data:/h2-data" --name h2-studentattendance -d -p 9092:9092 -p 8082:8082 buildo/h2database 
```

Go to localhost:8082 and login to database:
 
<img width="432" alt="Skærmbillede 2021-10-28 kl  13 55 36" src="https://user-images.githubusercontent.com/44894156/139255217-7d1dd14a-103a-45f0-867d-95a345c5761d.png">

To make Redis database within a docker container, run:
```{r, engine='bash', count_lines}
docker run --name studentattendance-redis -p 6379:6379 -v redis-data:/data -d redis:alpine
```

To execute commands within the redis CLI run:
```{r, engine='bash', count_lines}
docker exec -it studentattendance-redis redis-cli
```

### Endpoints available on localhost:8060

/get

* Retrieve one student[GET]: ```/student/{studentId}```
* Retrieve all students[GET]: /students

* Retrieve one teacher[GET]: /teacher/{teacherId}
* Retrieve all teachers[GET]: /teachers

* Retrieve one subject[GET]: /subject/{subjectId}
* Retrieve all subjects[GET]: /subjects

/new


Create student[POST]: /students/
Update student[PUT]: /students/{id}
Convert money [POST]: /students/convert


 
