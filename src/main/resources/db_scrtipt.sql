USE v27lxlsekmhtr59u;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    googleId VARCHAR(100) UNIQUE,
	mail VARCHAR(50) NOT NULL,
    name VARCHAR(30) NOT NULL,
    phone VARCHAR(17),
    photo VARCHAR(200),
    password VARCHAR(256),
    isAdmin BOOLEAN
);

DROP TABLE IF EXISTS engineTypes;
CREATE TABLE engineTypes (
	name VARCHAR(70) NOT NULL PRIMARY KEY,
    description TEXT
);

DROP TABLE IF EXISTS manufacturers;
CREATE TABLE manufacturers (
	name VARCHAR(30) PRIMARY KEY
);

DROP TABLE IF EXISTS engines;
CREATE TABLE engines (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    
    name VARCHAR(30) NOT NULL,
    manufacturer VARCHAR(30) NOT NULL,
    type VARCHAR(30) NOT NULL,
    price FLOAT4 NOT NULL,
    
    photo TEXT,
    
    CONSTRAINT engine_manufact FOREIGN KEY (manufacturer) REFERENCES manufacturers(name),
    CONSTRAINT engine_type FOREIGN KEY (type) REFERENCES engineTypes(name)
);

DROP TABLE IF EXISTS characteristics;
CREATE TABLE characteristics (
    engineId int NOT NULL,
    name VARCHAR(20) NOT NULL,
    value VARCHAR(20) NOT NULL,
    CONSTRAINT engine_character FOREIGN KEY (engineId) REFERENCES engines(id)
);

INSERT INTO manufacturers
(name) values
('ОАО «Могилевлифтмаш»');

INSERT INTO engineTypes
(name, description)
values
('Стандартные двигатели', ''),
('Двигатели для нефтегазовой промышленности', ''),
('Двигатели для объектов использования ядерного топлива', ''),
('Многоскоростные двигатели', ''),
('Двигатели с электромагнитным тормозом', ''),
('Двигатели с повышенным скольжениемм', ''),
('Тяговые двигатели', ''),
('Узкоспециализированные двигатели', ''),
('Встраиваемые двигатели', ''),
('Бытовые однофазные двигатели типа ДАК', ''),
('Товары народного потребления', ''),
('Электроакустические приборы', '');

select * from engines;

insert into engines
(name, type, manufacturer, photo, price)
values
(
	'АИР63А4/2',
	'Многоскоростные двигатели',
	'ОАО «Могилевлифтмаш»',
	'https://www.mez.by/upload/iblock/ad4/ad4f5e8bc3bebbe7396f5fe6301029ae.jpg',
	6599
);
insert into engines
(name, type, manufacturer, photo, price)
values
 (
	'4BP90L',
	'Встраиваемые двигатели',
	'ОАО «Могилевлифтмаш»',
	'https://www.emotorsdirect.ca/site/ItemImagesResized/Baldor%20503_motors.jpg?resizeid=2&resizeh=250&resizew=310',
	4234
);
insert into characteristics 
(engineId, name, value)
values 
(8, 'вес', '5.1кг'),
(8, 'частота вращения', '2720 об/мин'),
(8, 'мощность', '0.265 кВт'),
(8, 'кпд', '73%'),

(12, 'вес', '2.9кг'),
(12, 'частота вращения', '1930 об/мин'),
(12, 'мощность', '0.311 кВт'),
(12, 'кпд', '78%');

