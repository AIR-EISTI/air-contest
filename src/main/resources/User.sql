DROP TABLE IF EXISTS TOKEN;
DROP TABLE IF EXISTS JOB;
DROP TABLE IF EXISTS RESULT;
DROP TABLE IF EXISTS USER;
CREATE TABLE USER (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(200) NOT NULL,
  firstname VARCHAR(200) DEFAULT NULL,
  surname VARCHAR(200) DEFAULT NULL,

  role VARCHAR(50) DEFAULT '',

  PRIMARY KEY (id)
);
