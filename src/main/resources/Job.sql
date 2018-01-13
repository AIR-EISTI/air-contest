DROP TABLE IF EXISTS `JOB`;
CREATE TABLE `JOB` (
  uuid VARCHAR(36) NOT NULL,
  user INT NOT NULL,
  exercice_id INT NOT NULL,
  PRIMARY KEY (uuid),
  FOREIGN KEY (exercice_id) REFERENCES EXERCICE (id)
    ON DELETE CASCADE
);