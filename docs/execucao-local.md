# Execucao local no IntelliJ

## JDK

O projeto usa Spring Boot 3.2.3 e Java 17. No IntelliJ, configure:

- Project SDK: Java 17 ou superior
- Gradle/Maven Runner JRE: Java 17 ou superior

No terminal desta maquina, o comando `java -version` aponta para Java 8. Isso nao
serve para rodar o projeto. Se for executar fora do IntelliJ, use um JDK 17+.

## Banco local H2

O ambiente local usa H2 em arquivo:

```properties
spring.datasource.url=jdbc:h2:file:./data/vacinaja-local
spring.datasource.username=sa
spring.datasource.password=
```

O banco anterior `./data/vacinaja` foi mantido, mas pode ter sido criado com outra
credencial e gerar erro de autenticacao. O novo arquivo `vacinaja-local` evita esse
conflito sem apagar dados antigos.

## Primeiro acesso local

Ao iniciar com banco limpo, o `DataInitializer` cria:

```text
admin@vacinaja.com
admin123
```

## Se o IntelliJ continuar usando arquivos antigos

Use:

1. `Build > Rebuild Project`
2. Pare qualquer execucao antiga da aplicacao
3. Rode novamente a classe `VacinaJaApplication`

Se ainda aparecer erro de Flyway ou schema, apague apenas o banco local novo:

```text
data/vacinaja-local.mv.db
data/vacinaja-local.trace.db
```

Ele sera recriado automaticamente na proxima execucao.
