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
INSERT INTO master SET id = 2, name = "Максим", busy = 0;
INSERT INTO master SET id = 3, name = "Мария", busy = 0;

INSERT INTO master SET id = 4, name = "Андрей", busy = 0;
INSERT INTO master SET id = 5, name = "Сергей", busy = 0;
INSERT INTO master SET id = 6, name = "Наталья", busy = 0;
INSERT INTO master SET id = 7, name = "Юрий", busy = 0;
INSERT INTO master SET id = 8, name = "Алексей", busy = 0;

INSERT INTO master SET id = 9, name = "Федор", busy = 0;
INSERT INTO master SET id = 10, name = "Дмитрий", busy = 0;
INSERT INTO master SET id = 11, name = "Кирилл", busy = 0;
INSERT INTO master SET id = 12, name = "Евгений", busy = 0;

INSERT INTO master SET id = 13, name = "Анастасия", busy = 0;
INSERT INTO master SET id = 14, name = "Иван", busy = 0;
INSERT INTO master SET id = 15, name = "Виктор", busy = 0;
INSERT INTO master SET id = 16, name = "Игорь", busy = 0;
INSERT INTO master SET id = 17, name = "Антон", busy = 0;



INSERT INTO workshop SET id = 1, name = "Техосмотр";
INSERT INTO workshop SET id = 2, name = "Кузовной ремонт";
INSERT INTO workshop SET id = 3, name = "Шиномонтаж";
INSERT INTO workshop SET id = 4, name = "Ремонт двигателя";



INSERT INTO workshop_master SET masters_id = 1, workshop_id = 1;
INSERT INTO workshop_master SET masters_id = 2, workshop_id = 1;
INSERT INTO workshop_master SET masters_id = 3, workshop_id = 1;

INSERT INTO workshop_master SET masters_id = 4, workshop_id = 2;
INSERT INTO workshop_master SET masters_id = 5, workshop_id = 2;
INSERT INTO workshop_master SET masters_id = 6, workshop_id = 2;
INSERT INTO workshop_master SET masters_id = 7, workshop_id = 2;
INSERT INTO workshop_master SET masters_id = 8, workshop_id = 2;

INSERT INTO workshop_master SET masters_id = 9, workshop_id = 3;
INSERT INTO workshop_master SET masters_id = 10, workshop_id = 3;
INSERT INTO workshop_master SET masters_id = 11, workshop_id = 3;
INSERT INTO workshop_master SET masters_id = 12, workshop_id = 3;

INSERT INTO workshop_master SET masters_id = 13, workshop_id = 4;
INSERT INTO workshop_master SET masters_id = 14, workshop_id = 4;
INSERT INTO workshop_master SET masters_id = 15, workshop_id = 4;
INSERT INTO workshop_master SET masters_id = 16, workshop_id = 4;
INSERT INTO workshop_master SET masters_id = 17, workshop_id = 4;



INSERT INTO service SET id = 1, name = "техосмотр", cost = 1000, average_time = 50, workshop_id = 1;
INSERT INTO service SET id = 2, name = "повторный техосмотр", cost = 700, average_time = 30, workshop_id = 1;

INSERT INTO service SET id = 3, name = "восстановление кузова", cost = 10000, average_time = 360, workshop_id = 2;
INSERT INTO service SET id = 4, name = "покраска", cost = 5000, average_time = 120, workshop_id = 2;
INSERT INTO service SET id = 5, name = "полировка кузова", cost = 1000, average_time = 60, workshop_id = 2;
INSERT INTO service SET id = 6, name = "локальный ремонт кузова", cost = 2000, average_time = 120, workshop_id = 2;

INSERT INTO service SET id = 7, name = "монтаж 1 колеса", cost = 1000, average_time = 30, workshop_id = 3;
INSERT INTO service SET id = 8, name = "монтаж 2 колес", cost = 1800, average_time = 60, workshop_id = 3;
INSERT INTO service SET id = 9, name = "монтаж 4 колес", cost = 3000, average_time = 120, workshop_id = 3;

INSERT INTO service SET id = 10, name = "замена опоры двигателя", cost = 2000, average_time = 60, workshop_id = 4;
INSERT INTO service SET id = 11, name = "замена масла", cost = 600, average_time = 20, workshop_id = 4;
INSERT INTO service SET id = 12, name = "регулировка клапанов", cost = 500, average_time = 20, workshop_id = 4;
INSERT INTO service SET id = 13, name = "капитальный ремонт двигателя", cost = 4000, average_time = 180, workshop_id = 4;



INSERT INTO client SET car_id = "e234ac", queue_start_date = "2016-03-25", busy = 0;

INSERT INTO service_queue SET id = 1, car_id = "e234ac", workshop_id = 1;
