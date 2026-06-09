ALTER TABLE usuario ADD COLUMN email VARCHAR(255);
ALTER TABLE usuario ADD COLUMN senha VARCHAR(255);
ALTER TABLE usuario ADD COLUMN role VARCHAR(20);

UPDATE usuario
SET email = LOWER(REPLACE(nome, ' ', '') || id || '@vacinaja.local')
WHERE email IS NULL;

UPDATE usuario
SET senha = '{noop}usuario123'
WHERE senha IS NULL;

UPDATE usuario
SET role = 'USER'
WHERE role IS NULL;

ALTER TABLE usuario ALTER COLUMN email SET NOT NULL;
ALTER TABLE usuario ALTER COLUMN senha SET NOT NULL;
ALTER TABLE usuario ALTER COLUMN role SET NOT NULL;

ALTER TABLE usuario ADD CONSTRAINT uk_usuario_email UNIQUE (email);
