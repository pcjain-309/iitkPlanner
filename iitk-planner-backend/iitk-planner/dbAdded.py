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
    host='aws.connect.psdb.cloud',
    user='l5lfsh8l3e8di26wl33e',
    password='pscale_pw_Lakf8JzpZ7FXhlHhnacXRRAmpBGOqqczUj9j85qmiB',
    database='pre-rizzister',
    autocommit = True,
    ssl_mode = "VERIFY_IDENTITY",
    ssl      = {
    "ca": "./cacert.pem",
    }
)

with open('output.json') as json_file:
    data = json.load(json_file)

# Assuming you have a cursor object
cursor = connection.cursor()


# Iterate through each course entry in the JSON data
i = 0
for course in data:

    if(i <=796):
        i = i + 1
        continue
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
    connection.commit()

cursor.close()
connection.close()
