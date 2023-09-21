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

link:     https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.css

script:   https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.js


onload
window.CodeRunner = {
    ws: undefined,
    handler: {},

    init(url) {
        this.ws = new WebSocket(url);
        const self = this
        this.ws.onopen = function () {
            self.log("connections established");
            setInterval(function() {
                self.ws.send("ping")
            }, 15000);
        }
        this.ws.onmessage = function (e) {
            // e.data contains received string.

            let data
            try {
                data = JSON.parse(e.data)
            } catch (e) {
                self.warn("received message could not be handled =>", e.data)
            }
            if (data) {
                self.handler[data.uid](data)
            }
        }
        this.ws.onclose = function () {
            self.warn("connection closed")
        }
        this.ws.onerror = function (e) {
            self.warn("an error has occurred => ", e)
        }
    },
    log(...args) {
        console.log("CodeRunner:", ...args)
    },
    warn(...args) {
        console.warn("CodeRunner:", ...args)
    },
    handle(uid, callback) {
        this.handler[uid] = callback
    },
    send(uid, message) {
        message.uid = uid
        this.ws.send(JSON.stringify(message))
    }
}

//window.CodeRunner.init("wss://coderunner.informatik.tu-freiberg.de/")
window.CodeRunner.init("wss://ancient-hollows-41316.herokuapp.com/")

//window.CodeRunner.init("ws://127.0.0.1:8000/")

@end


@LIA.c:       @LIA.eval(`["main.c"]`, `gcc -Wall main.c -o a.out`, `./a.out`)
@LIA.clojure: @LIA.eval(`["main.clj"]`, `none`, `clojure -M main.clj`)
@LIA.cpp:     @LIA.eval(`["main.cpp"]`, `g++ main.cpp -o a.out`, `./a.out`)
@LIA.go:      @LIA.eval(`["main.go"]`, `go build main.go`, `./main`)
@LIA.haskell: @LIA.eval(`["main.hs"]`, `ghc main.hs -o main`, `./main`)
@LIA.java:    @LIA.eval(`["@0.java"]`, `javac @0.java`, `java @0`)
@LIA.julia:   @LIA.eval(`["main.jl"]`, `none`, `julia main.jl`)
@LIA.mono:    @LIA.eval(`["main.cs"]`, `mcs main.cs`, `mono main.exe`)
@LIA.nasm:    @LIA.eval(`["main.asm"]`, `nasm -felf64 main.asm && ld main.o`, `./a.out`)
@LIA.python:  @LIA.python3
@LIA.python2: @LIA.eval(`["main.py"]`, `python2.7 -m compileall .`, `python2.7 main.pyc`)
@LIA.python3: @LIA.eval(`["main.py"]`, `none`, `python3 main.py`)
@LIA.r:       @LIA.eval(`["main.R"]`, `none`, `Rscript main.R`)
@LIA.rust:    @LIA.eval(`["main.rs"]`, `rustc main.rs`, `./main`)
@LIA.zig:     @LIA.eval(`["main.zig"]`, `zig build-exe ./main.zig -O ReleaseSmall`, `./main`)

@LIA.dotnet:  @LIA.dotnet_(@uid)

@LIA.dotnet_
<script>
var uid = "@0"
var files = []

files.push(["project.csproj", `<Project Sdk="Microsoft.NET.Sdk">
  <PropertyGroup>
    <OutputType>Exe</OutputType>
    <TargetFramework>net6.0</TargetFramework>
    <ImplicitUsings>enable</ImplicitUsings>
    <Nullable>enable</Nullable>
  </PropertyGroup>
</Project>`])

files.push(["Program.cs", `@input(0)`])

send.handle("input", (e) => {
    CodeRunner.send(uid, {stdin: e})
})
send.handle("stop",  (e) => {
    CodeRunner.send(uid, {stop: true})
});


CodeRunner.handle(uid, function (msg) {
    switch (msg.service) {
        case 'data': {
            if (msg.ok) {
                CodeRunner.send(uid, {compile: "dotnet build -nologo"})
            }
            else {
                send.lia("LIA: stop")
            }
            break;
        }
        case 'compile': {
            if (msg.ok) {
                if (msg.message) {
                    if (msg.problems.length)
                        console.warn(msg.message);
                    else
                        console.log(msg.message);
                }

                send.lia("LIA: terminal")
                console.clear()
                CodeRunner.send(uid, {exec: "dotnet run"})
            } else {
                send.lia(msg.message, msg.problems, false)
                send.lia("LIA: stop")
            }
            break;
        }
        case 'stdout': {
            if (msg.ok)
                console.stream(msg.data)
            else
                console.error(msg.data);
            break;
        }

        case 'stop': {
            if (msg.error) {
                console.error(msg.error);
            }

            if (msg.images) {
                for(let i = 0; i < msg.images.length; i++) {
                    console.html("<hr/>", msg.images[i].file)
                    console.html("<img title='" + msg.images[i].file + "' src='" + msg.images[i].data + "' onclick='window.LIA.img.click(\"" + msg.images[i].data + "\")'>")
                }

            }

            send.lia("LIA: stop")
            break;
        }

        default:
            console.log(msg)
            break;
    }
})


