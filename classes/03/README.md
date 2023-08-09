<!--
author:   Andrea Charão

email:    andrea@inf.ufsm.br

version:  0.0.1

language: PT-BR

narrator: Brazilian Portuguese Female

comment:  Material de apoio para a disciplina
          ELC117 - Paradigmas de Programação
          da Universidade Federal de Santa Maria

translation: English  translations/English.md
-->

<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/24/README.md
-->


# Programação Funcional

              --{{0}}--
Enter some **text** that gets read out aloud with another voice.
Only paragraphs are allowed!'    


  <div style="column-count:2;column-gap: 20px;">
    <img src="https://m.media-amazon.com/images/I/91jXxWnLKeL._AC_UY327_FMwebp_QL65_.jpg">
    <p>Donec quis ex justo. Vestibulum tempor, quam nec fermentum pharetra, turpis dui dignissim mi, eu auctor mi nisi et odio. Nulla facilisi. Sed nec diam nec arcu hendrerit commodo. Etiam auctor est et nibh cursus feugiat. Nam sollicitudin auctor eros, at suscipit turpis mattis at. Nullam sit amet erat eu tellus tincidunt cursus. Sed ac justo risus.</p>
  </div>
  
  <div style="column-count:2;column-gap: 20px;">
    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus lacinia sapien ac nisi hendrerit, in fringilla justo tincidunt. Sed interdum lorem sit amet ipsum laoreet tempus. Nam vel diam a metus hendrerit dapibus. Sed eu facilisis quam. Integer faucibus neque in elementum fermentum.</p>
    <p>Donec quis ex justo. Vestibulum tempor, quam nec fermentum pharetra, turpis dui dignissim mi, eu auctor mi nisi et odio. Nulla facilisi. Sed nec diam nec arcu hendrerit commodo. Etiam auctor est et nibh cursus feugiat. Nam sollicitudin auctor eros, at suscipit turpis mattis at. Nullam sit amet erat eu tellus tincidunt cursus. Sed ac justo risus.</p>
  </div>

## 1. Programação imperativa X declarativa


- Linguagens são usualmente divididas em duas grandes categorias

  - Linguagens imperativas: 
    https://en.wikipedia.org/wiki/Imperative_programming
  - Linguagens declarativas: 
    https://en.wikipedia.org/wiki/Declarative_programming

- Linguagem C é **imperativa**

- Programação funcional é considerada **declarativa**

- Contraste entre programação **imperativa** e **declarativa** ajuda a entender programação funcional

### Programação imperativa

- Programas são formados por comandos que alteram o estado de variáveis

- Como no modo verbal imperativo da língua portuguesa, usamos comandos para "dar ordens" ao computador

- Comandos detalham as ações que o computador deve executar para obter um resultado

  - Ou seja: programação imperativa se concentra em "**como**" o programa deve obter um resultado

- Próxima da arquitetura de von Neumann

  - dados na memória são transferidos para processamento pela CPU
  - isso explica o sucesso de linguagens imperativas

- Exemplos: 

  - C e outras "clássicas" como Fortran, Pascal, BASIC, etc.
  - várias linguagens modernas e populares são predominantemente imperativas, embora multi-paradigma (Java, Python, C++, etc.)

  
### Programação declarativa

- Programas descrevem o resultado que se deseja obter

- Abordagem de mais alto nível, mais distante da arquitetura

- Sem detalhamento de todas ações que o computador deve executar 
  
  - Ou seja, programação declarativa em concentra mais em definir "**o quê**" se deseja obter, do que em "**como**" obter o resultado desejado
  - Detalhes de mais baixo nível ficam a cargo da implementação da linguagem, não de quem programa (alocação, índices, percurso, etc.)

- Exemplos:

  - clássicas como Lisp, Prolog, Scheme, SQL, entre outras
  - mais recentes como Haskell, Clojure, Scala, F#, Elm, etc.
  - alguns recursos incorporados em linguagens multi-paradigma como Java, Python, C++, etc.

## 2. Um exemplo

- Contraste "teórico" é muito abstrato, costuma levar tempo para ser compreendido
- Praticar e comparar códigos ajuda a entender
- Vamos ver um exemplo com programas equivalentes em C (imperativo) e Haskell (declarativo)

### Em C 

Linguagem procedimental, imperativa

- O que faz este código?
- Que recursos de linguagem você reconhece?


