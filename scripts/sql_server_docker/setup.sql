-- Tables with no dependencies get built first

CREATE TABLE users (
  id          INT NOT NULL AUTO_INCREMENT,
  username    VARCHAR(255) NOT NULL UNIQUE,
  description VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE tags (
  id       INT NOT NULL AUTO_INCREMENT,
  tag      VARCHAR(255) UNIQUE,
  PRIMARY KEY (id)
);

-- Now tables with dependencies can be created

CREATE TABLE userTags (
  id      INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  tag_id  INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (user_id, tag_id),
  FOREIGN KEY (user_id)
  REFERENCES users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (tag_id)
  REFERENCES tags (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
