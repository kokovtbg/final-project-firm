SELECT
max(
CASE
WHEN
(empl1.start_date <= empl2.start_date
AND empl2.start_date <= ifnull(empl1.end_date, curdate())
AND ifnull(empl1.end_date, curdate()) <= ifnull(empl2.end_date, curdate()))
THEN
datediff(ifnull(empl1.end_date, curdate()), empl2.start_date)

WHEN
(empl2.start_date <= empl1.start_date
AND empl1.start_date <= ifnull(empl2.end_date, curdate())
AND ifnull(empl2.end_date, curdate()) <= ifnull(empl1.end_date, curdate()))
THEN
datediff(ifnull(empl2.end_date, curdate()), empl1.start_date)

WHEN
(empl1.start_date <= empl2.start_date
AND empl2.start_date <= ifnull(empl2.end_date, curdate())
AND ifnull(empl2.end_date, curdate()) <= ifnull(empl1.end_date, curdate()))
THEN
datediff(ifnull(empl2.end_date, curdate()), empl2.start_date)

WHEN
(empl2.start_date <= empl1.start_date
AND empl1.start_date <= ifnull(empl1.end_date, curdate())
AND ifnull(empl1.end_date, curdate()) <= ifnull(empl2.end_date, curdate()))
THEN
datediff(ifnull(empl1.end_date, curdate()), empl1.start_date)
END) AS diff,

empl1.employee_id AS first_employee,
empl2.employee_id AS second_employee,
empl1.project_id

FROM s_employees.employee_projects AS empl1
JOIN s_employees.employee_projects AS empl2
ON empl1.project_id = empl2.project_id
AND empl1.employee_id != empl2.employee_id
