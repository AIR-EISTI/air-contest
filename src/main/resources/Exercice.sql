create table EXERCICE (
  id INT NOT NULL auto_increment,
  title VARCHAR(30) default NULL,
  description VARCHAR(500) default NULL,
  state INT(4) NOT NULL,
  points INT DEFAULT NULL,
  tournament BOOLEAN DEFAULT 0,
  PRIMARY KEY (id)
);
DROP TABLE EXERCICE;