CREATE TABLE User (
	id INT AUTO_INCREMENT PRIMARY KEY,
	login VARCHAR(32) UNIQUE NOT NULL,
	md5_password CHAR(32) UNIQUE NOT NULL,
	admin BOOLEAN DEFAULT FALSE NOT NULL,
	UNIQUE INDEX User_login (login)
);

CREATE TABLE Item (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(256) NOT NULL,
	description TEXT,
	img_url TEXT
);

CREATE TABLE Transaction (
	id INT AUTO_INCREMENT,
	user_id INT REFERENCES User(id),
	ts TIMESTAMP NOT NULL,
	PRIMARY KEY(id, user_id)
);

CREATE TABLE Transaction_Item (
	t_id INT NOT NULL REFERENCES Transaction(id),
	user_id INT NOT NULL REFERENCES User(id),
	item_id INT NOT NULL REFERENCES Item(id),
	quantity INT NOT NULL
);

INSERT INTO User (login, md5_password, admin) VALUES ('admin', MD5('admin'), TRUE);
