# Guida alle Query SQL
Le query SQL sono comandi utilizzati per interagire con un database relazionale al fine di recuperare, modificare, inserire o eliminare dati. Le query SQL seguono una sintassi specifica e possono essere utilizzate per eseguire una varietà di operazioni sui dati del database.
## Struttura di una Query SQL


<!-- TOC -->
* [CREATE](#create)
* [DROP](#drop)
* [INSERT](#insert)
* [UPDATE](#update)
* [DELETE](#delete)
* [SELECT](#select)
* [FROM](#from)
* [WHERE](#where)
* [GROUP BY](#group-by)
* [HAVING](#having)
* [ORDER BY](#order-by)
* [JOIN](#join)
    * [INNER JOIN](#inner-join)
    * [LEFT JOIN](#left-join)
    * [RIGHT JOIN](#right-join)
    * [FULL OUTER JOIN](#full-outer-join)
* [SUBQUERY](#subquery)
<!-- TOC -->
### TABELLE
Due tabelle utili a cui farà riferimento la guida.

#### 1. students
| student_id | student_name | course_id |
|------------|--------------|-----------|
|     1      |     John     |     101   |
|     2      |     Emily    |     102   |
|     3      |     James    |     101   |
|     4      |     Sarah    |     103   |
|     5      |     Michael  |     102   |

#### 2. courses
| course_id |  course_name  | professor_id |
|-----------|---------------|--------------|
|    101    |   Math        |       1      |
|    102    |   History     |       2      |
|    103    |   Biology     |       3      |
|    104    |   Physics     |       1      |

***

### CREATE
Le query `CREATE` vengono utilizzate per creare nuove tabelle nel database.
```roomsql
CREATE TABLE courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(50),
    professor_id INT
);

-- FOREIGN KEY stabilisce un vincolo di chiave esterna sulla colonna "course_id", che fa riferimento alla colonna "course_id" nella tabella "courses"
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(50),
    course_id INT,
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);
```
***

### DROP
Le query `DROP` vengono utilizzate per cancellare una o più tabelle all'interno del database.

```roomsql
DROP TABLE students;
```
***

### INSERT
Le query `INSERT` vengono utilizzate per aggiungere nuove righe di dati a una tabella del database.

```roomsql
INSERT INTO courses (course_id, course_name, professor_id)
VALUES 
  (101, 'Math', 1),
  (102, 'History', 2),
  (103, 'Biology', 3),
  (104, 'Physics', 1);

INSERT INTO students (student_id, student_name, course_id)
VALUES 
  (1, 'John', 101),
  (2, 'Emily', 102),
  (3, 'James', 101),
  (4, 'Sarah', 103),
  (5, 'Michael', 102);
```
***

### UPDATE
Le query UPDATE vengono utilizzate per modificare i dati in una tabella.

```roomsql
UPDATE students
SET student_name = 'Jack'
WHERE student_id = 1;
```
***

### DELETE
Le query `DELETE` vengono utilizzate per rimuovere una o più righe da una tabella esistente all'interno di un database.

```roomsql
--Questa query eliminerà la riga nella tabella "students" in cui il valore della colonna student_id è uguale a 5--
DELETE FROM students
WHERE student_id = 5;
```
***

### SELECT
La clausola `SELECT` è utilizzata per selezionare dati da una o più tabelle del database.

```roomsql
--Questa query seleziona tutte le colonne dalla tabella "students"--
SELECT * FROM students;
```
| student_id | student_name | course_id |
|------------|--------------|-----------|
|     1      |     John     |     101   |
|     2      |     Emily    |     102   |
|     3      |     James    |     101   |
|     4      |     Sarah    |     103   |
|     5      |     Michael  |     102   |
```roomsql
--Questa query seleziona solo le colonne "course_id", "course_name" dalla tabella "courses"--
SELECT course_id, course_name
FROM demo;
```
| course_id | course_name |
|-----------|-------------|
| 101       | Math        |
| 102       | History     |
| 103       | Biology     |
| 104       | Physics     |

***

### FROM
La clausola `FROM` specifica da quale tabella o tabelle vengono selezionati i dati.

```roomsql
--Questa query seleziona il nome di ogni studente--
SELECT student_name
FROM students 
```
| student_name |
|--------------|
| John         |
| Emily        |
| James        |
| Sarah        |
| Michael      |

***

### WHERE
La clausola `WHERE` viene utilizzata per filtrare i dati basandosi su determinate condizioni.

```roomsql
--Questa query seleziona tutte le colonne dalla tabella "students" dove il valore della colonna "course_id" è uguale a 101--
SELECT *
FROM students
WHERE course_id = 101;
```
| student_id | student_name | course_id |
|------------|--------------|-----------|
| 1          | John         | 101       |
| 3          | James        | 101       |

***
### GROUP BY
La clausola `GROUP BY` è utilizzata per raggruppare i risultati della query in base ai valori di una o più colonne,
è spesso utilizzato in combinazione con le funzioni di aggregazione come SUM, COUNT, AVG per ottenere risultati aggregati.
```roomsql
--Questa query conta quanti studenti ci sono per ciascun corso--
SELECT course_id, COUNT(*) AS student_count
FROM students
GROUP BY course_id;
```
| course_id | student_count |
|-----------|---------------|
| 101       | 2             |
| 102       | 2             |
| 103       | 1             |

***

### HAVING
La clausola `HAVING` viene utilizzata per filtrare i gruppi risultanti di una clausola GROUP BY.
```roomsql
--Questa query seleziona solo i corsi con uno studente--
SELECT course_id, COUNT(*) AS student_count
FROM students
GROUP BY course_id
HAVING COUNT(*) = 1;
```
| course_id | student_count |
|-----------|---------------|
| 103       | 1             |

***

### ORDER BY
La clausola `ORDER BY` è utilizzata per ordinare i risultati in base a una o più colonne. Puoi specificare se l'ordinamento deve essere crescente (ASC) o decrescente (DESC).
```roomsql
--Questa query ordina gli studenti per nome in senso decrescente--
SELECT *
FROM students
ORDER BY student_name DESC;
```
| student_id | student_name | course_id |
|------------|--------------|-----------|
| 4          | Sarah        | 103       |
| 5          | Michael      | 102       |
| 1          | John         | 101       |
| 3          | James        | 101       |
| 2          | Emily        | 102       |


***

### JOIN
I join sono utilizzati per combinare dati da due o più tabelle in base a una relazione specificata. Ci sono diversi tipi di join:

#### INNER JOIN
L'`INNER JOIN` restituisce solo le righe che hanno corrispondenze in entrambe le tabelle coinvolte.
```roomsql
SELECT s.student_id, s.student_name, c.course_name
FROM students s
INNER JOIN courses c ON s.course_id = c.course_id;
```

| student_id | student_name | course_name |
|------------|--------------|-------------|
| 1          | John         | Math        |
| 2          | Emily        | History     |
| 3          | James        | Math        |
| 4          | Sarah        | Biology     |
| 5          | Michael      | History     |

#### LEFT JOIN
Il `LEFT JOIN` restituisce tutte le righe dalla tabella a sinistra (prima tabella menzionata) e le righe corrispondenti dalla tabella a destra (seconda tabella menzionata). Se non ci sono corrispondenze, vengono restituiti valori NULL dalla tabella a destra.
```roomsql
SELECT s.student_id, s.student_name, c.course_name
FROM students s
LEFT JOIN courses c ON s.course_id = c.course_id;
```
| student_id | student_name | course_name |
|------------|--------------|-------------|
| 1          | John         | Math        |
| 2          | Emily        | History     |
| 3          | James        | Math        |
| 4          | Sarah        | Biology     |
| 5          | Michael      | History     |

#### RIGHT JOIN
Il `RIGHT JOIN` restituisce tutte le righe dalla tabella a destra (seconda tabella menzionata) e le righe corrispondenti dalla tabella a sinistra (prima tabella menzionata). Se non ci sono corrispondenze, vengono restituiti valori NULL dalla tabella a sinistra.
```roomsql
SELECT s.student_id, s.student_name, c.course_name
FROM students s
RIGHT JOIN courses c ON s.course_id = c.course_id;
```
| student_id | student_name | course_name |
|------------|--------------|-------------|
| 1          | John         | Math        |
| 2          | Emily        | History     |
| 3          | James        | Math        |
| 4          | Sarah        | Biology     |
| 5          | Michael      | History     |
| _NULL_     | _NULL_       | Physics     |
#### FULL OUTER JOIN
Il `FULL OUTER JOIN` restituisce tutte le righe quando c'è una corrispondenza in una delle tabelle coinvolte. Se non ci sono corrispondenze, vengono restituiti valori NULL per le colonne della tabella che non ha una corrispondenza.
```roomsql
SELECT s.student_id, s.student_name, c.course_name
FROM students s
FULL OUTER JOIN courses c ON s.course_id = c.course_id;
```
| student_id | student_name | course_name |
|------------|--------------|-------------|
| 1          | John         | Math        |
| 2          | Emily        | History     |
| 3          | James        | Math        |
| 4          | Sarah        | Biology     |
| 5          | Michael      | History     |
| _NULL_     | _NULL_       | Physics     |

***

### SUBQUERY
Una `subquery` è una query annidata all'interno di un'altra query. Viene utilizzata per eseguire operazioni complesse o condizioni di filtro più avanzate.
```roomsql
-- Questa subquery restituirà tutti gli studenti che frequentano il corso di Matematica --
-- La subquery interna seleziona l'ID del corso di Matematica dalla tabella dei corsi, 
-- che viene utilizzato come criterio nella clausola WHERE della query esterna per filtrare gli studenti che frequentano questo corso
SELECT *
FROM students
WHERE course_id = (SELECT course_id FROM courses WHERE course_name = 'Math');
```

| student_id | student_name | course_id |
|------------|--------------|-----------|
| 1          | John         | 101       |
| 3          | James        | 101       |

```roomsql
-- Questa subquery restituirà il numero totale di studenti iscritti a tutti i corsi --
-- La clausola IN restituirà righe in cui il valore specificato è presente nella lista di valori.
SELECT COUNT(*) as all_students
FROM students
WHERE course_id IN (SELECT course_id FROM courses);
```

| all_students |
|--------------|
| 5            |

```roomsql
-- Questa subquery restituirà il nome dei corsi che hanno più di due studenti iscritti --
SELECT course_name
FROM courses
WHERE course_id IN (SELECT course_id FROM students GROUP BY course_id HAVING COUNT(*) >= 2);
```

| course_name |
|-------------|
| Math        |
| History     |

***