CodeRunner.send(
    uid, { "data": files }
);

"LIA: wait"
</script>
@end

@LIA.eval:  @LIA.eval_(false,@uid,`@0`,@1,@2)

@LIA.evalWithDebug: @LIA.eval_(true,@uid,`@0`,@1,@2)

@LIA.eval_
<script>
const uid = "@1"
var order = @2
var files = []

if (order[0])
  files.push([order[0], `@'input(0)`])
if (order[1])
  files.push([order[1], `@'input(1)`])
if (order[2])
  files.push([order[2], `@'input(2)`])
if (order[3])
  files.push([order[3], `@'input(3)`])
if (order[4])
  files.push([order[4], `@'input(4)`])
if (order[5])
  files.push([order[5], `@'input(5)`])
if (order[6])
  files.push([order[6], `@'input(6)`])
if (order[7])
  files.push([order[7], `@'input(7)`])
if (order[8])
  files.push([order[8], `@'input(8)`])
if (order[9])
  files.push([order[9], `@'input(9)`])


send.handle("input", (e) => {
    CodeRunner.send(uid, {stdin: e})
})
send.handle("stop",  (e) => {
    CodeRunner.send(uid, {stop: true})
});


CodeRunner.handle(uid, function (msg) {
    switch (msg.service) {
        case 'data': {
            if (msg.ok) {
                CodeRunner.send(uid, {compile: @3})
            }
            else {
                send.lia("LIA: stop")
            }
            break;
        }
        case 'compile': {
            if (msg.ok) {
                if (msg.message) {
                    if (msg.problems.length)
                        console.warn(msg.message);
                    else
                        console.log(msg.message);
                }

                send.lia("LIA: terminal")
                CodeRunner.send(uid, {exec: @4})

                if(!@0) {
                  console.clear()
                }
            } else {
                send.lia(msg.message, msg.problems, false)
                send.lia("LIA: stop")
            }
            break;
        }
        case 'stdout': {
            if (msg.ok)
                console.stream(msg.data)
            else
                console.error(msg.data);
            break;
        }

        case 'stop': {
            if (msg.error) {
                console.error(msg.error);
            }

            if (msg.images) {
                for(let i = 0; i < msg.images.length; i++) {
                    console.html("<hr/>", msg.images[i].file)
                    console.html("<img title='" + msg.images[i].file + "' src='" + msg.images[i].data + "' onclick='window.LIA.img.click(\"" + msg.images[i].data + "\")'>")
                }

            }

            send.lia("LIA: stop")
            break;
        }

        default:
            console.log(msg)
            break;
    }
})


CodeRunner.send(
    uid, { "data": files }
);

"LIA: wait"
</script>
@end

-->

<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/13/README.md
-->




# Programação Lógica




> Este material é parte de uma introdução ao paradigma de **programação lógica** em linguagem Prolog.
>
> O conteúdo tem partes interativas e pode ser visualizado de vários modos usando as opções no topo da página.

## Execução Prolog


- Internamente, Prolog organiza a base de fatos e regras em uma estrutura de dados em árvore
- Execução de um programa Prolog é 

  - uma busca em profundidade (depth-first search) na base de fatos e regras
  - para verificar se uma proposição é verdadeira
  - com instanciação de valores para variáveis



### Mecanismos da execução

- Unificação (matching): busca com instanciação de valores para variáveis

- Retrocesso (backtracking): refaz unificação seguindo outra possibilidade (outro ramo não vistado na árvore)

- Corte (cut, !): impede backtracking




### Execução passo-a-passo


Em SWI-Prolog:

- Predicado `trace`: ativa execução passo-a-passo
- Predicado `nodebug`: volta ao modo normal
- Ver mais em: https://www.swi-prolog.org/pldoc/man?section=debugoverview


Para visualizar os processos de unificação e backtracking em ação, podemos ativar a execução passo-a-passo no SWI-Prolog.

