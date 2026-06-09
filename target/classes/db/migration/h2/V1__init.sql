CREATE TABLE campanha (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_fim DATE,
    data_inicio DATE,
    descricao VARCHAR(255),
    nome VARCHAR(255),
    publico_alvo VARCHAR(255)
);

CREATE TABLE posto_saude (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    endereco VARCHAR(255),
    horario_funcionamento VARCHAR(255),
    nome VARCHAR(255),
    regiao VARCHAR(255),
    telefone VARCHAR(255)
);

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    idade INT NOT NULL,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE vacina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doenca_prevenida VARCHAR(255),
    doses_necessarias INT NOT NULL,
    idade_maxima INT NOT NULL,
    idade_minima INT NOT NULL,
    intervalo_dias INT NOT NULL,
    nome VARCHAR(255)
);

CREATE TABLE posto_saude_vacinas_disponiveis (
    posto_saude_id BIGINT NOT NULL,
    vacinas_disponiveis_id BIGINT NOT NULL,
    CONSTRAINT fk_posto_vacina FOREIGN KEY (vacinas_disponiveis_id) REFERENCES vacina (id),
    CONSTRAINT fk_vacina_posto FOREIGN KEY (posto_saude_id) REFERENCES posto_saude (id)
);

CREATE TABLE registro_vacinacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data DATE,
    dose VARCHAR(255),
    posto_saude_id BIGINT,
    usuario_id BIGINT,
    vacina_id BIGINT,
    CONSTRAINT fk_registro_posto FOREIGN KEY (posto_saude_id) REFERENCES posto_saude (id),
    CONSTRAINT fk_registro_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_registro_vacina FOREIGN KEY (vacina_id) REFERENCES vacina (id)
);
