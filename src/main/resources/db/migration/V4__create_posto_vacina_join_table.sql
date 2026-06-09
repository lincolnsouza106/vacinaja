CREATE TABLE IF NOT EXISTS posto_saude_vacinas_disponiveis (
    posto_saude_id BIGINT NOT NULL,
    vacinas_disponiveis_id BIGINT NOT NULL,
    CONSTRAINT fk_posto_vacina FOREIGN KEY (vacinas_disponiveis_id) REFERENCES vacina (id),
    CONSTRAINT fk_vacina_posto FOREIGN KEY (posto_saude_id) REFERENCES posto_saude (id)
);