### Call-Exit-Redo-Fail

Na execução passo-a-passo, pode acontecer

- Call: busca predicado e argumentos na base
- Exit: conseguiu unificar (true), volta da busca
- Redo: refaz a busca (backtrack)
- Fail: não conseguiu unificar (false), volta da busca



## Exemplos

Fatos e regras sobre 2 famílias "famosas": Os Flintstones e Os Simpsons

``` prolog
female(marge).
female(lisa).
female(maggie).
female(wilma).
female(pebbles). % pedrita :-)
male(homer).
male(bart).
male(fred).

parent(marge, bart).
parent(homer, bart).
parent(marge, lisa).
parent(homer, lisa).
parent(marge, maggie).
parent(homer, maggie).
parent(wilma, pebbles).
parent(fred, pebbles).

mother(X, Y) :- 
  parent(X, Y), 
  female(X).
  
father(X, Y) :- 
  parent(X, Y), 
  male(X).
  
son(X, Y) :- 
  parent(Y, X), 
  male(X).
  
daughter(X, Y) :- 
  parent(Y, X), 
  female(X).
```

### Consulta com busca em fato

- Fato: `male`
- Variável X na busca é substituída por nome interno, diferente a cada execução ( ex: _4814)
- Call: busca na base
- Exit: deu match, volta da busca
- Redo: refaz a busca

> Aqui usamos ";"/Next  para forçar Redo


```
?- trace.
true.

[trace]  ?- male(X).
   Call: (8) male(_4014) ? creep
   Exit: (8) male(homer) ? creep
X = homer ;
   Redo: (8) male(_4014) ? creep
   Exit: (8) male(bart) ? creep
X = bart ;
   Redo: (8) male(_4014) ? creep
   Exit: (8) male(fred) ? creep
X = fred.
```



### Consulta com busca em regra


Agora vamos ver outra consulta, desta vez com o predicado `father`, definido com uma regra (com condições). Queremos saber quem é o pai `X` de `bart`.

- Consulta: `father(X,bart).`
- Regra: `father`, com 2 condições
- Fail: não encontrou o que estava procurando
- Redo: refaz a busca por outro ramo (backtrack)

> busca alternativa com backtracking é automática aqui, sem necessidade de forçar com ";"

```
?- trace.
true.

[trace]  ?- father(X,bart).
   Call: (8) father(_4066, bart) ? creep
   Call: (9) parent(_4066, bart) ? creep
   Exit: (9) parent(marge, bart) ? creep
   Call: (9) male(marge) ? creep
   Fail: (9) male(marge) ? creep
   Redo: (9) parent(_4066, bart) ? creep
   Exit: (9) parent(homer, bart) ? creep
   Call: (9) male(homer) ? creep
   Exit: (9) male(homer) ? creep
   Exit: (8) father(homer, bart) ? creep
X = homer.
```

### Consulta sem/com corte (cut, !)

Vejamos agora uma consulta com o predicado `son`, também definido com uma regra (com condições). Queremos saber quem são os filhos (ou filho) `F` de `marge`.

- Consulta: `son(F,marge).`
- Regra: `son`, com 2 condições
- Após o primeiro resultado, usamos ";" para forçar backtracking
- Havia outras opções para a condição `parent`, mas nenhuma é verdadeira para a condição `male`, então ambas falham e o resultado é `false`

```
?- trace,son(X,marge).
   Call: (11) son(_21556, marge) ? creep
   Call: (12) parent(marge, _21556) ? creep
   Exit: (12) parent(marge, bart) ? creep
   Call: (12) male(bart) ? creep
   Exit: (12) male(bart) ? creep
   Exit: (11) son(bart, marge) ? creep
X = bart ;
   Redo: (12) parent(marge, _21556) ? creep
   Exit: (12) parent(marge, lisa) ? creep
   Call: (12) male(lisa) ? creep
   Fail: (12) male(lisa) ? creep
   Redo: (12) parent(marge, _21556) ? creep
   Exit: (12) parent(marge, maggie) ? creep
   Call: (12) male(maggie) ? creep
   Fail: (12) male(maggie) ? creep
   Fail: (11) son(_21556, marge) ? creep
false.
```

#### Com corte

Agora usando `!` (cut/corte)

- Após o primeiro resultado, execução da consulta termina
- Ou seja, backtracking é impedido

```
[trace]  ?- trace,son(X,marge),!.
   Call: (11) son(_36126, marge) ? creep
   Call: (12) parent(marge, _36126) ? creep
   Exit: (12) parent(marge, bart) ? creep
   Call: (12) male(bart) ? creep
   Exit: (12) male(bart) ? creep
   Exit: (11) son(bart, marge) ? creep
X = bart.
```


