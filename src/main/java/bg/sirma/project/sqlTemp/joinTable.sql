SELECT 
if(empl1.start_date <= empl2.start_date 
AND empl2.start_date <= empl1.end_date 
AND empl1.end_date <= empl2.end_date, 
datediff(ifnull(empl1.end_date, curdate()), empl2.start_date), ''),
empl1.employee_id, empl1.project_id 
FROM s_employees.employee_projects AS empl1
JOIN s_employees.employee_projects AS empl2
ON empl1.project_id = empl2.project_id
GROUP BY empl1.employee_id, empl1.project_id