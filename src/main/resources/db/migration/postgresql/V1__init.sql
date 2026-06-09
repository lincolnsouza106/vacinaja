CREATE TABLE campanha (
    id BIGSERIAL PRIMARY KEY,
    data_fim DATE,
    data_inicio DATE,
    descricao VARCHAR(255),
    nome VARCHAR(255),
    publico_alvo VARCHAR(255)
);

CREATE TABLE posto_saude (
    id BIGSERIAL PRIMARY KEY,
    endereco VARCHAR(255),
    horario_funcionamento VARCHAR(255),
    nome VARCHAR(255),
    regiao VARCHAR(255),
    telefone VARCHAR(255)
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    idade INTEGER NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

CREATE TABLE vacina (
    id BIGSERIAL PRIMARY KEY,
    doenca_prevenida VARCHAR(255),
    doses_necessarias INTEGER NOT NULL,
    idade_maxima INTEGER NOT NULL,
    idade_minima INTEGER NOT NULL,
    intervalo_dias INTEGER NOT NULL,
    nome VARCHAR(255)
);

CREATE TABLE posto_saude_vacinas_disponiveis (
    posto_saude_id BIGINT NOT NULL,
    vacinas_disponiveis_id BIGINT NOT NULL,
    CONSTRAINT pk_posto_saude_vacinas PRIMARY KEY (posto_saude_id, vacinas_disponiveis_id),
    CONSTRAINT fk_posto_vacina FOREIGN KEY (vacinas_disponiveis_id) REFERENCES vacina (id),
    CONSTRAINT fk_vacina_posto FOREIGN KEY (posto_saude_id) REFERENCES posto_saude (id)
);

CREATE TABLE registro_vacinacao (
    id BIGSERIAL PRIMARY KEY,
    data DATE,
    dose VARCHAR(255),
    posto_saude_id BIGINT,
    usuario_id BIGINT,
    vacina_id BIGINT,
    CONSTRAINT fk_registro_posto FOREIGN KEY (posto_saude_id) REFERENCES posto_saude (id),
    CONSTRAINT fk_registro_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_registro_vacina FOREIGN KEY (vacina_id) REFERENCES vacina (id)
);

CREATE TABLE agendamento (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    horario TIME(6) NOT NULL,
    observacao VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    posto_saude_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    vacina_id BIGINT NOT NULL,
    CONSTRAINT fk_agendamento_posto FOREIGN KEY (posto_saude_id) REFERENCES posto_saude (id),
    CONSTRAINT fk_agendamento_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_agendamento_vacina FOREIGN KEY (vacina_id) REFERENCES vacina (id)
);
