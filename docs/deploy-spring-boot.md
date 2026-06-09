# Deploy da aplicacao Spring Boot

Esta aplicacao e um backend web Spring Boot com Thymeleaf e Tomcat embutido.
Ela precisa rodar como um processo Java persistente, escutando uma porta HTTP.

## Por que nao usar Vercel para este projeto

A Vercel e excelente para frontends e functions serverless, mas este projeto nao e
um app Next.js/React nem uma function isolada em `/api`. Ele e uma aplicacao Spring
Boot tradicional, com servidor embutido, sessoes, templates Thymeleaf e conexao JPA.

Quando esse tipo de projeto e enviado direto para a Vercel, o resultado comum e 404,
porque a plataforma nao encontra uma saida estatica ou uma rota serverless que
represente a aplicacao.

## Plataformas recomendadas

Use uma plataforma que rode Docker ou Java persistente:

- Render
- Railway
- Fly.io
- Koyeb
- Azure App Service
- AWS Elastic Beanstalk

## Variaveis para producao com Neon

Configure no provedor escolhido:

```properties
SPRING_PROFILES_ACTIVE=neon
DATABASE_URL=jdbc:postgresql://SEU_HOST.neon.tech/SEU_BANCO?sslmode=require
```

Se usuario e senha estiverem separados:

```properties
SPRING_DATASOURCE_USERNAME=SEU_USUARIO
SPRING_DATASOURCE_PASSWORD=SUA_SENHA
```

## Porta

O projeto aceita a variavel `PORT`:

```properties
server.port=${PORT:8080}
```

Isso permite que plataformas como Render ou Railway definam automaticamente a porta
do container/processo. Localmente, sem `PORT`, a aplicacao continua em `8080`.
