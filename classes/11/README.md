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


# Prática de Prolog


Prática `prolog01` no Repl.it:

- Faça login em sua conta no Repl.it (a mesma usada em todas as outras práticas)
- Acesse o menu Teams e inicie a prática `prolog01`
- Faça o aquecimento antes de fazer os exercícios para entrega


## Para aquecimento

As 2 seções a seguir têm  instruções de aquecimento. 

Não é necessário entregar nada nesta parte, mas os exercícios da próxima parte dependem deste aquecimento.




### Programa `movies.pl`

Este código em Prolog declara os seguintes predicados:

- `movie/2`: fatos na forma `movie(MovieName,Year)`
- `actor/2`: fatos na forma `actor(MovieName, ActorName)`
- `actress/2`: fatos na forma `actress(MovieName, ActressName)`
- `genre/2`: fatos na forma `genre(MovieName, Genre)`
- `likes/2`: fatos na forma `likes(UserName, MovieName)`
- `user/1`: fatos na forma `user(UserName)`
- `drama_actor/1`: regra estabelecendo que `A` é um ator de drama se `A` for ator do filme `M` e este filme for do gênero drama.

     
### Consultas básicas


- No Shell do Repl.it, digite `swipl movies.pl` para abrir o programa `movies.pl` no interpretador de comandos do SWI-Prolog
- No prompt do SWI-Prolog (`?-`), você vai digitar consultas que vão retornar um resultado para uma pergunta. Por exemplo: "Em que ano foi lançado o filme Precious?"

  ``` prolog
  ?- movie(precious,A).
  A = 2009.
  ```

Teste as seguintes consultas:


1. Quais os filmes lançados no ano de 2001?

   `?- movie(M, 2001).`

2. Quais os filmes lançados nesta década? (digite `;` após a primeira resposta para buscar mais respostas)
   
   `?- movie(M, A), A >= 2020.`

3. Quais os filmes lançados no século passado? 
   
   `?- movie(M, A), A =< 2000.`


### Comandos no `swipl`

- Para sair do interpretador: `Ctrl-D` ou `halt.`
- Para (re-)carregar o programa `movies.pl` quando você estiver dentro do interpretador: `[movies].` (sem extensão .pl, e sem esquecer do ponto!)
- Para ativar o modo de execução passo-a-passo: `trace.`
- Para desativar o modo de execução passo-a-passo: `nodebug.`

### Por dentro da execução

Ative o modo de execução passo-a-passo para ver como o interpretador Prolog faz uma busca na base de fatos e regras:

```
?- trace.
true.
```

Execute a consulta `actor(shallow_hal,A)`. 

Digite `Enter` para avançar e `;` depois da primeira resposta, para buscar outras respostas.

```
[trace]  ?- actor(shallow_hal,A).
   Call: (10) actor(shallow_hal, _15438) ? creep
   Exit: (10) actor(shallow_hal, jack_black) ? creep
A = jack_black ;
   Redo: (10) actor(shallow_hal, _15438) ? creep
   Exit: (10) actor(shallow_hal, jason_alexander) ? creep
A = jason_alexander.
```

Desative o modo de execução passo-a-passo:

```
[trace]  ?- nodebug.
true.
```

## Para entregar

A entrega dos exercícios a seguir será em 2 arquivos na prática `prolog01` no Repl.it: `Consultas.md` e `movies.pl`. 

### Consultas em `Consultas.md`

Neste arquivo, escreva as seguintes consultas em Prolog:


1. Quais os atores do filme `fight_club`? 

2. O filme `interstellar` é uma comédia?

3. Quais os filmes lançados na década de 80 (entre 1981 e 1990, inclusive)?

4. Quais os atores ou atrizes do filme `black_widow`?

5. O ator `brad_pitt` é um ator de drama? 

6. Há quantos anos foi lançado o filme `the_avengers`? Consulte o material da aula passada para saber como fazer operações aritméticas em Prolog.

7. Em português, escreva uma pergunta que possa ser respondida com os predicados do programa `movies.pl`. Responda a pergunta com uma consulta em Prolog.

8. O ator `chris_evans` é um ator de comédia?



### Regras em `movies.pl`

No final do arquivo `movies.pl`:

1. Defina o predicado `drama_artist(A)`, que será verdadeiro se `A` for ator ou atriz em um filme de drama. 

2. Defina o predicado `movieaged(M, Y)`, em que `Y` será a idade (em anos) do filme `M`, no ano atual. 

3. Defina o predicado `recommend(U,M)`, para recomendar um filme `M` a um usuário `U`. Este predicado será verdadeiro se for encontrado um filme do mesmo gênero de um filme apreciado (`like`)pelo usuário `U`.


**Atenção!** Teste todos os predicados que você definir, observando se o resultado é igual ao esperado.



## Extras

Terminou tudo? A seguir você encontra algumas atividades extras, que não precisam ser entregues, mas que seria muuuuiiito bom você fazer.

### Listas em `movies.pl`

Prolog suporta listas nativamente.  No programa `movies.pl`, há alguns predicados que manipulam listas.


Teste as seguintes consultas:


1. Quais os gêneros de filmes na base de dados? A resposta será uma lista na variável G.

   `?- allgenres(G).`

3. Qual o primeiro gênero na lista? A resposta estará na variável H.

   `?- allgenres(G), head(G, H).`

2. Quantos são os gêneros de filmes na base de dados?
   
   `?- countgenres(C).`


Mesmo tendo apenas um primeiro contato com Prolog, você consegue criar um predicado para contar o número de usuários na base de dados?


### Miscelânea

- Acesse a playlist de Paradigmas no Spotify :-) https://open.spotify.com/playlist/0DQZZAkKbA7lL8dzOUuPPZ
- No Repl.it prolog01, o programa `songs.pl` tem dados sobre as músicas da playlist. Será que você consegue consultá-los em Prolog?
- No Repl.it prolog01, o programa `scifi-story.pl` gera enredos de filmes de ficção científica :-) Você consegue descobrir como executá-lo?


