INSERT INTO roles(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');
INSERT INTO users(email,password,username) VALUES ('admin@mail', '$argon2id$v=19$m=4096,t=3,p=1$JATlBjlMZUe50BpPtlNUKg$3jShEBfB3Ya5GPtXXN+5+zXCMIUHY7By1gT8qruLIcA', 'admin');
INSERT INTO user_role(user_id,role_id) VALUES ('1', '2');