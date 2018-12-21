/* Se crean Usuarios con Roles*/
INSERT INTO `usuarios` (username, password, enabled) VALUES ('andres', '$2a$10$AH2vsaFzRNPaE2kmzw181.qXeC/VqJpJ7RA242GvfljBn2ZQ5nr96', 1);
INSERT INTO `usuarios` (username, password, enabled) VALUES ('admin', '$2a$10$aTkJs8FJdm.SqwtBGirup.cGvNjx76CX7/vwvaJzTET1ZQjNE1riW', 1);

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (1, 1);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 1);