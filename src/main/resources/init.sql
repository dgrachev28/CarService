DROP TABLE service_queue;
DROP TABLE client;
DROP TABLE workshop_master;
DROP TABLE service;
DROP TABLE master;
DROP TABLE workshop;



CREATE TABLE master(id INT NOT NULL, name VARCHAR(30) NOT NULL, busy TINYINT(1) NOT NULL, PRIMARY KEY (id));
CREATE TABLE workshop(id INT NOT NULL, name VARCHAR(30) NOT NULL, PRIMARY KEY (id));
CREATE TABLE workshop_master(masters_id INT NOT NULL, workshop_id INT NOT NULL, FOREIGN KEY (masters_id) REFERENCES master(id), FOREIGN KEY (workshop_id) REFERENCES workshop(id));
CREATE TABLE service(id INT NOT NULL, name VARCHAR(30) NOT NULL, cost INT NOT NULL, average_time DOUBLE NOT NULL, workshop_id INT NOT NULL, PRIMARY KEY (id), FOREIGN KEY (workshop_id) REFERENCES workshop(id));
CREATE TABLE client(car_id VARCHAR(30) NOT NULL, queue_start_date DATE NOT NULL, busy TINYINT(1) NOT NULL, PRIMARY KEY (car_id));
CREATE TABLE service_queue(id INT NOT NULL, car_id VARCHAR(30) NOT NULL, workshop_id INT NOT NULL, PRIMARY KEY (id), FOREIGN KEY (workshop_id) REFERENCES workshop(id), FOREIGN KEY (car_id) REFERENCES client(car_id));



INSERT INTO master SET id = 1, name = "Денис", busy = 0;
INSERT INTO master SET id = 2, name = "Max", busy = 1;
INSERT INTO master SET id = 3, name = "Vasya", busy = 0;

INSERT INTO workshop SET id = 1, name = "Тех. осмотр";
INSERT INTO workshop SET id = 2, name = "Мойка";

INSERT INTO workshop_master SET masters_id = 1, workshop_id = 1;
INSERT INTO workshop_master SET masters_id = 2, workshop_id = 1;
INSERT INTO workshop_master SET masters_id = 3, workshop_id = 2;

INSERT INTO service SET id = 1, name = "Осмотр двигателя", cost = 1000, average_time = 50, workshop_id = 1;
INSERT INTO service SET id = 2, name = "Мойка салона", cost = 500, average_time = 30, workshop_id = 2;

INSERT INTO client SET car_id = "e234ac", queue_start_date = "2016-03-25", busy = 0;

INSERT INTO service_queue SET id = 1, car_id = "e234ac", workshop_id = 1;
