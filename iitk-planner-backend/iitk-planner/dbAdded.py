import json
import re

with open('output.json') as json_file:
    data = json.load(json_file)

# import mysql.connector
import MySQLdb
import json

# Assuming you have already established a connection to the database
# Replace 'your_database_info' with your actual database connection details
connection = MySQLdb.connect(
    host='localhost',
    user='root',
    password='pcj@34309',
    database='iitk_planner'
)

with open('output.json') as json_file:
    data = json.load(json_file)

# Assuming you have a cursor object
cursor = connection.cursor()

# Iterate through each course entry in the JSON data
for course in data:
    course_id = course['id']
    name = course['name']
    course_code = course['courseCode']
    professor = course['professor']
    credits = course['credits']
    
    credits_match = re.search(r'\((\d+)\)', credits)
    print(credits_match)

    if credits_match:
        credits = int(credits_match.group(1))
    # Insert course details into the "courses" table
    sql_insert_course = "INSERT INTO course (id, name, course_code, professor, credits) VALUES (%s, %s, %s, %s, %s)"
    cursor.execute(sql_insert_course, (course_id, name, course_code, professor, credits))

    # Insert timing details into the "timings" table (assuming you have a separate table for timings)
    for timing in course['timings']:
        start_time = timing['startTime']
        end_time = timing['endTime']
        day = timing['day']

        sql_insert_timing = "INSERT INTO timings (course_id, start_time, end_time, day) VALUES (%s, %s, %s, %s)"
        cursor.execute(sql_insert_timing, (course_id, start_time, end_time, day))

# Commit the changes and close the connection
connection.commit()
cursor.close()
connection.close()
