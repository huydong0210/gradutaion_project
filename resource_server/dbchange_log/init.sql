create table parents
(
    id                 bigint auto_increment
        primary key,
    name nvarchar(50),
    phone_number varchar(20),
    age int,
    relation varchar(50),
    user_id long
);
create table subject(
                        id bigint auto_increment primary key ,
                        name nvarchar(50),
                        grade double,
                        user_id long
);
create table address(
                        id bigint auto_increment primary key ,
                        city NVARCHAR(50),
                        district NVARCHAR(50),
                        user_id long
)
