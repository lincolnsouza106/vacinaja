CREATE TABLE agendamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    horario TIME NOT NULL,
    observacao VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    posto_saude_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    vacina_id BIGINT NOT NULL,
    CONSTRAINT fk_agendamento_posto FOREIGN KEY (posto_saude_id) REFERENCES posto_saude (id),
    CONSTRAINT fk_agendamento_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_agendamento_vacina FOREIGN KEY (vacina_id) REFERENCES vacina (id)
);
