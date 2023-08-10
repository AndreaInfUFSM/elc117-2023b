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
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/02/README.md
-->


# Paradigmas de Programação

> Este material é um resumo de perguntas e respostas importantes sobre o estudo de Paradigmas de Programação

## O que é isso?


> Paradigmas de programação são classificações de linguagens segundo alguns conjuntos de características.


- Cada paradigma engloba um conjunto de características / recursos de programação
- Por exemplo, "herança e polimorfismo" estão entre os recursos característicos da programação orientada a objetos, "funções lambda" são um recurso característico da programação funcional, etc.
- Uma linguagem pode seguir um ou mais paradigmas (ou seja, ter características de mais de um paradigma)
- Existem muitas linguagens, mas poucos paradigmas


## Quais/quantos são?

- Quantas linguagens existem?

  - Difícil responder precisamente, mas...
  - Vejamos uma pista: http://www.99-bottles-of-beer.net/
  - Nesse site, encontramos um mesmo programa em diferentes linguagens e variações (mais de 1500 !)

- Quais os principais paradigmas?

  - Procedural/Procedimental 
  - Funcional
  - Lógico
  - Orientado a Objetos
  - Concorrente
  - Outros mais avançados/específicos: orientado a aspectos, visual, orientado a eventos, etc. 
  - Ver mais na Wikipedia: 
    
    - https://en.wikipedia.org/wiki/Programming_paradigm
    - https://en.wikipedia.org/wiki/Comparison_of_programming_paradigms

## Por que estudá-los?

- Porque é um conhecimento compatível com formação superior na grande área de Computação (contraste com formação técnica)
- Porque amplia sua capacidade de resolver problemas, pensando de formas diferentes
- Porque facilita o aprendizado de novas linguagens que virão no futuro
- Porque torna você alguém com capacidade de fazer a diferença em Computação



## Outras classificações de linguagens

- Classificações são úteis para criar contrastes e organizar o que se sabe

- Algumas classificações contrastantes

  - Programação Imperativa X Declarativa
  - Programação Sequencial X Concorrente
  - Linguagens Interpretadas X Compiladas
  - Por domínio de aplicação 




### Programação imperativa X declarativa

| Programação imperativa   | Programação declarativa   |
| :--------- | :--------- |
| https://en.wikipedia.org/wiki/Imperative_programming     | https://en.wikipedia.org/wiki/Declarative_programming     |
| Ênfase em **como** obter um resultado | Ênfase em descrever **o quê** queremos obter |
| Paradigmas estruturado, procedimental, orientado a objetos | Paradigmas funcional e lógico |



### Programação sequencial X concorrente

| Programação sequencial   | Programação concorrente   |
| :--------- | :--------- |
| Um único fluxo de execução | Vários fluxos de execução |
| Uma operação por intervalo de tempo | Várias operações por intervalo de tempo |

### Linguagens compiladas X interpretadas

| Compilador   | Interpretador   | Híbrido |
| :--------- | :--------- | :--------- |
| Traduz código-fonte em código objeto/executável  | Lê código-fonte e executa instruções  | Associam tradução e interpretação |
| Tradução lenta, execução veloz, resultado não portável | Mais lento que compilado, portabilidade maior | Velocidade intermediária com portabilidade |
| Exemplos: C, C++, ... |  Exemplos: PHP, Python, JavaScript, ... | Exemplos: Java, Python, JavaScript, ... |

### Domínios de aplicação

Linguagens podem também ser classificadas em função do domínio de aplicação (onde costumam ser empregadas)

Por exemplo:

- Software básico: C, C++, Rust, ..
- Computação científica: Fortran, C, Python, ..
- Scripting: Python, bash, Ruby, ..
- Web: PHP, JavaScript/TypeScript, Java, C#, Python, ... (e as de front?)
- Mobile: Java, Kotlin, Objective C, TypeScript, ...
- Inteligência Artificial: LISP, Prolog, .. Python, C++...

## Tarefa rápida


- Procure na Wikipedia sobre estas linguagens: Go, Clojure, Rust                                                  
- Que paradigmas elas seguem?
- Você reconhece termos vistos nesta aula?

### Mini-quiz

Go é uma linguagem de programação funcional

- [( )] Sim
- [(X)] Não



### Classroom

> No menu "Share" no topo à direita, ative a opção Classroom conforme orientações em aula.


Rust é uma linguagem com suporte à programação concorrente

- [(sim)] Sim
- [(nao)] Não

O que você descobriu sobre Clojure?

    [[___ ___ ___ ___]]

## Bibliografia



                 {{0}}
************************************************

Básica

Sebesta, Robert. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html

************************************************

              --{{0}}--
Este livro do Sebesta é uma referência clássica na área. É uma boa fonte para aspectos teóricos sobre as linguagens. Cada capítulo sobre um paradigma traz uma visão geral e depois tem seções focadas em algumas linguagens que suportam o paradigma. Não é uma boa referência para apoiar as práticas com uma linguagem, mas sim para você exercitar leitura e reflexão sobre o que está aprendendo.



                 {{1}}
************************************************

Complementar

- https://en.wikipedia.org/wiki/Programming_paradigm
- https://en.wikipedia.org/wiki/Comparison_of_programming_paradigms

************************************************

              --{{1}}--
É sempre bom conhecer o que a Wikipedia diz sobre os assuntos que você está estudando. Você não precisa ler/aceitar todo o conteúdo, mas aproveite que está acessível e que costuma ser revisado por pessoas que se interessam e entendem do assunto.


