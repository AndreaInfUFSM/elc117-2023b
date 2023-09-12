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
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/10/README.md
-->

# Programação Lógica




> Este material é uma introdução ao paradigma de **programação lógica** em linguagem Prolog.
>
> O conteúdo tem partes interativas e pode ser visualizado de vários modos usando as opções no topo da página.


## Um problema de lógica

Você sabe responder isso?


> Para comemorar o aniversário de Cíntia, ela e mais quatro amigas - Alice, Bia, Dirce e Eunice - foram almoçar juntas no RU. As mesas são redondas e acomodam exatamente 5 pessoas. Cíntia e Dirce sentam-se uma ao lado da outra. Alice e Bia não sentam-se uma ao lado da outra. (Fonte: Olimpíada Brasileira de Informática)

As duas amigas sentadas ao lado de Eunice são:

   - [( )] Cíntia e Alice
   - [( )] Cíntia e Dirce
   - [(x)] Alice e Bia
   - [( )] Dirce e Bia
   - [( )] Alice e Dirce



> Você saberia fazer um programa para encontrar a resposta?




### Solução em Prolog

> Para comemorar o aniversário de Cíntia, ela e mais quatro amigas - Alice, Bia, Dirce e Eunice - foram almoçar juntas no RU. As mesas são redondas e acomodam exatamente 5 pessoas. Cíntia e Dirce sentam-se uma ao lado da outra. Alice e Bia não sentam-se uma ao lado da outra. (Fonte: Olimpíada Brasileira de Informática)

Código:

```prolog
solucao(X,Y) :-
    A = [alice,bia,cintia,dirce,eunice],
    permutation(A,L),
    aolado(cintia,dirce,L),
    not(aolado(alice,bia,L)),
    aolado(X,eunice,L),
    aolado(Y,eunice,L),
    not(X=Y), !.

aolado(X,Y,L) :- nextto(X,Y,L); nextto(Y,X,L).
aolado(X,Y,L) :- naspontas(X,Y,L).

naspontas(X,Y,L) :- L = [X|_], last(L,Y).
naspontas(X,Y,L) :- L = [Y|_], last(L,X).
```

Execução:

```prolog
user@localhost:~$ swipl mesas.pl 

?- solucao(cintia,alice).
false.

?- solucao(cintia,dirce).
false.

?- solucao(alice,bia).
true.

?- solucao(dirce, bia).
false.

?- solucao(alice,dirce).
false.
```

## Programação lógica

Características: 

- É declarativa (assim como a funcional): expressa o que se quer obter, não como obter 
- Baseada em lógica matemática
- Programas são compostos por cláusulas que permitem deduções
- Prolog é a principal linguagem (propósito geral)

Deduções?!

- Obtenção de informações que não estão explícitas... 
- Exemplo:

  - Diana Prince é estudante de Paradigmas. 
  - Clark Kent é estudante de Paradigmas.
  - Todo estudante de Paradigmas é inteligente. 
  - Logo, Diana Prince e Clark Kent são inteligentes.

### Aplicações

- Primeiros passos em Inteligência Artificial
- Sistemas especialistas, bancos de dados inteligentes, processamento de linguagem natural, chatbots...
- Exemplo:

  - Um produto: https://www.ibm.com/watson
  - Bastidores: https://www.cs.nmsu.edu/ALP/2011/03/natural-language-processing-with-prolog-in-the-ibm-watson-system/

### Origens

