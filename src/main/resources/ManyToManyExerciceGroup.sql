DROP TABLE IF EXISTS EXERCICE_GROUP;
CREATE TABLE EXERCICE_GROUP (
  exercice_id INT NOT NULL,
  group_id INT NOT NULL,
  PRIMARY KEY (exercice_id, group_id),
  FOREIGN KEY (exercice_id) REFERENCES EXERCICE (id)
    ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES `GROUP` (id)
    ON DELETE CASCADE
);
