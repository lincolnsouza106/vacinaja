ALTER TABLE posto_saude ADD COLUMN IF NOT EXISTS cep VARCHAR(9);
ALTER TABLE posto_saude ADD COLUMN IF NOT EXISTS dias_funcionamento VARCHAR(255);
ALTER TABLE posto_saude ADD COLUMN IF NOT EXISTS hora_abertura TIME;
ALTER TABLE posto_saude ADD COLUMN IF NOT EXISTS hora_fechamento TIME;

UPDATE posto_saude
SET dias_funcionamento = 'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY'
WHERE dias_funcionamento IS NULL;

UPDATE posto_saude
SET hora_abertura = TIME '07:00:00'
WHERE hora_abertura IS NULL;

UPDATE posto_saude
SET hora_fechamento = TIME '19:00:00'
WHERE hora_fechamento IS NULL;
