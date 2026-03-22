# Atividade de implementação da feature

## Contexto
Este projeto faz parte de um experimento acadêmico para avaliar esforço de compreensão e esforço de modificação em diferentes arquiteturas de software.

Você recebeu a versão do sistema implementada com **arquitetura em camadas**.

## Sua tarefa
1. Identificar a arquitetura usada no projeto.
2. Justificar com base na estrutura do código.
3. Identificar a classe que deve ser alterada
4. Implementar a feature de cálculo de descontos no carrinho.
5. Realizar testes de ponta a ponta para validar a implementação.
6. Executar os testes até que todos passem.

## Regras que precisam ser implementadas

### 1. Desconto por quantidade total de itens
- 1 item = 0%
- 2 itens = 5%
- 3 itens = 10%
- 4 ou mais itens = 15%

### 2. Desconto adicional por categoria
- CAPINHA = 5%
- CARREGADOR = 10%
- FONE = 8%
- PELICULA = 0%
- SUPORTE = 0%

### 3. Regra de desconto máximo
- A soma dos descontos é cumulativa
- O percentual total não pode ultrapassar 25%

### 4. Total final
- valorDesconto = subtotal * percentualDesconto / 100
- total = subtotal - valorDesconto

## Observações
- Os produtos já estão carregados em memória
- A interface web existe apenas para apoio visual
- A principal validação da atividade ocorre pelos testes unitários

## Resultado esperado
Ao final da implementação:
- todos os testes devem passar, inclusive os testes de ponta a ponta
- o resumo do carrinho deve apresentar subtotal, desconto e total final corretamente
