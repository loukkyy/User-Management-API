-- add privileges
insert into privilege values (1, 'ROLE_USER_READ', 'ROLE_USER_READ');
insert into privilege values (2, 'ROLE_USER_WRITE', 'ROLE_USER_WRITE');
insert into privilege values (3, 'ROLE_USER_DELETE', 'ROLE_USER_DELETE');
insert into privilege values (4, 'ROLE_PRIVILEGE_READ', 'ROLE_PRIVILEGE_READ');
insert into privilege values (5, 'ROLE_PRIVILEGE_WRITE', 'ROLE_PRIVILEGE_WRITE');
insert into privilege values (6, 'ROLE_PRIVILEGE_DELETE', 'ROLE_PRIVILEGE_DELETE');

-- add roles
insert into role values (1, 'ROLE_ADMIN');
insert into role values (2, 'ROLE_USER');

-- add roles to user
insert into role_privileges values (1, 1); -- admin can user read
insert into role_privileges values (1, 2); -- admin can user write
insert into role_privileges values (1, 3); -- admin can user delete
insert into role_privileges values (1, 4); -- admin can privilege read
insert into role_privileges values (1, 5); -- admin can privilege write
insert into role_privileges values (1, 6); -- admin can privilege delete
insert into role_privileges values (2, 1); -- user can user read
insert into role_privileges values (2, 4); -- user privilege user read

-- add users
insert into user values (1, '2020-04-05 11:59:15.493', 'user@test.com', 'TRUE', 'John', 'Doe', '$2a$10$LNPcf0oj6wIC42DOydCFluqs2jkCJa1to4UUtIqy97AibvAgu/8u.'); --  user@test.com/123
insert into user values (2, '2020-04-05 11:59:15.493', 'admin@test.com', 'TRUE', 'Jane', 'Doe', '$2a$10$LNPcf0oj6wIC42DOydCFluqs2jkCJa1to4UUtIqy97AibvAgu/8u.'); --  admin@test.com/123

-- add roles to users
insert into user_roles values (1, 2); -- user@test.com has ROLE_USER role
insert into user_roles values (2, 1); -- admin@test.com has ROLE_ADMIN role
insert into user_roles values (2, 2); -- admin@test.com has ROLE_USER role

