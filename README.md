### In application.properties you can see I am using MySQL for the problem.See username and password to change for your preferences. DB schema and tables are created automatically on start of project.
### My solution is by creating only one Entity -> EmployeeProject. Validations are made by Annotations on Entity and method in EmployeeProjectService.
### CSV file can be received and read automatically after receiving by sending `POST` request to `/csv`
Postman form-data with parameters `file`(of type file) and `date-pattern`(`yyyy-MM-dd`)
(I chose one pattern for whole csv file because of the problem with patterns `dd/MM/yyyy` and `MM/dd/yyyy`)
Validations are made after file is saved to dir `temp` which you can see in the CSVService
Some Exceptions are caught and added to List so that the file reading and saving to DB can continue but errors with row on which they have occurred are sent on the response.
### In folder `sqlTemp` you can see `joinTable.sql` file with progression on the task by commit history. 
### In `EmployeeProjectRepository` you can see the native query. I used Interface Projection for receiving result of the native SQL query
### Endpoint for receiving the solution of the task in JSON format is `/max-duration` with `GET` request
# Wish you Merry Christmas and Happy New Year