```C
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main() {
  char *sstr = "a-bra-ca-da-bra";
  char *rstr = malloc(strlen(sstr) + 1);

  int rindex = 0;
  for (int i = 0; i < strlen(sstr); i++) {
    if (sstr[i] != '-') {
      rstr[rindex] = sstr[i];
      rindex++;
    }
  }
  rstr[rindex] = '\0';
  printf("%s\n", rstr);
}
```

### Em Haskell 

Linguagem funcional, declarativa

- Programa equivalente ao anterior
- Mesmo sem conhecer a linguagem, o que você consegue entender?

```haskell
main = do
  let sstr = "a-bra-ca-da-bra"
  --putStrLn (filter (\c -> c /= '-') sstr)
  putStrLn (filter (/= '-') sstr)
```



## 3. Características do paradigma

Veremos na sequência algumas características do paradigma funcional:

- [Inspiração em funções matemáticas](#inspiração-em-funções-matemáticas)
- [Sem efeitos colaterais](#sem-efeitos-colaterais)
- [Dados imutáveis](#dados-imutáveis)
- [Favorece boas práticas](#favorece-boas-práticas)


### Inspiração em funções matemáticas

- Em matemática, funções estabelecem correspondência entre elementos de 2 conjuntos
- Por exemplo: 


  $$ f : Z \rightarrow Z $$
  $$ f(x) = x + 4 $$

  - dada a variável independente $ x $, elemento do conjunto $ Z $ de números inteiros (domínio)
  - a regra $ x + 4 $ determina elementos correspondentes no contradomínio $ Z $ (subconjunto imagem)
  - por exemplo, dado $ x = 1 $ no conjunto domínio, teremos $ f(x) = 5 $ no conjunto imagem
  

- Linguagens imperativas como C aproveitam este conceito em subprogramas, mas com "efeitos colaterais" que se distanciam da matemática

### Sem efeitos colaterais

Em inglês, *side effect*: https://en.wikipedia.org/wiki/Side_effect_%28computer_science%29 :


> In computer science, an operation, function or expression is said to have a side effect if it modifies some state variable value(s) outside its local environment, which is to say if it has any observable effect other than its primary effect of returning a value to the invoker of the operation.

Em português: 

> Em ciência da computação, diz-se que uma operação, função ou expressão tem um efeito colateral se ela modifica uma variável fora de seu ambiente local, ou seja, se tem algum efeito observável além de seu efeito primário de retornar um valor para o invocador da operação.

Avance para ver exemplos em C...

                 {{1}}
************************************************

Função em C **sem efeito colateral** (pura), como função matemática:

```C
int f(int x) {
   return x+4;
}
```



************************************************



                 {{2}}
************************************************

Função em C **com efeito colateral** (além do resultado, muda o estado da saída padrão):

```C
int f(int x) {
   printf("%d\n", x);
   return x+4;
}
```

************************************************


                 {{3}}
************************************************

Função em C **com efeito colateral** (muda estado de variável global):

```C
int a = 0;
int f(int x) {
   a++;
   return x+4;
}

```

************************************************

                 {{4}}
************************************************

Função em C **com efeito colateral** (muda estado de argumento):

```C
int g(int x, int* n) {
   *n = 10;
   return x+4;
}

```

************************************************

#### Motivos para evitar

Efeitos colaterais:

- Podem dificultar compreensão do código
- Podem esconder bugs e dificultar depuração / teste
- Podem ser minimizados com boas práticas de programação


### Dados imutáveis

Dados imutáveis: https://en.wikipedia.org/wiki/Immutable_object

> In object-oriented and functional programming, an immutable object is an object whose state cannot be modified after it is created. This is in contrast to a mutable object, which can be modified after it is created. 

- Em C:

  - geralmente trabalhamos com variáveis e estruturas de dados mutáveis, ou seja, cujo estado é modificado ao longo da execução
  - também podemos ter constantes 

- Na programação funcional, dados são imutáveis

  - dados são passados como argumento, mas não são modificados
  - resultados de funções são criados e retornados pela linguagem
  - não existem "variáveis" como em C, mas podemos dar nome a valores e expressões


### Favorece boas práticas

A programação funcional reforça algumas boas práticas que se aplicam à programação em geral:

- Modularidade de código (organização, legibilidade, manutenção)

  - pois todo o código fica distribuído em diversas funções

- Decompor um problema em partes menores, mais facilmente gerenciáveis

  - pois funções sofisticadas podem ser construídas a partir de várias outras mais simples, que já estão prontas ou podem ser criadas e reusadas

- Reconhecer e reusar padrões algorítmicos (produtividade)

  - pois existem muitas funções poderosas que já estão prontas

- Facilitar o teste do código (qualidade)

  - pois ferramentas automatizadas de teste geralmente partem de funções

## 4. Recursos da programação funcional

Independentemente de sintaxe, linguagens com suporte à programação funcional têm alguns recursos em comum

### Básicos

<!-- data-type="none" -->
| Recurso   | Exemplo (notação matemática)  |
| :--------- | :--------- |
| Definição de função     | $$ f(x) = x + 4 $$    |
| Aplicação (uso) de função     | $$ f(1) $$    |
| Composição de funções     | $$ f .  g (x) = f(g(x)) $$    |
| Definição condicional     | $$ f(x) = \begin{cases}
   x^2 + 1 &\text{se } x >= 0 \\
   0 &\text{se } x < 0
\end{cases} $$   |
| Definição recursiva     | $$ f(x) = \begin{cases}
   1 &\text{se } x = 0 \\
   x * f(x-1)  &\text{se } x > 0
\end{cases} $$    |




### Higher order functions

- Higher order functions: https://en.wikipedia.org/wiki/Higher-order_function

- Em português: "funções de ordem superior" ou "funções de alta ordem"

- Funções que:

  - recebem uma ou mais funções como argumento
  - e/ou retornam função como resultado

- Contraste com funções básicas que são "*first-order functions*" (funções de primeira ordem)

- Muito poderosas e vantajosas (código reusável, produtividade, etc.)

- No código [em Haskell](#em-haskell) mostrado anteriormente, temos a função de alta ordem `filter` 


### Lambda / anonymous functions


- Anonymous function: https://en.wikipedia.org/wiki/Anonymous_function

  - Também conhecidas como "lambda functions"

- Funções sem nome, definidas na hora de usar

- Recurso de programação funcional que foi incorporado na maioria das linguagens imperativas modernas


### Outros

- Linguagens de programação funcional costumam ter outros recursos muito interessantes e avançados

  - do ponto de vista de quem programa e também de quem implementa/desenvolve a linguagem (quem desenvolve compiladores, interpretadores, bibliotecas e frameworks)

- Partindo do básico, vale explorar particularidades de cada linguagem


## 5. Linguagens

### Evolução

Você já parou para pensar que, assim como seres vivos, linguagens de programação têm um ciclo de vida: nascem, crescem, possivelmente se reproduzem e morrem ?

Muitos sites têm informação sobre a evolução das linguagens ao longo do tempo:

- Timeline of programming languages: https://en.wikipedia.org/wiki/Timeline_of_programming_languages
- Online Historical Encyclopaedia of Programming Languages: http://hopl.info/ (tem uma grande árvpre genealógica)
- Computer Languages History: https://www.levenez.com/lang/
- PLDB - a Programming Language Database. A computable encyclopedia about programming languages: https://github.com/breck7/pldb (sugestão do mvisentini)

Nesses sites, podemos ver onde se situam as linguagens de programação funcional

### Lisp


Lisp (**lis**t **p**rocessing)

- John McCarthy, MIT (~1960), um dos "pais" da Inteligência Artificial
- processamento de listas
- adotada por precurssores da área de IA
- vários dialetos (GNU Lisp, C Lisp, etc.)

Exemplo

```lisp
(print (car (list 1 2 3)))
(print (cdr (list 1 2 3)))
```

Resultado

```lisp
1
(2 3)
```

Explicação:

- Função `car`: retorna primeiro elemento da lista (head)
- Função `cdr`: retorna o restante da lista, sem o primeiro (tail)


### Haskell

Em https://haskell.org:

> An advanced, purely functional programming language

- Puramente funcional
- Justamente por isso, muito contrastante com outras linguagens imperativas ou multi-paradigma


### Muitas outras

- Muitas linguagens não são puramente funcionais, mas incorporaram recursos de programação funcional
- Linguagens populares atualmente:

  - Python JavaScript, Java, C#: são multi-paradigma, com recursos de programação funcional

- Outras modernas mas não tão conhecidas:

  - Clojure, Scala, F#, Erlang, Elixir, Ruby, etc.

## Bibliografia

- Sebesta, Robert. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html

  - Capítulo 15: Programação Funcional
  - Capítulos anteriores: recursos de linguagens imperativas


- Richard Pawson. Confused about: Subroutine, Procedure, Function, and Method? https://www.computingatschool.org.uk/news-and-blogs/2023/july/confused-about-subroutine-procedure-function-and-methodeur