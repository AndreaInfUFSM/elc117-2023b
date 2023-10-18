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




> Este material faz parte de uma introdução ao paradigma de **programação orientada a objetos** em linguagem Java.

## Quizzes

As 10 questões abaixo têm correção automática:


                 {{1}}
************************************************

Os atributos e métodos de um objeto representam, respectivamente... 

[( )] seu comportamento e seu estado
[(x)] seu estado e seu comportamento

************************************************

                 {{2}}
************************************************

Em um sistema de e-commerce em Java, há uma classe 'Product' que contém uma String chamada 'sku' para representar o código de um produto e um float 'price' para representar o preço do produto.

Uma variável 'prod' é declarada como sendo da classe 'Product'. Qual das seguintes afirmações está correta?

[( )] sku e price são instâncias de Product
[( )] Product é uma instância de prod
[(x)] prod terá seu próprio valor de price
[( )] prod é um atributo de Product


************************************************

                 {{3}}
************************************************

Qual das opções abaixo é um construtor válido para uma classe 'Person'?


[( )] `public void Person() { }`
[(x)] `public Person(String name) { }`
[( )] `public initPerson() {}`
[( )] `void constructPerson() { }`


************************************************

                 {{4}}
************************************************

Verdadeiro ou falso? "Um método público pode acessar somente atributos públicos declarados na classe a que ele pertence."

[( )] Verdadeiro
[(x)] Falso


************************************************

                 {{5}}
************************************************

As declarações de métodos abaixo são válidas dentro de uma classe Person?

``` java
public void method(int a) { }

public void method(float b) { }
```

[(x)] Sim
[( )] Não


************************************************


                 {{6}}
************************************************

As declarações de métodos abaixo são válidas dentro de uma classe Person?

``` java
public int method(int a) { }

public void method(int a) { }
```

[( )] Sim
[(x)] Não


************************************************

                 {{7}}
************************************************

Considerando o código abaixo, qual das opções é válida para criar uma instância de Person?


``` java
public class Person {
  private String name;
  private int age;
  public Person(String n, int a) {
    name = n;
    age = a;
  }
}
```

[(x)] Person p = new Person("Maria", 18);
[( )] Object p = new Person("Maria", "18");
[( )] Person p = new Person();
[( )] Object p = new Person();


************************************************

                 {{8}}
************************************************

Quantos atributos estão declarados na classe Person? 

``` java
public class Person {
  private String name;
  private int age;
  public Person(String n, int a) {
    name = n;
    age = a;
  }
}
```

[[2]]

************************************************


                 {{9}}
************************************************

O código `Person p;` cria um objeto da classe Person em Java?


[( )] Sim
[(x)] Não




************************************************

                 {{10}}
************************************************

Para descontrair :-) Java é uma linguagem que suporta o paradigma de programação... 

[( )] orientado a dificuldades
[( )] orientado a gambiarras
[(x)] orientado a objetos
[( )] orientado a games

************************************************




## Prática

Prática `java04` no Repl.it:

- Faça login em sua conta no Repl.it (a mesma usada em todas as outras práticas)
- Acesse o menu Teams e clique na prática `java04`
- Siga as instruções a seguir


### Parte 1: Herança em Assignments

Sua empresa está desenvolvendo um software assistente na organização de tarefas (assignments) de disciplinas de faculdade. Uma das funcionalidades do software é a geração de mensagens de notificação sobre cada tarefa. As tarefas podem ser individuais ou em grupo, e tarefas em grupo têm dados adicionais. Você vai colaborar no desenvolvimento de algumas classes para resolver este problema.


1. Você vai trabalhar com arquivos na pasta `01-assignments` do projeto `java04` no Repl.it. Os arquivos são os seguintes:

   - Assignment.java:  classe que representa uma tarefa
   - GroupAssignment.java: classe que representa uma tarefa a ser desenvolvida em grupo
   - TrackAssignments: classe que contém o método main, que cria e manipula uma lista de tarefas


2. Compile e execute o código fornecido, usando comandos vistos nas práticas anteriores. Você verá mensagens indicando que o código tem que ser completado.



