DROP TABLE IF EXISTS ITEM_LAST_VIEWED_TIMES;
DROP TABLE IF EXISTS ITEMS;
DROP TABLE IF EXISTS USER_DETAILS;

CREATE TABLE ITEMS (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  description VARCHAR(250) ,
  price DECIMAL DEFAULT 0.0,
  quantity SMALLINT DEFAULT 20,
  price_increased BOOLEAN DEFAULT false,
  last_viewed_time VARCHAR(2500)
);
CREATE TABLE ITEM_LAST_VIEWED_TIMES (
  ITEM_ID INT,
  last_Viewed_Times TIMESTAMP
);

CREATE TABLE USER_DETAILS (
  username VARCHAR(250) PRIMARY KEY,
  password VARCHAR(250) NOT NULL
);

INSERT INTO ITEMS (name, description, price, quantity) VALUES
('Creatine', 'Naked creatine', 10, 100),
('Protein bar', '28gm protein bar', 20, 200 ),
('Whey', 'optimum nutrition 100% Gold standard', 30, 300),
('Casein', 'Slowly Release amino acid', 40, 0);

INSERT INTO USER_DETAILS (username, password) VALUES
('anand', 'anand'),
('aaron', 'aaron'),
('aidan', 'aidan');
