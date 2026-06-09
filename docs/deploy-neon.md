# Deploy com Neon PostgreSQL

Este projeto agora possui um perfil separado para producao usando Neon/PostgreSQL.
O perfil local continua usando H2, enquanto o deploy deve ativar o perfil `neon`.
As migrations foram separadas por banco:

- H2 local: `classpath:db/migration/h2`
- Neon/PostgreSQL: `classpath:db/migration/postgresql`

## Variaveis de ambiente

Configure no provedor de deploy:

```properties
SPRING_PROFILES_ACTIVE=neon
DATABASE_URL=jdbc:postgresql://SEU_HOST.neon.tech/SEU_BANCO?sslmode=require
```

Se preferir separar usuario e senha, use a URL sem credenciais e configure tambem:

```properties
SPRING_DATASOURCE_USERNAME=SEU_USUARIO
SPRING_DATASOURCE_PASSWORD=SUA_SENHA
```

Importante: o Spring precisa de uma URL JDBC. Se o Neon fornecer uma URL no formato
`postgresql://usuario:senha@host/banco?sslmode=require`, converta para:

```properties
jdbc:postgresql://host/banco?sslmode=require
```

Depois informe usuario e senha pelas variaveis `SPRING_DATASOURCE_USERNAME` e
`SPRING_DATASOURCE_PASSWORD`.

Confira tambem se o host do Neon esta completo. Ele normalmente termina com
`.neon.tech`. Se terminar apenas em `.aws`, a aplicacao vai falhar com
`UnknownHostException` porque o DNS nao consegue encontrar o banco.

## Banco e migrations

No perfil `neon`, o Flyway usa as migrations em:

```text
classpath:db/migration/postgresql
```

A migration inicial cria as tabelas finais esperadas pela aplicacao:

- `usuario`
- `vacina`
- `posto_saude`
- `posto_saude_vacinas_disponiveis`
- `registro_vacinacao`
- `campanha`
- `agendamento`

O Hibernate esta configurado com `ddl-auto=validate`, ou seja, ele nao cria nem altera
tabelas automaticamente em producao. Quem cria o schema e o Flyway.

## Primeiro acesso

Quando o banco estiver vazio, o `DataInitializer` cria dados iniciais, incluindo:

```text
admin@vacinaja.com / admin123
```

Recomenda-se trocar essa senha depois do primeiro acesso em um fluxo futuro de gestao
de conta.

## Observacoes

- O console H2 fica desativado no perfil `neon`.
- O pool de conexoes foi limitado por padrao para 5 conexoes com `DB_POOL_SIZE`, o que
  combina melhor com bancos gerenciados e planos menores.
- Para deploy real, use a connection string com `sslmode=require`.
