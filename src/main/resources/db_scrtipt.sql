USE v27lxlsekmhtr59u;
SET SQL_SAFE_UPDATES = 0;
SET @MAX_QUESTIONS=0;

SELECT * FROM news ORDER BY id DESC LIMIT 3 OFFSET 0;

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
    pageOrder int,
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
    name VARCHAR(30) NOT NULL PRIMARY KEY,
    manufacturer VARCHAR(30) NOT NULL,
    type VARCHAR(70) NOT NULL,
    priceLapy FLOAT4 NOT NULL,
    priceCombi FLOAT4 NOT NULL,
    priceFlanets FLOAT4 NOT NULL,
    photo TEXT,
    mass float not null,
    axisHeight float not null,

    CONSTRAINT engine_manufact FOREIGN KEY (manufacturer) REFERENCES manufacturers(name),
    CONSTRAINT engine_type FOREIGN KEY (type) REFERENCES engineTypes(name)
);

DROP TABLE IF EXISTS characteristics;
CREATE TABLE characteristics (
	id INT PRIMARY KEY AUTO_INCREMENT,
    engineName varchar(30) NOT NULL,                        
    power FLOAT NOT NULL,                         # P (кВт)
    frequency INT NOT NULL,                       # Номинальная частота вращения (об/мин)
    efficiency float NOT NULL,                      # КПД (%)
    cosFi float NOT NULL,                         # cos fi
    electricityNominal115 float,                  # ток номинальный (А) для напряжения 115В
    electricityNominal220 float,                  # ток номинальный (А) для напряжения 220В
    electricityNominal380 float,                  # ток номинальный (А) для напряжения 380В
    electricityRatio float,                       # Iп/Iн
    momentsRatio float,                           # Mп/Мн
    momentsMaxRatio float,                        # Mmax/Мн
    momentsMinRatio float,                        # Mmin/Мн
    momentsMinRatio float,                        # Mmin/Мн
    voltage115 float,                             # Uнс (115)
    voltage220_230 float,                         # Uнс (220/230)
    capacity115 float,                            # C(115), мкф
    capacity220 float,                            # C(220), мкф
    capacity230 float,                            # C(230), мкф  
    criticalSlipping float,                       # %
    CONSTRAINT engine_character FOREIGN KEY (engineName) REFERENCES engines(name) ON DELETE CASCADE
);

DROP TABLE IF EXISTS photos;
CREATE TABLE photos (
    engineName varchar(30) NOT NULL,                        
    photo varchar(150) NOT NULL,
    PRIMARY KEY (engineName, photo),
    CONSTRAINT engine_photo FOREIGN KEY (engineName) REFERENCES engines(name) ON DELETE CASCADE
);

DROP TABLE IF EXISTS news;
CREATE TABLE news (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    date VARCHAR(16) NOT NULL,
    beforePhotoText TEXT,
    photo TEXT,
    afterPhotoText TEXT,
    views int default 0,
    tags varchar(100)
);

SELECT * FROM news;
UPDATE news SET views = views / 2 WHERE id > 0;