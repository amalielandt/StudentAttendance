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

### Endpoints 

1. All endpoints requires that you are logged in as either student or teacher.
2. When logged in a token is generated, and this should be send as part of Request Header {Session-Token:"token"} within the following requests. 
3. Some endpoints are only available for teachers and not for students. 
4. A teacher is the ONLY ONE, who can create student, teacher, subject and lecture.

```{r, engine='bash', count_lines}
localhost:8060/new
```

* Create student[POST]: /student
```
@RequestBody {"name":String, "email":String, "phonenumber":String, "address":String, "city":String, "zipcode":String, "birthdate":String} 
```

* Create teacher[POST]: /teacher
```
@RequestBody {"name":String, "email":String} 
```

* Create subject[POST]: /subject
```
@RequestBody {"name":String, "teacher": {"id":String, "name":String} } 
```

* Create lecture[POST]: /lecture
```
@RequestBody String <i> e.g. 02.12.2021 </i>
```


```{r, engine='bash', count_lines}
localhost:8060/attendance
```

* Login as teacher or student[POST]: ```/login/{session}``` <i>session = STUDENT or TEACHER</i>
```{r, engine='bash', count_lines}
@RequestBody {"email":String, "password":String} 
```

* Retrieve attendance code as student[GET]: ```/{lectureId}```
* Student sign up for attending subject[POST]: ```/{subjectId}/{studentId}```
* Student attends lecture of subject[POST]: ```/{attendanceCode}```

```{r, engine='bash', count_lines}
localhost:8060/get
```

* Retrieve one student[GET]: ```/student/{studentId}```
* Retrieve all students[GET]: ```/students```

* Retrieve one teacher[GET]: ```/teacher/{teacherId}```
* Retrieve all teachers[GET]: ```/teachers```

* Retrieve one subject[GET]: ```/subject/{subjectId}```
* Retrieve all subjects[GET]: ```/subjects```


 
