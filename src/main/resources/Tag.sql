DROP TABLE TAG;
create table TAG (
  id INT NOT NULL auto_increment,
  color VARCHAR(6) DEFAULT NULL,
  tag VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (id)
);