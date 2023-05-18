CREATE SCHEMA dukes;
CREATE SCHEMA beans;

CREATE TABLE dukes.instrument(
  id      UUID PRIMARY KEY,
  name    VARCHAR(255) NOT NULL,
  type    VARCHAR(255)
);

CREATE TABLE beans.instrument(
  id      UUID PRIMARY KEY,
  name    VARCHAR(255) NOT NULL,
  type    VARCHAR(255)
);