- Início da década de 70 (assim como a linguagem C)
- Pesquisas em processamento de linguagem natural: https://dl.acm.org/doi/10.1145/234286.1057820
- Motivações persistem até hoje (por exemplo: ["Alexa, How Can I Reason with Prolog?"](https://drops.dagstuhl.de/opus/volltexte/2019/10884/pdf/OASIcs-SLATE-2019-17.pdf))
- Ver árvore genealógica das linguagens: https://www.digibarn.com/collections/posters/tongues/tongues.jpg



### Prolog

- Várias implementações e "dialetos" 
- Padronização [ISO/IEC](https://www.iso.org/standard/21413.html)
- Principais compiladores/interpretadores: SWI Prolog, GNU Prolog, Tau Prolog
- SWI-Prolog (recomendado): https://www.swi-prolog.org/

  - tem ambiente online (SWISH): https://swish.swi-prolog.org/
  - funciona também no Repl.it



## Prolog

Vamos ver alguns recursos da linguagem na sequência.

Usaremos o SWI-Prolog online no Repl.it.


### Prolog no Repl.it

> Podemos executar Prolog no Repl.it!


Instruções:

- Clique no botão Run e aguarde aparecer o prompt do shell no Linux
- Digite `swipl main.pl` para abrir o programa `main.pl` no interpretador de comandos do SWI-Prolog
- No prompt do SWI-Prolog, digite consultas conforme os exemplos das próximas seções. Por exemplo:

  ```
  ?- inteligente(diana).
  ```

Repl.it: 

 - Link público (fora do Teams): https://replit.com/@AndreaSchwertne/swi-prolog
 - Execução: botão Run (não requer login)
 - Edição: botão Fork (requer login)





### Basics

- Um programa em Prolog é composto por definições de **predicados** (verificáveis true ou false)
- Predicados expressam propriedades ou relações entre objetos
- Definições de predicados por meio de 2 tipos de cláusulas: **fatos** ou **regras** 
- Cláusulas são terminadas por um **ponto final** (`.`) (Prolog não usa chaves ou indent para delimitação)
- Execução do programa é uma **consulta**

| Conceito | Exemplo | Prolog |
| -------- | ------- | ------ |
| Fato | "Diana Prince é estudante de Paradigmas" | `paradigmer(diana).` |
| Regra | "Todo estudante de Paradigmas é inteligente" | `inteligente(X) :- paradigmer(X).` |
| Consulta | "Diana Prince é inteligente?" | `?- inteligente(diana).` |


### Predicados

- Predicados são a base das instruções em Prolog
- Forma geral: `nomedopredicado(arg1, arg2, ...)`
- Predicados se diferenciam pelo nome (case-sensitive) e quantidade de argumentos
- Nome de predicado usado em **fatos**, **regras**, **consultas**
- Argumentos não têm tipo explícito e podem ser **constantes** ou **variáveis**
- Constantes são valores simbólicos: inicial minúscula (ex.: `joaozinho`), números (ex.: `9`, `-8`, `22.3`), strings (ex.: `"ABC"`), listas (`[a,b]`)
- Variáveis **iniciam por maiúscula** e servem para estabelecer relações e receber valores durante a busca de uma solução

Aqui temos 2 predicados:

``` prolog
paradigmer(diana).
inteligente(X) :- paradigmer(X).
```
``` prolog
?- inteligente(diana).
```

### Fatos

- Fatos expressam verdades **incondicionais**.
- São cláusulas com apenas um predicado, uma parte (ao contrário das regras que têm 2 partes)
- Sintaxe: sempre terminam com ponto final (`.`)

```Prolog
idade(diana, 5000).
idade(clark, 22).
paradigmer(diana).
paradigmer(clark).
nacionalidade(clark, krypton).
nacionalidade(diana, themyscira).
mae(diana, hippolyta).
planeta(krypton).
```
### Consultas

- Usam **predicados** definidos por meio de fatos e regras
- Buscam resposta, que pode ser `true`/`false` ou valor para variável
- Sintaxe: sempre terminam com ponto final (`.`)

```Prolog
?- paradigmer(diana).
true
?- paradigmer(outronome).
false
```

#### Com variáveis

- Consultas podem ter variáveis
- Se houver valor que satisfaça a consulta, ele será vinculado à variável

```Prolog
?- planeta(X).
X = krypton
?- idade(diana,I).
I = 5000
```


#### Buscando mais respostas

- Ponto-e-vírgula (`;`) serve para buscar outra resposta no interpretador
- Significa "ou" e pode ser usado também em regras


```Prolog
?- paradigmer(X).
X = diana ;
X = clark
```




### Regras

- São cláusulas com **condicionais**.
- Forma geral: `<consequente> :- <condição>.` 

  - do lado direito uma condição
  - do lado esquerdo, o que pode ser deduzido caso a condição se verifique
- Sintaxe: sempre terminam com ponto final (`.`)


Condições simples

```Prolog
ave(X) :- papagaio(X).
```

Condições compostas 

```Prolog
idoso(X) :- idade(X, I), I >= 65.
alto(X) :- altura(X,A), A > 170.
```

#### Regras com "E" lógico

Usamos vírgula (`,`) para expressar "E" lógico.

Exemplo (em português):

> Se X é mãe de Y e Y é mãe de Z, então podemos deduzir que X é avó de Z (aqui não estamos considerando as outras possibilidades).

Em Prolog:

```Prolog
avo(X,Z) :- mae(X,Y), mae(Y,Z). 
```



#### Regras com "OU" lógico

Usamos ponto-e-vírgula (`;`) para expressar "OU" lógico.

Exemplo (em português):

> Se X é um papagaio ou uma coruja, então X é uma ave.

Em Prolog:

```Prolog
ave(X) :- papagaio(X) ; coruja(X). 
```

Outra opção (muito usada) é expressar o "OU" na forma de definições alternativas para o predicado:

```Prolog
ave(X) :- papagaio(X).
ave(X) :- coruja(X).
```


### Operadores relacionais (com números)

Em regras ou consultas, podemos usar operadores relacionais, que comparam valores e resultam verdadeiro ou falso.


Exemplos de consultas com o predicado `idade` definido pelos fatos mais abaixo:


```Prolog
?- idade(N,X), X =< 30.
?- idade(N,X), X > 25, X < 30.
```

```Prolog
idade(pedro,35).
idade(ana,30).
idade(paulo,27).
```

Se estivermos trabalhando com **números**, podemos usar estes operadores:

| Operador | Operação |
| -------- | -------- |
| `>` | maior |
| `<` | menor |
| `>=` | maior ou igual |
| `=<` | menor ou igual | 
| `=:=` | igual (numérico) |
| `=\=` | diferente (numérico) |



### Aritmética

- Aritmética em Prolog usa o operador `is` (não use o símbolo `=` para isso!)
- Esse operador faz com que as expressões aritméticas sejam avaliadas pelo interpretador
- Sem esse operador, as expressões aritméticas são tratadas como símbolos agrupados, sem execução de cálculos

```Prolog
soma(A,B,C) :- C is A + B.
```

```Prolog
?- soma(1, 4, C).
C = 5
```

> Observe que regras em Prolog se assemelham a procedimentos, não a funções. O resultado do predicado `soma` estará na variável C.


### Regras com aritmética

```Prolog
raio(chafariz, 5).
raio(piscina, 3).

area(X,A) :- raio(X,R), A is pi*R^2.
```

```Prolog
?- area(piscina,A).
A = 28.274333882308138.

?- area(chafariz,A).
A = 78.53981633974483.

?- area(roda, A).
false.
```


## Curiosidades

Na sequência, dois exemplos clássicos com Prolog.

### Prolog e Árvores Genealógicas

Um exercício clássico em Prolog:

- Fatos e regras representando árvores genealógicas: https://www.101computing.net/prolog-family-tree/


Atualização para novos contextos:

- Árvore genealógica de Game of Thrones: https://www.freecodecamp.org/news/how-to-learn-prolog-by-watching-game-of-thrones-4852ea960017/

- Árvore genealógica de deuses gregos: https://github.com/elc117/t2-2022a-eduardo_gilson


### Eliza chatbot

- Uma primeira experiência com processamento de linguagem natural
- Chatbot "terapeuta"
- Na Wikipedia: https://en.wikipedia.org/wiki/ELIZA
- Em Prolog: https://swish.swi-prolog.org/example/eliza.pl

## Bibliografia


- Patrick Blackburn, Johan Bos, and Kristina Striegnitz. [Learn Prolog Now](http://www.learnprolognow.org).
- Markus Triska. [The Power of Prolog](https://www.metalevel.at/prolog).

