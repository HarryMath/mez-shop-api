USE v27lxlsekmhtr59u;
SET SQL_SAFE_UPDATES = 0;

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
    photo VARCHAR(200),
    shortDescription TEXT,
    fullDescription TEXT
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
    type VARCHAR(70) NOT NULL,
    price FLOAT4 NOT NULL,
    photo TEXT,
    mass float,

    CONSTRAINT engine_manufact FOREIGN KEY (manufacturer) REFERENCES manufacturers(name),
    CONSTRAINT engine_type FOREIGN KEY (type) REFERENCES engineTypes(name)
);

DROP TABLE IF EXISTS characteristics;
CREATE TABLE characteristics (
	id INT PRIMARY KEY AUTO_INCREMENT,
    engineId INT NOT NULL,                        
    power FLOAT NOT NULL,                         # P (кВт)
    frequency INT NOT NULL,                       # Номинальная частота вращения (об/мин)
    efficiency INT NOT NULL,                      # КПД (%)
    cosFi float NOT NULL,                         # cos fi
    electricityNominal220 float,                  # ток номинальный (А) для напряжения 220В
    electricityNominal380 float,                  # ток номинальный (А) для напряжения 380В
    electricityRatio float,                       # Iп/Iн
    momentsRatio float,                           # Mп/Мн
    momentsMaxRatio float,                        # Mmax/Мн
    momentsMinRatio float,                        # Mmin/Мн
    CONSTRAINT engine_character FOREIGN KEY (engineId) REFERENCES engines(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS photos;
CREATE TABLE photos (
    engineId INT NOT NULL,                        
    photo varchar(150) NOT NULL,
    PRIMARY KEY (engineId, photo),
    CONSTRAINT engine_photo FOREIGN KEY (engineId) REFERENCES engines(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS news;
CREATE TABLE news (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(30) NOT NULL,
    date VARCHAR(16) NOT NULL,
    beforePhotoText TEXT,
    photo TEXT,
    afterPhotoText TEXT,
    views int default 0,
    tags varchar(100)
);

INSERT INTO manufacturers
(name) values
('ОАО «Могилевлифтмаш»');

INSERT INTO engineTypes
(name, shortDescription, fullDescription)
values
('Стандартные двигатели', 'короткое описание категории', ''),
('Двигатели для нефтегазовой промышленности', 'короткое описание категории', ''),
('Двигатели для объектов использования ядерного топлива', 'короткое описание категории', ''),
('Многоскоростные двигатели', 'короткое описание категории', ''),
('Двигатели с электромагнитным тормозом', 'короткое описание категории', ''),
('Двигатели с повышенным скольжениемм', 'короткое описание категории', ''),
('Тяговые двигатели', 'короткое описание категории', ''),
('Узкоспециализированные двигатели', 'короткое описание категории', ''),
('Встраиваемые двигатели', 'короткое описание категории', ''),
('Бытовые однофазные двигатели типа ДАК', 'короткое описание категории', ''),
('Товары народного потребления', 'короткое описание категории', ''),
('Электроакустические приборы', 'короткое описание категории', '');
UPDATE engineTypes 
SET fullDescription = 'тут будет плоное описание';

select type, count(*) from engines group by type;

insert into engines
(name, type, manufacturer, photo, price, mass)
values
(
	'АИР63А4/2',
	'Многоскоростные двигатели',
	'ОАО «Могилевлифтмаш»',
	'https://www.mez.by/upload/iblock/ad4/ad4f5e8bc3bebbe7396f5fe6301029ae.jpg',
	6599, 3.4
);
insert into engines
(name, type, manufacturer, photo, price, mass)
values
 (
	'4BP90L',
	'Встраиваемые двигатели',
	'ОАО «Могилевлифтмаш»',
	'https://www.emotorsdirect.ca/site/ItemImagesResized/Baldor%20503_motors.jpg?resizeid=2&resizeh=250&resizew=310',
	4234, 2.9
);

SELECT * FROM engineTypes;
SELECT * FROM engines ORDER BY id LIMIT 100 OFFSET 0;
insert into photos (engineId, photo) 
values
(2, 'photoUrl');
SELECT photo FROM photos WHERE engineId = 2;
SELECT * from characteristics;
SELECT max(id) from engines where name = 's/2';