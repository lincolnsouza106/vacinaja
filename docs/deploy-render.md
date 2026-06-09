# Roteiro de deploy no Render

Este roteiro considera o projeto VacinaJa como uma aplicacao Spring Boot com
Docker e banco Neon PostgreSQL.

## 1. Preparar o repositorio

Antes de criar o servico no Render, garanta que o codigo mais recente esta no
GitHub/GitLab/Bitbucket.

Arquivos importantes ja presentes no projeto:

- `Dockerfile`
- `pom.xml`
- `src/main/resources/application-neon.properties`
- `src/main/resources/db/migration/postgresql/V1__init.sql`

O Render deve usar Docker para esse projeto, porque a aplicacao e Java/Spring
Boot e roda como servidor web persistente.

## 2. Criar o banco no Neon

1. Acesse o painel do Neon.
2. Crie um projeto PostgreSQL.
3. Copie os dados de conexao do banco.
4. Use SSL no deploy com `sslmode=require`.

Formato recomendado para o Spring:

```properties
DATABASE_URL=jdbc:postgresql://SEU_HOST.neon.tech/SEU_BANCO?sslmode=require
SPRING_DATASOURCE_USERNAME=SEU_USUARIO
SPRING_DATASOURCE_PASSWORD=SUA_SENHA
```

O host precisa estar completo. Em conexoes pooled do Neon, ele costuma ter um
formato parecido com:

```text
ep-alguma-coisa-pooler.c-2.us-east-1.aws.neon.tech
```

Se o host terminar apenas em `.aws`, esta incompleto e o Render vai falhar com
`UnknownHostException`.

Se o Neon entregar uma URL no formato:

```text
postgresql://usuario:senha@host/banco?sslmode=require
```

converta para:

```text
jdbc:postgresql://host/banco?sslmode=require
```

e coloque usuario/senha nas variaveis separadas.

## 3. Criar o Web Service no Render

1. Entre no painel do Render.
2. Clique em **New +**.
3. Selecione **Web Service**.
4. Conecte o repositorio do projeto.
5. Em **Language**, selecione **Docker**.
6. Confirme que o `Dockerfile` esta na raiz do repositorio.
7. Escolha a branch principal do projeto.
8. Escolha a regiao mais proxima possivel do banco Neon.
9. Escolha o plano desejado.
10. Em **Advanced**, configure as variaveis de ambiente.

## 4. Variaveis de ambiente no Render

Configure no servico:

```properties
SPRING_PROFILES_ACTIVE=neon
DATABASE_URL=jdbc:postgresql://SEU_HOST.neon.tech/SEU_BANCO?sslmode=require
SPRING_DATASOURCE_USERNAME=SEU_USUARIO
SPRING_DATASOURCE_PASSWORD=SUA_SENHA
DB_POOL_SIZE=5
```

Observacoes:

- Nao coloque aspas nos valores.
- Nao comite senha ou connection string real no repositorio.
- `SPRING_PROFILES_ACTIVE=neon` e obrigatorio para usar PostgreSQL/Neon.
- `DB_POOL_SIZE=5` evita abrir conexoes demais em planos pequenos.

## 5. Porta da aplicacao

O projeto ja aceita a variavel `PORT`:

```properties
server.port=${PORT:8080}
```

O Render recomenda que o servidor HTTP use a porta indicada por `PORT`. Se o
Render nao definir essa variavel, a aplicacao usa `8080`.

## 6. Health check

Se o Render pedir um caminho de health check, use:

```text
/login
```

Motivo: a tela inicial `/` exige usuario autenticado e redireciona para login.
A rota `/login` e publica e deve responder normalmente.

## 7. Primeiro deploy

Depois de configurar tudo:

1. Clique em **Create Web Service**.
2. Acompanhe os logs de build.
3. O Docker vai executar o Maven e gerar o `.jar`.
4. No startup, o Spring Boot sobe com o perfil `neon`.
5. O Flyway cria as tabelas no Neon usando `db/migration/postgresql`.
6. O `DataInitializer` cria o usuario administrador inicial se o banco estiver vazio.

Credenciais iniciais:

```text
admin@vacinaja.com
admin123
```

## 8. Checklist de validacao apos deploy

1. Acesse a URL `.onrender.com` gerada pelo Render.
2. Confirme que abre a tela de login.
3. Entre com `admin@vacinaja.com` e `admin123`.
4. Verifique a tela inicial.
5. Acesse **Usuarios** e cadastre um usuario comum.
6. Saia e entre com o usuario comum.
7. Confirme que ele nao ve telas administrativas.
8. Teste carteira, agendamentos, postos e campanhas.

## 9. Erros comuns

### Erro: banco H2 sendo usado no Render

Causa provavel: faltou configurar o perfil.

Verifique:

```properties
SPRING_PROFILES_ACTIVE=neon
```

### Erro: URL JDBC invalida

Causa provavel: foi usada a URL do Neon comecando com `postgresql://`.

Use:

```properties
jdbc:postgresql://host/banco?sslmode=require
```

### Erro: UnknownHostException no host do Neon

Causa provavel: o host foi colado incompleto nas variaveis do Render.
Exemplo de host errado:

```text
ep-dry-recipe-aqosmm9s-pooler.c-8.us-east-1.aws
```

Exemplo de host esperado:

```text
ep-dry-recipe-aqosmm9s-pooler.c-8.us-east-1.aws.neon.tech
```

Copie novamente a connection string no painel do Neon e confirme que o valor de
`DATABASE_URL` no Render contem o dominio completo antes do nome do banco.

### Erro: Flyway encontrou migrations duplicadas

Causa provavel: build antigo ou pasta `target` com arquivos antigos localmente.
No Render, isso tende a desaparecer em build limpo. Localmente, rode:

```powershell
mvn clean package
```

### Erro: aplicacao sobe, mas Render nao detecta porta

Confirme se o deploy pegou a configuracao:

```properties
server.port=${PORT:8080}
```

e veja nos logs se o Tomcat iniciou na porta indicada.

### Erro: Hibernate validate falhou

Causa provavel: o schema do Neon nao esta igual ao esperado pela aplicacao.
Se for um banco de teste e puder apagar os dados, limpe o schema ou crie outro
banco no Neon para o Flyway aplicar a migration inicial do zero.

### Erro: missing column intervalo_doses in table vacina

Causa provavel: o atributo Java `intervaloDoses` estava sendo traduzido pelo
Hibernate para `intervalo_doses`, enquanto as migrations criaram a coluna
historica `intervalo_dias`.

Correcao aplicada no codigo:

```java
@Column(name = "intervalo_dias")
private int intervaloDoses;
```

Depois dessa correcao, basta fazer novo deploy. Nao e necessario apagar o banco
por causa desse erro especifico, porque a coluna `intervalo_dias` ja existe.

## 10. Fontes oficiais consultadas

- Docker no Render: https://render.com/docs/docker
- Web Services e porta `PORT`: https://render.com/docs/web-services
- Variaveis de ambiente e segredos: https://render.com/docs/configure-environment-variables
