CREATE TABLE customers 
(
   username VARCHAR(50) PRIMARY KEY,
   password VARCHAR(100) NOT NULL,
   enabled boolean NOT NULL, 
   email VARCHAR(30),
   contact VARCHAR(10),
   gender CHAR(1)
);

create table roles (
    username VARCHAR(50) not null,
    role VARCHAR(50) not null,
    constraint fk_authorities_customers foreign key(username) references customers(username)
);
create unique index ix_auth_username on roles (username,role);