# Sistema de Cálculo de Descontos para Marketplace em Arquitetura em Camadas

## Objetivo do projeto

O sistema simula um pequeno marketplace de acessórios para celulares. Os produtos ficam carregados em memória e o usuário pode selecionar quantidades para montar um carrinho.

A feature principal do experimento é a implementação da regra de **cálculo de descontos** e do **total final do carrinho**.

## Tecnologias
- Java 17
- Spring Boot 3
- Thymeleaf
- JUnit 5
- Maven

## Como executar

```Instalar dependências e compilar o projeto
mvn clean install
```

```bash
mvn spring-boot:run
```

Acesse:
```text
http://localhost:8080
```

## Como executar os testes

```bash
mvn test
```

## Resultado esperado no experimento
Inicialmente, parte dos testes falhará, porque a lógica de desconto ainda não foi implementada.
O objetivo do participante é localizar o ponto correto da modificação, implementar a feature e fazer todos os testes passarem.
Realizar um teste de ponta a ponta para concretizar todas as regras de negocios.

## Diagrama de arquitetura
Os arquivos do diagrama estão em `docs/`.

## Regras de negocios e cenarios de testes
Os arquivos estão em `docs/`.

## Responda o formulario de feedback (OBRIGATÓRIO)
Após a conclusão da atividade, por favor, responda o formulário de feedback para ajudar na avaliação do experimento:
[Formulário de Feedback]()
