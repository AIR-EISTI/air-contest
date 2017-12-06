DROP TABLE IF EXISTS EXERCICE;
create table EXERCICE (
  id INT NOT NULL auto_increment,
  title VARCHAR(200) DEFAULT NULL,
  description TEXT DEFAULT NULL,
  inputFile TEXT DEFAULT NULL,
  outputFile TEXT DEFAULT NULL,
  points INT DEFAULT NULL,
  tournament BOOLEAN DEFAULT 0,
  creatingDate DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);