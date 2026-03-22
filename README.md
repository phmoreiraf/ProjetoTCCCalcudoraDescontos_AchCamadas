# Sistema de Cálculo de Descontos para Marketplace em Arquitetura em Camadas

Projeto base em **Spring Boot** para um experimento acadêmico de comparação arquitetural entre **Arquitetura em Camadas** e **Clean Architecture**.

Este repositório representa a versão em **arquitetura em camadas**.

## Objetivo do projeto

O sistema simula um pequeno marketplace de acessórios para celulares. Os produtos ficam carregados em memória e o usuário pode selecionar quantidades para montar um carrinho.

A feature principal do experimento é a implementação da regra de **cálculo de descontos** e do **total final do carrinho**.

## Regras de negócio da feature

### Desconto por quantidade total de itens
- 1 item: **0%**
- 2 itens: **5%**
- 3 itens: **10%**
- 4 ou mais itens: **15%**

### Desconto adicional por categoria
- **CAPINHA**: 5%
- **CARREGADOR**: 10%
- **FONE**: 8%
- **PELÍCULA** e **SUPORTE**: 0%

### Regra de limite
- O desconto total é **cumulativo**
- O desconto total **não pode ultrapassar 25%**

## Estrutura em camadas

```text
src/main/java/com/example/marketplace
├── controller
├── model
├── repository
├── service
└── MarketplaceApplication.java
```

## Tecnologias
- Java 17
- Spring Boot 3
- Thymeleaf
- JUnit 5
- Maven

## O que já está pronto
- Estrutura completa do projeto
- Produtos em memória
- Layout simples para execução local
- Controller, Service, Repository e Model
- Testes unitários já implementados
- Documentação da atividade
- Diagrama da arquitetura em camadas

## O que falta implementar
A classe abaixo contém a feature incompleta:

```text
src/main/java/com/example/marketplace/service/CartService.java
```

O método a ser concluído é:

```java
public CartSummary buildSummary(List<CartSelection> selections)
```

## Como executar

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

## Evidências da arquitetura em camadas
- Organização por **controller / service / repository / model**
- Regra de negócio concentrada na camada **service**
- Fluxo vertical tradicional
- Ausência de use cases explícitos e independentes

## Diagrama de arquitetura
Os arquivos do diagrama estão em `docs/`.
