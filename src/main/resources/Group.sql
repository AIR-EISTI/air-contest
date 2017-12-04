create table GROUP_EXERCICE (
  id INT NOT NULL auto_increment,
  name VARCHAR(200) DEFAULT NULL,
  description TEXT DEFAULT NULL,
  publicationDate DATETIME DEFAULT CURRENT_TIMESTAMP,
  endDate DATETIME DEFAULT NULL,
  points INT DEFAULT NULL,
  PRIMARY KEY (id)
);
DROP TABLE GROUP_EXERCICE;