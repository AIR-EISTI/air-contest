DROP TABLE IF EXISTS `JOB_INFO`;
CREATE TABLE `JOB_INFO` (
  id INT NOT NULL AUTO_INCREMENT,
  uuid VARCHAR(36) NOT NULL,
  execTime DATETIME DEFAULT CURRENT_TIMESTAMP,
  msgType VARCHAR(20) NOT NULL,
  msgInfo TEXT,
  PRIMARY KEY (id)
);