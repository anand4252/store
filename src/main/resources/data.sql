DROP TABLE IF EXISTS items;

CREATE TABLE items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  description VARCHAR(250) ,
  price DECIMAL DEFAULT 0.0,
  quantity SMALLINT DEFAULT 20,
  price_increased BOOLEAN DEFAULT false,
  last_viewed_time VARCHAR(2500)
);

INSERT INTO items (name, description, price, quantity) VALUES
('Creatine', 'Naked creatine', 10, 100),
('Protein bar', '28gm protein bar', 20, 200 ),
('Whey', 'optimum nutrition 100% Gold standard', 30, 300),
('Casein', 'Slowly Release amino acid', 40, 0);
