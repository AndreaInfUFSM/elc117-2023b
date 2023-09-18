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
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/11/README.md
-->




# Programação Lógica




> Este material é uma introdução ao paradigma de **programação lógica** em linguagem Prolog.
>
> O conteúdo tem partes interativas e pode ser visualizado de vários modos usando as opções no topo da página.


## Quizzes sobre a prática

- Primeira parte: [consultas](#primeira-parte-consultas)
- Segunda parte: [regras](#segunda-parte-regras)

### Primeira parte: consultas

A primeira parte da prática tinha 8 questões. A seguir temos questões sobre 3 delas.

                 {{1}}
************************************************

Qual das alternativas abaixo responde a pergunta: "Quais os filmes lançados na década de 80?"

- [( )] `?- movie(M,A), A >= 1981, A <= 1990.`
- [( )] `?- movie(M,A), A >= 1981, A =< 1990`
- [(x)] `?- movie(M,A), A >= 1981, A =< 1990.`
- [( )] `?- movie(M,A), A >= 1981, A <= 1990`
***********************

Prolog usa o operador relacional `=<` para verificar se um valor é igual ou menor que outro. Além disso, as cláusulas devem terminar com um `.`

Exemplo de resultado:

```
?- movie(M,A), A >= 1981, A =< 1990.
M = dead_poets_society,
A = 1989 ;
M = my_neighbor_totoro,
A = 1988 ;
false.
```

***********************


************************************************


                 {{2}}
************************************************

Qual das alternativas abaixo **não** retorna nomes que respondam à pergunta: "Quais os atores ou atrizes do filme `black_widow`"?

- [(x)] `?- actor(black_widow,A), actress(black_widow,A).`
- [( )] `?- actor(black_widow,Actor); actress(black_widow,Actress).`
- [( )] `?- actor(black_widow,A); actress(black_widow,A).`
- [( )] `?- actor(black_widow,A); actress(black_widow,B).`
***********************

Prolog usa `;` para expressar um "ou" lógico.

Exemplo de resultado:

```
?- actor(black_widow,Actor);actress(black_widow,Actress).
Actor = david_harbour ;
Actress = scarlett_johansson ;
Actress = florence_pugh.
```

***********************

************************************************



                 {{3}}
************************************************

A consulta abaixo responde à pergunta: "Há quantos anos foi lançado o filme `the_avengers`"?

```
?- movie(the_avengers), Idade is 2023 - A.
```

- [( )] Sim
- [(x)] Não
***********************

Não, porque o predicado `movie` tem 2 argumentos, mas nesse exemplo só foi passado um. Além disso, a variável `A` não poderá ser deduzida.

A seguir uma consulta correta para responder à pergunta:

```
?- movie(the_avengers,A), Idade is 2023 - A.
A = 2012,
Idade = 11.
```

***********************

************************************************

### Segunda parte: regras

A primeira parte da prática tinha 3 questões. A seguir temos questões sobre 2 delas.


                 {{1}}
************************************************

Qual das opções abaixo **não** define corretamente o predicado `drama_artist(A)`, que será verdadeiro se `A` for ator ou atriz em um filme de drama. 

- [( )] `drama_artist(A) :- (actor(M, A); actress(M,A)), genre(M, drama).`
- [( )] `drama_artist(A) :- actor(M, A); actress(M,A), genre(M, drama).`
- [(x)] `drama_artist(A) :- genre(M, drama), actor(M, A), actress(M,A).`
- [( )] `drama_artist(A) :- genre(M, drama), (actor(M, A); actress(M,A)).`
- [( )] `drama_artist(A) :- genre(M, drama), actor(M, A); actress(M,A).` 
***********************

Este predicado precisa de um "ou" (`;`) entre os predicados `actor` e `actress`.

***********************

************************************************




                 {{2}}
************************************************

Complete abaixo o predicado `recommend(U,M)`, para recomendar um filme `M` a um usuário `U`. 

Este predicado será verdadeiro se for encontrado um filme do mesmo gênero de um filme apreciado (`like`)pelo usuário `U`.

Sua resposta deve ser preenchida no campo marcado com "?".

`recommend(U,M) :- likes(U,A), genre(A,G), movie(M,_), genre(M,` [[ G ]] `), not(A=M).`
***********************

Este predicado:

1. busca um filme `A` apreciado pelo usuário `U` (`likes(U,A)`)
2. obtém o gênero `G` do filme `A` (`genre(A,G)`)
3. busca um filme `M`, desprezando sua data de lançamento (`movie(M,_)`)
4. verifica se o gênero de `M` é `G`, ou seja, igual ao gênero de `A` (`genre(M,G)`)
5. impõe a condição `not(A=M)`, para garantir que a recomendação seja de um filme diferente daquele que já consta como apreciado pelo usuário

***********************

************************************************






## Unificação

- Unificação é um conceito fundamental em Prolog, que: 

  - verifica se 2 termos são compatíveis/unificáveis
  - atribui valores a variáveis buscando tornar os termos idênticos

- Unificação ocorre "por trás" da execução de um programa e também explicitamente com `=`

### Operador `=`

- Não tem o mesmo sentido do `=` usado nas linguagens imperativas
- Usado para provocar unificação, geralmente com variáveis
- Resultado pode ser `true`, `false` ou valores atribuídos a variáveis 

### Variável anônima (`_`)

Mais sobre variáveis em Prolog

- Iniciam por maiúsculas, p.ex. `X`, `Solucao`, etc.
- Também podem iniciar por `_` (underline, sublinhado), p.ex. `_casas`, `_X` (sintaxe pouco usada)

Variável pode ser "anônima" (sem nome)

- Apenas o símbolo `_`
- Usado como "placeholder", quando não nos interessa o valor, só a posição

### Exemplos

```
?- A = 1.
A = 1.

?- B = A + 1.
B = A+1.

?- B is A + 1.
ERROR: Arguments are not sufficiently instantiated
ERROR: In:
ERROR:    [8] _4054 is _4060+1
ERROR:    [7] <user>

?- A = 1, B is A + 1.
A = 1,
B = 2.

?- A = 1, A = A + 1.
false.

?- A = [1,2], length(A,L).
A = [1, 2],
L = 2.

?- A = [1,2,X], X is 2+1.
A = [1, 2, 3],
X = 3.

?- casa(_, azul, _) = casa(bob, _, gato).
true.

?- casa(_, azul, _) = casa(bob, azul, gato).
true.

?- casa(_, azul, _) = casa(bob, X, _), X = verde.
false.

?- casa(_, azul, _) = casa(bob, azul, _, _).
false.
```

## Listas

- Suportadas nativamente em Prolog
- Representam sequências finitas de elementos
- Podem ser homogêneas ou heterogêneas (elementos de diversos tipos: constantes, variáveis, predicados)
- Sintaxe básica: delimitação com colchetes 

  - Com elementos separados por vírgula, p.ex.:

    ``` prolog
    [elem1, elem2, elem3]
    [drama, comedy, scifi, adventure]
    [casa(1,azul),casa(2,verde)]
    [1,a,2,b]
    [A, 2]
    ```
  - Com variáveis e `|` para separação entre head e tail, p.ex.:

    ``` prolog
    [H | [b,c,d]]
    [1 | T]
    [H | [b,c,d]]
    ```



### Predicados com listas

- Lembre que predicados em Prolog se assemelham a procedimentos (não a funções)
- Se quisermos que "retornem" algum valor, devemos usar variáveis nos argumentos

#### `length/2`

- Predicado usado para obter/verificar o tamanho (número de elementos) de uma lista
- Sintaxe: `length(List,Length)`, onde `Length` é o tamanho da lista `List`
- Documentação: https://www.swi-prolog.org/pldoc/man?predicate=length/2
- Exemplos executados no interpretador SWI-Prolog:

  ```
  ?- length([a,b,c],Len).
  Len = 3.
  
  ?- length([movie(the_avengers)],Len).
  Len = 1.

  ?- length([movie(the_avengers)],2).
  false.

  ?- length(["abc"],Len).
  Len = 1.

  ?- length(["abc","def"],2).
  true.

  ```


#### `member/2`

- Predicado usado para verificar a existência de um elemento na lista
- Sintaxe: `member(Elem,List)`, onde `Elem` é um elemento da lista `List`
- Documentação: https://www.swi-prolog.org/pldoc/man?predicate=member/2
- Exemplos executados no interpretador SWI-Prolog:

  ```
  ?- member(a, [a,b,c,d]).
  true.

  ?- member(x, [a,b,c,d]).
  false.

  ?- member(E,[a,b,c,d]).
  E = a ;
  E = b ;
  E = c ;
  E = d.

  ```

#### `nextto/3`

- Predicado usado para verificar se 2 elementos são consecutivos
- Sintaxe: `nextto(X, Y, List)`, verdadeiro se `X` antecede `Y` na lista `List`
- Documentação: https://www.swi-prolog.org/pldoc/man?predicate=nextto/3
- Exemplos executados no interpretador SWI-Prolog:

  ```
  ?- nextto(b,c,[a,b,c,d]).
  true .

  ?- nextto(b,X,[a,b,c,d]).
  X = c ;
  false.

  ?- nextto(b,X,[a,b,1,a,b,2]).
  X = 1 ;
  X = 2 ;
  false.
  ```


#### `permutation/2`

- Predicado usado para gerar permutações de uma lista
- Sintaxe: `permutation(Xs, Ys)`, onde `Xs` é uma permutação de `Ys`
- Documentação: https://www.swi-prolog.org/pldoc/doc_for?object=permutation/2
- Exemplos executados no interpretador SWI-Prolog:

  ```
  ?- permutation([a,b,c],[b,c,a]).
  true .

  ?- permutation([a,b,c],L).
  L = [a, b, c] ;
  L = [a, c, b] ;
  L = [b, a, c] ;
  L = [b, c, a] ;
  L = [c, a, b] ;
  L = [c, b, a] ;
  false.

  ?- permutation(L,[a,b,c]).
  L = [a, b, c] ;
  L = [a, c, b] ;
  L = [b, a, c] ;
  L = [c, a, b] ;
  L = [b, c, a] ;
  L = [c, b, a] ;
  false.
  ```

#### `findall/3`


- Predicado usado para gerar uma lista com valores de uma variável em uma consulta
- Veja mais em: https://www.educba.com/prolog-findall/
- Exemplo: suponha esta base

  ```
  age(edgard, 23).
  age(edward, 25).
  ```
- Consulta no interpretador SWI-Prolog:

  ```
  ?- findall(A, age(_,A), L).
  L = [23, 25].
  ```



#### Outros predicados com listas

- Prolog tem vários outros predicados pré-definidos que manipulam listas
- Descubra-os na documentação: https://www.swi-prolog.org/pldoc/man?section=lists



### Exemplos: Logic Puzzles


Você já ouviu falar do "Enigma de Einstein" ou "Charada de Einstein"? 

É um problema de lógica que circula pela Internet há décadas, no estilo "clickbait", afirmando (sem nenhuma evidência) que foi criado por Albert Einstein e que só uma pequena parcela da população consegue resolvê-lo.

Imagine então quantas pessoas conseguem resolvê-lo em Prolog? :-)

A seguir veremos uma versão simplificada, mas você pode encontrar uma versão completa em: https://rachacuca.com.br/logica/problemas/teste-de-einstein/

#### Primeira versão



Problema:

- Existem 3 casas alinhadas, cada uma com uma cor diferente: vermelha, verde e azul.
- Em cada casa vive uma pessoa: Alice, Bob e Carla.
- Cada pessoa tem um pet: gato, cachorro, hamster.
- Bob vive na casa vermelha.
- A pessoa que tem um gato vive na casa do meio.
- Carla vive na casa ao lado da casa azul.
- A pessoa que vive na casa verde tem um cachorro.
- Qual a cor, o morador e o pet de cada casa?




                 {{1}}
************************************************

Solução em Prolog, usando listas:

``` prolog
ao_lado(X, Y, List) :- nextto(X, Y, List). % X à esquerda de Y
ao_lado(X, Y, List) :- nextto(Y, X, List). % Y à esquerda de X

solucao(Casas) :-
  Casas = [_,casa(_,_,gato),_],
  member(casa(_,verde,cachorro),Casas),
  member(casa(_,azul,_),Casas),
  member(casa(_,_,hamster),Casas),
  member(casa(alice,_,_),Casas),
  member(casa(bob,vermelha,_),Casas),
  member(casa(carla,_,_),Casas),
  ao_lado(casa(carla,_,_),casa(_,azul,_),Casas). 
```
************************************************


                 {{2}}
************************************************

Quantas soluções existem? Vejamos no SWI-Prolog:

```
?- solucao(X).
X = [casa(carla, verde, cachorro), casa(alice, azul, gato), casa(bob, vermelha, hamster)] ;
X = [casa(bob, vermelha, hamster), casa(alice, azul, gato), casa(carla, verde, cachorro)] ;
false.

?- findall(X, solucao(X), Solucoes), length(Solucoes, Len).
Solucoes = [[casa(carla, verde, cachorro), casa(alice, azul, gato), casa(bob, vermelha, hamster)], [casa(bob, vermelha, hamster), casa(alice, azul, gato), casa(carla, verde, cachorro)]],
Len = 2.
```

> Prolog nos ajuda a buscar alternativas que satisfaçam condições!

************************************************

#### Segunda versão



Problema com algumas condições modificadas:

- Existem 3 casas alinhadas, cada uma com uma cor diferente: vermelha, verde e azul.
- Em cada casa vive uma pessoa: Alice, Bob e Carla.
- Cada pessoa tem um pet: gato, cachorro, hamster.
- Bob vive na casa vermelha.
- A pessoa que tem um gato vive na casa do meio.
- Carla **tem um hamster** e vive na casa ao lado da casa azul.
- **A primeira casa é verde**.
- Qual a cor, o morador e o pet de cada casa?




                 {{1}}
************************************************

Solução em Prolog, usando listas:

``` prolog
ao_lado(X, Y, List) :- nextto(X, Y, List). % X à esquerda de Y
ao_lado(X, Y, List) :- nextto(Y, X, List). % Y à esquerda de X

solucao(Casas) :-
  Casas = [casa(_,verde,_),casa(_,_,gato),_],
  member(casa(_,_,cachorro),Casas),
  member(casa(_,azul,_),Casas),
  member(casa(_,_,hamster),Casas),
  member(casa(alice,_,_),Casas),
  member(casa(bob,vermelha,_),Casas),
  member(casa(carla,_,hamster),Casas),
  ao_lado(casa(carla,_,_),casa(_,azul,_),Casas).  
```
************************************************


                 {{2}}
************************************************

Quantas soluções existem? Vejamos no SWI-Prolog:

```
?- solucao(Casas).
Casas = [casa(carla, verde, hamster), casa(alice, azul, gato), casa(bob, vermelha, cachorro)] ;
false.

?- findall(X,solucao(X), Solucoes), length(Solucoes, Len).
Solucoes = [[casa(carla, verde, hamster), casa(alice, azul, gato), casa(bob, vermelha, cachorro)]],
Len = 1.
```

************************************************



### Representação `[Head|Tail]`

Sintaxe usada para representar uma lista **não-vazia** onde:

- `H` designa o primeiro elemento da lista, chamado de "cabeça" (head)
- `T` designa o restante da lista, chamado de "cauda" (head), que é outra lista (possivelmente vazia), excluindo o primeiro elemento

Exemplos:

- `[1, 2, 3]` equivale a `[1 | [2, 3]]`, sendo 1 a cabeça da lista e [2, 3] a cauda
- `[a, b, c]` equivale a `[H | T]` se H = a e T = [b,c]
- `[1, 2]` equivale a `[X | Y]` se X = 1 e Y = [2]
- `[movie(the_avengers)]` equivale a `[H | T]` se H = movie(the_avengers) e T = []

No interpretador SWI-Prolog:

```
?- A=[1|[2,3]].
A = [1, 2, 3].

?- [a,b,c]=[H|T].
H = a,
T = [b, c].

?- [a,b,c]=[H|T].
H = a,
T = [b, c].

?- [1,2]=[X|Y].
X = 1,
Y = [2].

?- [H|T]=[movie(the_avengers)].
H = movie(the_avengers),
T = [].
```

#### Usos de `[H|T]`


- Manipulação de listas: 

  - sintaxe `[H|T]` é usada para decompor listas e acessar seus elementos


- Construção de listas:

  - sintaxe `[H|T]`  também pode ser usada para construir listas
  - por exemplo, [1, 2, 3] pode ser construída como `L = [1|[2|[3|[]]]]`, onde `[]` representa uma lista vazia.


- Recursão: 

  - sintaxe `[H|T]` costuma ser usada em recursão
  - por exemplo, regras que obtêm `H` e aplicam a própria regra com `T` até atingir a lista vazia (`[]`).

- Unificação com padrões: 

  - sintaxe `[H|T]` pode ser usada para verificar se uma lista corresponde a um padrão específico



#### Regras com `[H|T]`

Vejamos alguns exemplos de regras que usam `[H|T]`.

                 {{1}}
************************************************

Regra que verifica se duas listas têm a mesma "head"

Definição:

``` prolog
hasEqHeads(L1, L2) :-
  L1 = [H1|_],
  L2 = [H2|_],
  H1 = H2.
```

Alternativa abreviada:

``` prolog
hasEqHeads([H|_], [H|_]).
```

Uso no SWI-Prolog:

``` prolog
?- hasEqHeads([1,2,3],[2,3,4]).
false.

?- hasEqHeads([a,2,3],[a,3,4]).
true.
```

************************************************


                 {{2}}
************************************************

Regra que gera lista com "head"s de outras 2 listas:


``` prolog
getHeads(L1, L2, L3) :-
  L1 = [H1|_],
  L2 = [H2|_],
  L3 = [H1, H2].
```

Alternativa abreviada:

``` prolog
getHeads([H1|_], [H2|_], [H1, H2]).
```


Uso no SWI-Prolog:

``` prolog
?- getHeads([1,2],[4,5],L).
L = [1,4].
```


************************************************

                 {{3}}
************************************************

Regra que gera nova lista com quadrado do "head" da primeira lista

``` prolog
doubleHead(L1,L2) :-
  L1 = [H|T],
  D is H*H,
  L2 = [D|T].
``` 

Alternativa abreviada:

``` prolog
doubleHead([H|T],[H*H|T]).
``` 


Uso no SWI-Prolog:

``` prolog
?- doubleHead([2,3,4],L).
L = [4,3,4].
```

************************************************


#### Recursão com `[H|T]`


                 {{1}}
************************************************

Somatório de elementos de uma lista

``` prolog
sum(L,S) :-
  L = [],
  S = 0.

sum(L, S) :-
   L = [H|T],
   sum(T, S1),
   S is H + S1.
```

Alternativa abreviada:

``` prolog
sum([],0).

sum([H|T], S) :-
   sum(T, S1),
   S is H + S1.
```


************************************************


                 {{2}}
************************************************

Quantidade de elementos de uma lista

``` prolog
tamanho([], 0).
tamanho([_|T], N) :-
  tamanho(T, X),
  N is X + 1.
```

Uso no SWI-Prolog:

```
?- tamanho([],T).
T = 0.

?- tamanho([1,2,3],T).
T = 3.

?- tamanho([1,2,3,a,b,c],T).
T = 6.

?- tamanho([a,b,c],T).
T = 3.

?- tamanho([a,b,c,1,2,3],T).
T = 6.
```


************************************************

                 {{3}}
************************************************

Último elemento de uma lista

- se lista tem um elemento: último da lista é H
- senão: último da lista é último de T

``` prolog
ultimo([H], H).
ultimo([_|T], U) :-
  ultimo(T, U).
```


************************************************

                 {{4}}
************************************************

Gera nova lista com todos elementos da primeira lista elevados ao quadrado


``` prolog
mapSquare([],[]).
mapSquare([H|T],L) :-
  D is H*H,
  L = [D | R],
  mapSquare(T,R).
```



************************************************

                 {{5}}
************************************************

Filtra elementos pares de uma lista 

``` prolog
even(N) :- 
  R is N mod 2,
  R = 0.
  
filterEven([],[]).
filterEven([H|T],L) :-
  even(H),  
  filterEven(T,Aux),
  L = [H|Aux].

filterEven([H|T],L) :-
  not(even(H)),  
  filterEven(T,L).
```


************************************************


## Prática

Prática `prolog02` no Repl.it:

- Faça login em sua conta no Repl.it (a mesma usada em todas as outras práticas)
- Acesse o menu Teams e clique na prática `prolog02`
- Nesta prática, você vai trabalhar com os arquivos `hotel-murder.pl` e `songs.pl`.


### `hotel-murder.pl`

No final de uma festa no oitavo andar do Hotel "Paradigm Palace", o milionário Sr. Dollars foi assassinado! Todas as pessoas presentes no final da festa foram imediatamente detidas como suspeitas: eram 2 homens (Bob e Dave) e 2 mulheres (Alice e Carol). Cada suspeito estava hospedado em uma das suítes do oitavo andar: 801, 802, 803 e 804. Em cada suíte, foi encontrada uma arma: Faca, Revólver, Corda e Veneno.

Para desvendar este crime, você precisa deduzir onde cada pessoa estava e qual arma foi encontrada em cada suíte. 

As pistas são as seguintes:

1. A pessoa com a Corda estava no quarto 803.
2. Alice estava com o Revólver.
3. Carol estava num quarto de número par.
4. Alice e Bob não estavam em quartos adjacentes.
5. Nenhum dos homens estava com o Veneno.
6. Dave estava com a Faca.
7. Uma das mulheres estava no último quarto.
8. Quem cometeu o assassinato estava hospedado no quarto 802.

> Quem matou o Sr. Dollars e qual arma foi usada?

Orientações:

- Você deve resolver este enigma no arquivo `hotel-murder.pl`.

- Você deve definir um predicado que expresse todas as pistas sob forma de condições.

- Você não deve fazer deduções "de cabeça" para simplificar o programa!



### `songs.pl`

Este código em Prolog declara os seguintes predicados:

- `song/2`: fatos na forma `song(SongName, Year)`
- `duration/3`: fatos na forma `duraction(SongName, Minutes, Seconds)`
- `artist/2`: fatos na forma `artist(SongName, ArtistName)`


No final do arquivo `songs.pl`:

1. Defina o predicado `duration_in_secs(SongName, S)`, de forma que `S` seja a duração da música `SongName` em segundos. Lembre de usar `is` para aritmética em Prolog.

2. Adicione o seguinte predicado para testar seu código:

```
playlist_duration(Time) :-
  findall(S, duration_in_secs(_, S), L), sum_list(L, Time). 
```

Se tudo estiver certo, você deverá poder executar a seguinte consulta:

```
?- playlist_duration(T).
T = 2500.
```

3. Adicione um predicado recursivo `filterShorts(L1, L2)`, que receba uma lista de números `L1` e produza `L2` contendo somente os números menores que `200`.

4. Adicione o seguinte predicado para testar seu código:

```
playlist_shorts(L) :- 
  findall(S, duration_in_secs(_, S), A), filterShorts(A, L).
```

Se tudo estiver certo, você deverá poder executar a seguinte consulta:

```
?- playlist_shorts(L).
L = [139, 158, 144] 
```





## Bibliografia


-  Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulo 16: Programação Lógica)
- Patrick Blackburn, Johan Bos, and Kristina Striegnitz. [Learn Prolog Now](http://www.learnprolognow.org).
- Markus Triska. [The Power of Prolog](https://www.metalevel.at/prolog).
