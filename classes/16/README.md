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




# Programação Orientada a Objetos




> Este material é parte de uma introdução ao paradigma de **programação orientada a objetos** em linguagem Java.




## Histórico e motivações


- Origens: linguagens Simula (1960s) e Smalltalk (1970s)
- Programação imperativa, evolução da programação procedimental estruturada 
- Conjunto de conceitos/recursos de programação para lidar com a complexidade crescente dos programas

## Procedural X OO

- Linguagens procedurais

  - código organizado em subprogramas (módulos, procedimentos, funções)
  - dados entram/saem dos subprogramas

- Linguagens orientadas a objetos

  - objetos encapsulam código e dados 
  - objetos interagem (um chama outro, um se relaciona com outro, etc.)

## Terminologia

- A programação orientada a objetos compreende uma vasta terminologia técnica
- Alguns termos para você ir se acostumando: 

  - classe, objeto, instância
  - construtor
  - atributo e método
  - estado e comportamento
  - abstração
  - encapsulamento
  - herança
  - polimorfismo




## Linguagens

- Muitas linguagens relevantes atualmente possuem suporte a orientação a objetos
- Por exemplo: C++, Java, C#, PHP, Python, JavaScript, ...
- Veja alguns rankings de linguagens

  - TIOBE Index

    - Critério: search engines
    - https://www.tiobe.com/tiobe-index/

  - RedMonk

    - Critério: GitHub + StackOverflow 
    - https://redmonk.com/

  - Most Popular Programming Languages 1965 - 2022

    - Compilação de várias fontes em um gráfico animado
    - https://www.youtube.com/watch?v=qQXXI5QFUfw



## Java

- Linguagem criada em 1995 (Sun Microsystems, James Gosling)
- Implementação híbrida: compilador + interpretador (JVM)
- Multiparadigma, mas principalmente orientada a objetos
- Faz parte da família de linguagens derivadas do C
- Histórico de versões: https://en.wikipedia.org/wiki/Java_version_history



### Ambiente de execução

- Java Development Kit (JDK): compilador, interpretador e bibliotecas
- Java Runtime Environment (JRE): apenas para execução
- Várias implementações: Oracle, OpenJDK, Azul, etc.
- Gerenciadores de projetos/dependências: Maven, Gradle, etc.
- Em nuvem: Repl.it é uma boa opção


### Exemplo clássico

``` java  +HelloWorld.java
class HelloWorld {
  public static void main(String[] args) {
    System.out.println("Hello, World!"); 
  }
}
```

Compilar:

```
javac HelloWorld.java
```

Executar (JVM):

```
java HelloWorld
```

> IDEs escondem estas etapas em um único botão/atalho, mas elas continuam existindo!

## Atividade em duplas

Esta atividade será realizada em aula, seguindo estes passos:

1. Acompanhe o sorteio de duplas e junte-se a(o) colega sorteado
2. Acesse o projeto `java01` no Repl.it
3. Execute o programa conforme orientações no código `Main.java`
4. Responda questões: 

   1. A questão associada ao número da sua dupla
   2. Uma questão à sua escolha
5. Apresente sua resposta para a turma


> Esta atividade não precisa ser entregue (Submit) no Repl.it. Basta realizá-la durante a aula.

### Questões

Todas as questões se referem ao programa `Main.java` no projeto `java01` no Repl.it:

1. Identifique 3 (ou mais) semelhanças entre Java e C presentes no código.
2. Identifique 3 (ou mais) diferenças entre Java e C presentes no código.
3. O que fazem os comandos `javac Main.java` e `java Main`?
4. O que significa `students.size()`? Qual seu valor?
5. O que significa `student.hasOOPxp()`?
6. O que significa `groupA.add(student)`?
7. O que significa `largest.remove(0)`?
8. O que significa `smallest.isEmpty()`?
9. O que significa `student1.getName()`?
10. O que significa `line.split(",")`?
11. O código manipula dados de estudantes da turma. Que dados caracterizam cada estudante?
12. Identifique um tipo de objeto definido no código.
13. Identifique a criação de um objeto no código.
14. Em Java, strings são objetos da classe String. Identifique no código algumas operações que podemos fazer com strings.
15. O que significa `private` em alguns pontos do código?
16. O que significa `public` em alguns pontos do código?
17. Substitua uma das ocorrências de `student1.getName()` por `student1.name`. Explique o que acontece na compilação do código.
18. Se substituirmos `class Main` por `class StudentPairs`, o código irá compilar sem erro, mas não poderá ser executado como antes. Por quê? Como executá-lo?



## Bibliografia


Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulos 11 e 12)
