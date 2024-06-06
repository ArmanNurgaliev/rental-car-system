INSERT INTO users ( id, location_id, city, country, email, name, password, phone, state, status, street_address, zip_code)
VALUES (1, null, 'Tomsk', 'Russia', 'admin@mail.ru', 'Admin', '$2a$10$ExrikhOceAgEXs5w680iJuzG.ER05Yla62wlGgT.0QeCfMPnR..kq', '89997775544', 'Tomskaya oblast''', 'ACTIVE', 'Lenina, 4', '658952');

INSERT INTO roles (role_id, name)
VALUES (1, 'ROLE_CUSTOMER');
INSERT INTO roles (role_id, name)
VALUES (2, 'ROLE_ADMIN');

INSERT INTO users_roles (role_id, user_id)
VALUES (1, 1);
INSERT INTO users_roles (role_id, user_id)
VALUES (2, 1);