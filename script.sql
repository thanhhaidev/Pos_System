use headmasterdb;

create table account(
    id int primary key auto_increment,
    username varchar(20) NOT NULL,
    password TEXT NOT NULL,
    enabled BOOLEAN DEFAULT true NOT NULL
)