3. Na classe `Assignment`, implemente o método `public String toString()` , de forma que o primeiro laço em `TrackAssignments` produza o seguinte:

   ```
   ==> Printing all assignment **OBJECTS**:
   { dueDate='2023-10-09', description='java02', pending='true', submitDate='null'}
   { dueDate='2023-10-16', description='java03', pending='true', submitDate='null'}
   { dueDate='2023-10-23', description='java04', pending='true', submitDate='null'}
   { dueDate='2023-10-29', description='project01', pending='true', submitDate='null'}   
   ```
   Veja mais sobre o método `toString` [aqui](http://www.mauda.com.br/?p=1472) (em português) ou [aqui](https://runestone.academy/ns/books/published/csawesome/Unit9-Inheritance/topic-9-7-Object.html) (em inglês).

4. Na classe `Assignment`, complete o método `status`  para retornar uma String que represente a situação da tarefa:

   - "done" se a tarefa estiver completa (não pendente)
   - "late" se a tarefa estiver pendente e atrasada
   - "due in x days" se a tarefa estiver pendente, faltando x=daysLeft() dias para a entrega

5. Na classe `GroupAssignment`, note que o construtor usa a palavra-chave `super`. Uma explicação sobre o uso de `super` em construtores está em: https://materialpublic.imd.ufrn.br/curso/disciplina/2/8/8/4. Existe também outra forma de usar `super`, explicada em: https://www.w3schools.com/java/ref_keyword_super.asp. Leia este material, pois vai ser útil na questão seguinte.

6. Na classe `GroupAssignment`, complete o método `notificationMessage()`  para retornar uma mensagem modificada quando a tarefa for em grupo, conforme o exemplo abaixo:

   ```
   ==> Printing all assignment **MESSAGES**:
   Assignment java02 is late
   Assignment java03 is late
   Assignment java04 is due in 5 days
   Group Assignment project01 is due in 11 days - call teamMate1, teamMate2
   ```
   Dicas:

   - Identifique um "padrão" nas mensagens de notificação: o que é variável e o que é constante na mensagem modificada?
   - Para evitar redundância, use `super` para aproveitar/reusar a mensagem implementada na superclasse


7. Na classe `TrackAssignments`, no final do método `main`, adicione um código para contar e mostrar a quantidade de tarefas concluídas (não pendentes).


8. Se você completou tudo corretamente, o resultado da execução do código agora terá esta forma (com algumas diferenças dependendo da data em que você executar):

   ```
   ==> Printing all assignment **OBJECTS**:
   { dueDate='2023-10-09', description='java02', pending='true', submitDate='null'}
   { dueDate='2023-10-16', description='java03', pending='true', submitDate='null'}
   { dueDate='2023-10-23', description='java04', pending='true', submitDate='null'}
   { dueDate='2023-10-29', description='project01', pending='true', submitDate='null'}

   ==> Printing all assignment **MESSAGES**:
   Assignment java02 is late
   Assignment java03 is late
   Assignment java04 is due in 5 days
   Group Assignment project01 is due in 11 days - call teamMate1, teamMate2

   ==> Printing all assignment messages **AGAIN**:
   Assignment java02 is done
   Assignment java03 is late
   Assignment java04 is due in 5 days
   Group Assignment project01 is due in 11 days - call teamMate1, teamMate2

   ==> Completed assignments: 1
   ```





### Parte 2: Herança em Quizzes


Esta parte deverá ser entregue na pasta `02-quizzes` da prática `java04` no Repl.it.

Nesta parte, você vai criar um programa "do zero", escrevendo todo o código. O programa deverá ter uma hierarquia de classes representando diferentes tipos de questões de quizzes, uma classe representando um quiz e uma programa principal que irá criar e fazer algumas operações com um quiz.

1. Crie uma superclasse `Question`, com atributos/métodos comuns a qualquer tipo de quiz (você tem liberdade para defini-los - não há uma única forma correta de representar isso).

2. Crie pelo menos 2 classes derivadas de `Question`, representando outros tipos de questões, por exemplo: verdadeiro/falso, múltipla-escolha, preencher lacunas, etc. Estas classes especializadas deverão ter atributos específicos.

3. Crie uma classe `Quiz`, que deverá armazenar e gerenciar uma lista de questões, como no exemplo com herança na classe `Laboratory` da aula anterior. 

4. Crie uma classe `Main`, que deverá criar um quiz com várias questões de diferentes classes. Depois de criá-lo, você deverá fazer pelo menos 2 operações à sua escolha  (por exemplo, mostrar as questões, verificar resposta de uma questão, sortear uma questão, etc.), de acordo com os métodos que você implementou na classe `Quiz`.








## Bibliografia


Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulos 11 e 12)
