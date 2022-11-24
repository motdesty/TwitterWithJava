DROP DATABASE IF EXISTS Twitter;
CREATE DATABASE Twitter;
USE Twitter;
SET foreign_key_checks =0;
CREATE TABLE Users (
  id int PRIMARY KEY AUTO_INCREMENT,
  username varchar(4) unique,
  email varchar(45),
  password varchar(20),
  bio text(200),
  dob datetime,
  creation timestamp,
  profile_img mediumblob,
  bg_img mediumblob
);

CREATE TABLE Post (
  id int PRIMARY KEY AUTO_INCREMENT,
  tweet text(500),
  stamp timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  user_id int ,
  post_img mediumblob
);

CREATE TABLE Retweet (
  id int PRIMARY KEY AUTO_INCREMENT,
  tweet_id int unique,
  user_id int ,
  retweet_stamp timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  retweet_text text(500)
);

CREATE TABLE Timeline (
  id int PRIMARY KEY AUTO_INCREMENT,
  post_id int unique,
  retweet_id int unique
);

CREATE TABLE Bookmark (
  id int PRIMARY KEY AUTO_INCREMENT,
  post_id int unique ,
  user_id int unique
);

CREATE TABLE Comments (
  id int PRIMARY KEY AUTO_INCREMENT,
  post_id int unique,
  user_id int unique,
  comment text(200),
  created_on timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Follow (
--   id int PRIMARY KEY AUTO_INCREMENT,
--   user_id int,
--   following_id int unique
     follower VARCHAR(40) not null,
     followee VARCHAR(40) not null,
     foreign key(follower) references user(userid),
     foreign key(followee) references user(userid),
     primary key(follower, followee)
);

-- ALTER TABLE Users ADD FOREIGN KEY (id) REFERENCES Post(user_id);

-- ALTER TABLE Post ADD FOREIGN KEY (id) REFERENCES Retweet (tweet_id);

-- ALTER TABLE Users ADD FOREIGN KEY (id) REFERENCES Retweet (user_id);

-- ALTER TABLE Post ADD FOREIGN KEY (id) REFERENCES Timeline (post_id);

-- ALTER TABLE Retweet ADD FOREIGN KEY (id) REFERENCES Timeline (retweet_id);

-- ALTER TABLE Post ADD FOREIGN KEY (id) REFERENCES Bookmark (post_id);

-- ALTER TABLE Users ADD FOREIGN KEY (id) REFERENCES Bookmark (user_id);

-- ALTER TABLE Post ADD FOREIGN KEY (id) REFERENCES Comments (post_id);

-- ALTER TABLE Users ADD FOREIGN KEY (id) REFERENCES Comments (user_id);

-- ALTER TABLE Follow ADD FOREIGN KEY (user_id) REFERENCES Users (id);

-- INSERT INTO Users (username, password) VALUES("1234","1234");
select * from Users;
select * from Post;


