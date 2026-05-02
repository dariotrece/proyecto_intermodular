-- =============================================
-- Datos de ejemplo - Biblioteca API
-- =============================================
-- Usuarios disponibles:
--   dario1  → contraseña: dario1       → rol BIBLIOTECARIO
--   dario   → contraseña: biblioteca123 → rol BIBLIOTECARIO
--   ana1    → contraseña: biblioteca123 → rol USUARIO
--   carlos  → contraseña: biblioteca123 → rol USUARIO
--   maria   → contraseña: biblioteca123 → rol USUARIO
-- (Contraseñas hasheadas con BCrypt, nunca almacenadas en texto plano)
-- =============================================

-- Libros
INSERT INTO public.libros VALUES ('1234567890', 'Frank Herbert', 'Dune');
INSERT INTO public.libros VALUES ('978-0-06-112008-4', 'Harper Lee', 'Matar a un Ruiseñor');
INSERT INTO public.libros VALUES ('978-0-7432-7356-5', 'F. Scott Fitzgerald', 'El Gran Gatsby');
INSERT INTO public.libros VALUES ('978-0-14-028329-7', 'George Orwell', '1984');
INSERT INTO public.libros VALUES ('978-0-06-093546-9', 'Antoine de Saint-Exupéry', 'El Principito');
INSERT INTO public.libros VALUES ('978-84-376-0494-7', 'Gabriel García Márquez', 'Cien Años de Soledad');
INSERT INTO public.libros VALUES ('978-0-14-303943-3', 'Miguel de Cervantes', 'Don Quijote');

-- Usuarios (contraseñas hasheadas con BCrypt)
INSERT INTO public.usuarios VALUES (1, 'Darío', '$2a$10$IjzQu124Lq4k3j2Z20PznO5GF5Bpvz6L3Vj3BSKmyirysPlWZ6I/a', 'BIBLIOTECARIO', 'dario1');
INSERT INTO public.usuarios VALUES (2, 'Dario Perez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh.i', 'BIBLIOTECARIO', 'dario');
INSERT INTO public.usuarios VALUES (6, 'Ana', '$2a$10$gi8c2qEloPXUjWrVVRrIvOwC0RYF7f/J3TjC5AoMVbF3/BREl4roS', 'USUARIO', 'ana1');
INSERT INTO public.usuarios VALUES (7, 'Carlos', '$2a$10$brS8hfEuBjDC2SlOmd6.QuoFyWGMnmXzYAUh/4.sbEQ6nSTmscZTC', 'USUARIO', 'carlos');
INSERT INTO public.usuarios VALUES (8, 'Maria', '$2a$10$evdb/744LEFFIFm1ZxpvGeH.plfGe7oRgmX57DYhWRa/Cv3l07zQ.', 'USUARIO', 'maria');

-- Préstamos (id, fecha_devolucion, fecha_prestamo, libro_isbn, usuario_id)
INSERT INTO public.prestamos VALUES (4, NULL, '2026-04-02 11:51:22.988002', '978-0-06-112008-4', 6);
INSERT INTO public.prestamos VALUES (5, NULL, '2026-04-02 11:51:22.988002', '978-0-14-028329-7', 7);
INSERT INTO public.prestamos VALUES (6, NULL, '2026-04-02 11:51:22.988002', '978-84-376-0494-7', 8);
INSERT INTO public.prestamos VALUES (1, '2026-04-03 13:00:24.218796', '2026-04-02 11:44:23.642043', '978-0-06-112008-4', 2);
INSERT INTO public.prestamos VALUES (7, '2026-04-13 18:06:27.553285', '2026-04-13 18:03:27.779691', '1234567890', 6);

-- Reservas
INSERT INTO public.reservas VALUES (1, 'CANCELADA', '2026-04-03 12:02:05.201486', '978-0-14-028329-7', 6);

-- Valoraciones
INSERT INTO public.valoracion VALUES (1, 'Una obra maestra absolutamente imprescindible', 'BIBLIOTECA', '2026-04-02 11:44:23.645788', 5, 'Matar a un Ruiseñor', 2);
INSERT INTO public.valoracion VALUES (2, 'Muy buena aunque el final me dejó pensando', 'BIBLIOTECA', '2026-04-02 11:44:23.645788', 4, 'El Gran Gatsby', 2);
INSERT INTO public.valoracion VALUES (7, 'Una obra maestra absolutamente imprescindible', 'BIBLIOTECA', '2026-04-02 11:51:22.994915', 5, 'Matar a un Ruiseñor', 6);
INSERT INTO public.valoracion VALUES (8, 'Muy buena aunque el final me dejó pensando', 'BIBLIOTECA', '2026-04-02 11:51:22.994915', 4, 'El Gran Gatsby', 6);
INSERT INTO public.valoracion VALUES (9, 'Escalofriante y brillante a la vez', 'BIBLIOTECA', '2026-04-02 11:51:22.994915', 5, '1984', 7);
INSERT INTO public.valoracion VALUES (10, 'Un clásico que nunca pasa de moda', 'PRIVADO', '2026-04-02 11:51:22.994915', 5, 'El Principito', 7);
INSERT INTO public.valoracion VALUES (11, 'Compleja pero muy enriquecedora', 'BIBLIOTECA', '2026-04-02 11:51:22.994915', 4, 'Cien Años de Soledad', 8);
INSERT INTO public.valoracion VALUES (12, 'Interesante pero cuesta arrancar', 'PRIVADO', '2026-04-02 11:51:22.994915', 3, 'Don Quijote', 8);

-- Secuencias (para que los próximos IDs autogenerados sean correctos)
SELECT pg_catalog.setval('public.prestamos_id_seq', 7, true);
SELECT pg_catalog.setval('public.reservas_id_seq', 1, true);
SELECT pg_catalog.setval('public.usuarios_id_seq', 9, true);
SELECT pg_catalog.setval('public.valoracion_id_seq', 12, true);