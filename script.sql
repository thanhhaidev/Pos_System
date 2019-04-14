use headmasterdb;

create table user (
    id int primary key AUTO_INCREMENT,
    username varchar(30) not null,
    password text not null,
    email varchar(40),
    name varchar(50)
);

-- INSERT INTO roles(name_role) VALUES('ROLE_EMPLOYEE');
-- INSERT INTO roles(name_role) VALUES('ROLE_MANAGER');
-- INSERT INTO roles(name_role) VALUES('ROLE_ADMIN');

-- create table roles (
--     id int primary key AUTO_INCREMENT,
--     name_role varchar(10),
--     permission text
-- )


-- create table brand (
--     id int primary key AUTO_INCREMENT,
--     name_brand varchar(50),
--     address_brand varchar(50),
--     phone varchar(12)
-- );