## Regras recursivas com listas

A seguir, veja o passo-a-passo da execução de alguns predicados recursivos com listas

### Tamanho da lista

Predicado recursivo para encontrar o tamanho de uma lista (equivalente a `length`, mas tenos que usar outro nome para não conflitar)

``` prolog
tamanho([], 0).
tamanho([_|T], N) :-
  tamanho(T, X),
  N is X + 1.
```


```
?- trace.
true.

[trace]  ?- tamanho([a,b,c],X).
   Call: (8) tamanho([a, b, c], _3896) ? creep
   Call: (9) tamanho([b, c], _4138) ? creep
   Call: (10) tamanho([c], _4138) ? creep
   Call: (11) tamanho([], _4138) ? creep
   Exit: (11) tamanho([], 0) ? creep
   Call: (11) _4142 is 0+1 ? creep
   Exit: (11) 1 is 0+1 ? creep
   Exit: (10) tamanho([c], 1) ? creep
   Call: (10) _4148 is 1+1 ? creep
   Exit: (10) 2 is 1+1 ? creep
   Exit: (9) tamanho([b, c], 2) ? creep
   Call: (9) _3896 is 2+1 ? creep
   Exit: (9) 3 is 2+1 ? creep
   Exit: (8) tamanho([a, b, c], 3) ? creep
X = 3.
```

### Somatório de elementos da lista

Predicado recursivo para encontrar o somatório de elementos de uma lista

> Faça o passo-a-passo no SWI-Prolog!

Primeira versão: 2 predicados com variável L

``` prolog
sumv1(L,S) :-
  L = [],
  S = 0.

sumv1(L, S) :-
  L = [H|T],
  sumv1(T, S1),
  S is H + S1.
```

Segunda versão: predicados que facilitam a unificação com listas (sintaxe [H|T])

```
sumv2([],0).

sumv2([H|T], S) :-
   sumv2(T, S1),
   S is H + S1.   
```   


## Prática

Prática `prolog03` no Repl.it:

- Faça login em sua conta no Repl.it (a mesma usada em todas as outras práticas)
- Acesse o menu Teams e clique na prática `prolog03`
- Não há nada a ser entregue (Submit) no Repl.it (a participação será verificada pela criação do seu Repl.it desta prática, quando você clicar em "Start working")

### 01-simpsons-flintstones.pl

1. Analise o código e responda sem executá-lo: qual das consultas a seguir retorna as filhas de `marge`?

   - a) `daughter(marge,X).`
   - b) `daughter(X,marge).`

2. Execute passo-a-passo as consultas. Em qual delas ocorre backtracking?

### 02-students.pl

1. Que consulta você pode executar para descobrir a idade de cada um dos estudantes de `biology`?

2. Por que esta consulta vai dar erro: `mean_age(math,X)`?

3. O erro acima pode ser corrigido se você acrescentar uma linha ao predicado `mean_age`. Qual seria ela?


### 03-list-members.pl

1. Qual dos predicados retornará sempre `false`? Por quê?

2. Explique a diferença na execução passo-a-passo destas consultas:

   - a) `example1(S).` e
   - b) `example1(S),!.`

### 04-list-recursion.pl

1. Compare a execução passo-a-passo de `sumv1([1,2,3],L)` e `sumv2([1,2,3],L)`. O que você observa de semelhante/diferente?

2. Por que a consulta `tamanho(ab,L).` retorna `false` ?

### 05-factorial.pl


1. Execute a consulta `factv1(3,N).` e use `;` após a resposta. Isso deve gerar um erro. Você consegue identificar o motivo deste erro?

2. Por que este erro não acontece na consulta `factv2(3,N).`?

3. Você consegue identificar alguma vantagem de `factv2` sobre `factv3`?


## Bibliografia


- Prolog Instantiation and Backtracking: https://www.youtube.com/watch?v=AmWf6SeFmqc

  - Vídeo de ~5min em inglês britânico.
  - Assista mesmo que seu inglês não seja lá essas coisas :-)
  - Você pode ativar subtítulos / tradução automática.
  - Grandes chances de você entender bem, agora que sabe mais sobre Prolog!




-  Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulo 16: Programação Lógica)
- Patrick Blackburn, Johan Bos, and Kristina Striegnitz. [Learn Prolog Now](http://www.learnprolognow.org).
- Markus Triska. [The Power of Prolog](https://www.metalevel.at/prolog).
