delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$aM9lerPepvPNT5sKBfnDzurzWkBZv5m/D6YHZlX6HWYaAyC6xTnxK', 'admin'),
(2, true, '$2a$08$BvAhzQ3ppxCUxaNjxCljS.C6oxAOPPSjSeS9WJ/0ov/M.nqXu/wpi', 'u1');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'), (2, 'USER');