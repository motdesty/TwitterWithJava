DROP DATABASE IF EXISTS Twitter;
CREATE DATABASE Twitter;
USE Twitter;
-- SET foreign_key_checks =0;

CREATE TABLE Users (
  id int AUTO_INCREMENT PRIMARY KEY,
  username varchar(20) unique,
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
  post text(500),
  stamp timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  author_id int ,
  board_owner_id int,
  post_img mediumblob
);

CREATE TABLE Retweet (
  id int PRIMARY KEY AUTO_INCREMENT,
  post_id int,
  author_id int,
  stamp timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  post text(500)
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
  post_id int,
  author_id int,
  comment text(200),
  created_on timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Follow (
     follower varchar(20),
     followee VARCHAR(20),
     foreign key(follower) references Users(username),
     foreign key(followee) references Users(username),
     primary key(follower, followee)
);

ALTER TABLE Post ADD FOREIGN KEY (author_id) REFERENCES Users(id);

ALTER TABLE Post ADD FOREIGN KEY (board_owner_id) REFERENCES Users(id);

ALTER TABLE Retweet ADD FOREIGN KEY (post_id) REFERENCES Post (id);

ALTER TABLE Retweet ADD FOREIGN KEY (author_id) REFERENCES Users (id);

ALTER TABLE Timeline ADD FOREIGN KEY (post_id) REFERENCES Post (id);

ALTER TABLE Timeline ADD FOREIGN KEY (retweet_id) REFERENCES Retweet (id);

ALTER TABLE Bookmark ADD FOREIGN KEY (post_id) REFERENCES Post (id);

ALTER TABLE Bookmark ADD FOREIGN KEY (user_id) REFERENCES Users (id);

ALTER TABLE Comments ADD FOREIGN KEY (author_id) REFERENCES Post (id);

ALTER TABLE Comments ADD FOREIGN KEY (author_id) REFERENCES Users (id);
