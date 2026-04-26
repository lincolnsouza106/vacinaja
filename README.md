# VacinaJá

## Descrição do Projeto

O VacinaJá é um sistema desenvolvido para combater a baixa adesão às campanhas de vacinação no Brasil. O projeto visa centralizar e facilitar o acesso à informação sobre postos de saúde, vacinas disponíveis e o calendário vacinal.

## Problema Social

A falta de informação clara e organizada impacta diretamente a saúde pública, aumentando o risco de surtos de doenças preveníveis (como gripe, sarampo e COVID-19).

## Funcionalidades

- Cadastro e gerenciamento de Usuários, Vacinas e Postos de Saúde.
- Registro de histórico de vacinação.
- Controle de campanhas ativas.
- Serviços de busca por região e verificação de pendências vacinais.

## Estrutura (Padrão MVC Simplificado)

- **Model**: Classes de domínio (Usuario, Vacina, PostoSaude, etc.) com seus respectivos atributos, construtores e encapsulamento.
- **Service**: Regras de negócio e armazenamento temporário em memória utilizando `ArrayList`.
- **View**: Interface de interação inicial via console.
