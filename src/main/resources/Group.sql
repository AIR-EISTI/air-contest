DROP TABLE IF EXISTS EXERCICE_GROUP;
DROP TABLE IF EXISTS `GROUP`;
create table `GROUP` (
  id INT NOT NULL auto_increment,
  name VARCHAR(200) DEFAULT NULL,
  description TEXT DEFAULT NULL,
  publicationDate DATETIME DEFAULT CURRENT_TIMESTAMP,
  endDate DATETIME DEFAULT NULL,
  points INT DEFAULT NULL,
  imageUrl TEXT NOT NULL,
  PRIMARY KEY (id)
);
