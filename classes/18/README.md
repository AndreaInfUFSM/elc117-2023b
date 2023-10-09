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




## Prática

Prática `java03` no Repl.it:

- Faça login em sua conta no Repl.it (a mesma usada em todas as outras práticas)
- Acesse o menu Teams e clique na prática `java03`
- Siga as instruções a seguir

### Parte 1: Point, Circle e ListOfCircles

> Nesta parte, você vai apenas analisar e executar os códigos fornecidos, como base para as partes seguintes. Não há nada a entregar.

Você vai trabalhar com arquivos na pasta `01-circles` do projeto `java03` no Repl.it. Os arquivos são os seguintes:

- `Point.java`: contém uma classe que representa um ponto em um plano, com coordenadas X e Y
- `Circle.java`: contém uma classe que representa um círculo, definido por um ponto central e um raio
- `ListOfCircles.java`: contém classe com método `main` que cria vários objetos e os adiciona a uma lista.


#### Alguns comandos Linux

Nesta prática, você vai precisar de alguns comandos de Linux no Shell do Repl.it:


- Para entrar na pasta `01-circles` se você estiver em `java03`: `cd 01-listofcircles` (dica: use Tab para completar nomes de arquivos/pastas)

- Para voltar a `java03` se você estiver em `01-circles`: `cd ..`

- Para voltar a `java03` a partir de qualquer pasta: `cd ~/java03`

- Para remover todos os arquivos `.class` em uma pasta: `rm *.class`


#### Analise os códigos

1. Compile e execute o código na pasta `01-circles`:
   ```
   cd 01-circles
   javac *.java
   java ListOfCircles
   ```

2. Analise as classes `Point` e `Circle`. Note que cada uma delas é uma classe bem simples, como as que vimos na aula anterior.

3. Analise a classe `ListOfCircles`, que contém novidades. Veja que esta classe usa a classe `ArrayList`, que representa uma estrutura de dados muito útil. Para usá-la, temos que primeiro criar um objeto da classe `ArrayList`, e depois usar métodos públicos desta classe para adicionar elementos ou fazer outras operações com a lista. 

4. Veja [aqui](https://www.w3schools.com/java/java_arraylist.asp) algumas operações possíveis de realizar com uma `ArrayList`. 



### Parte 2: ListOfTeamMates

> Esta parte deve ser realizada na pasta `02-teammates`.

Nesta parte, você vai criar um programa para armazenar informações sobre uma lista de colegas de equipe. 

Os dados a serem armazenados podem ser constantes ou gerados aleatoriamente (não devem ser digitados pelo usuário).

⚠️ Sempre que for definir nomes de classes, métodos, atributos, variáveis, etc., acostume-se a seguir alguma convenção de estilo, por exemplo
https://google.github.io/styleguide/javaguide.html#s5-naming. Veja [aqui](https://vaiprogramar.com/nomes-de-variaveis-tudo-que-voce-precisa-saber-para-programar/) um artigo sobre a importância disso.



#### Class TeamMate

Na pasta `02-teammates`, no arquivo `TeamMate.java`:

1. Crie a classe `TeamMate` com os seguintes atributos representando um colega:
   - userId: String
   - name: String
   - online: boolean 

2. Crie métodos get/set para todos os atributos.

3. Crie um construtor default (que não recebe argumentos). Você pode escolher quais serão os valores default para cada atributo.

4. Crie um construtor que receba 3 argumentos e os utilize para inicializar os 3 atributos.

5. Crie um construtor que receba valores para `userId` e `name` e inicialize os 2 atributos correspondentes. Este construtor deverá inicializar o atributo `online` com o valor default `false`.

#### Classe ListOfTeamMates

Na pasta `02-teammates`, no arquivo `ListOfTeamMates.java`:

1. Crie a classe `ListOfTeamMates` que deverá ter um método `main`.  Siga exatamente a sintaxe usada nos outros exemplos que definem este método: `public static void main(String[] args)`.

2. Dentro do método `main`, crie um objeto da classe `ArrayList<TeamMate>` para representar uma lista de colegas de equipe.

3. Crie vários objetos da classe `TeamMate`, usando qualquer lógica à sua escolha para setar os atributos de cada `TeamMate`. Adicione todos os objetos na lista.

4. Percorra a lista e mostre o nome de cada `TeamMate` armazenado.

5. Percorra a lista e mostre o `userId` dos `TeamMate` que estiverem `online`.

6. Consulte [aqui](https://www.w3schools.com/java/java_arraylist.asp) outros métodos disponíveis na classe `ArrayList` e utilize algum que não tenha sido usado antes.


### Parte 3: Laboratory

> Nesta parte, você vai completar um código fornecido.



Você vai trabalhar com arquivos na pasta `03-laboratory` do projeto `java03` no Repl.it. Os arquivos são os seguintes:


- `Student.java`: classe que representa um estudante de uma instituição
- `Professor.java`: classe que representa um professor de uma instituição
- `Laboratory.java`: classe que representa um laboratório da instituição. Um laboratório pode ter estudantes e professores como membros. **Esta classe está incompleta!**
- `Main.java`: classe contendo um método `main` que cria e manipula objetos das classes acima


#### Completando o código

1. Compile e execute o código:
   ```
   cd 03-laboratory
   javac *.java
   java Main
   ```
   **Você verá que a compilação dá erro, pois o código está incompleto!**


2. Você deverá fazer o código funcionar alterando somente o arquivo `Laboratory.java`, de forma a completar as partes marcadas com "COMPLETE-ME". Para isso, é importante analisar todos os arquivos fornecidos.

4. Se o código for completado como solicitado, a saída do Main será a seguinte:
   ```
   NCC has 3 members
   [{class='Student', name='Nome1 Sobrenome1', userId='username1'}, {class='Student', name='Nome2 Sobrenome2', userId='username2'}, {class='Professor', name='Andrea Charao', userId='andrea', room='376', building='Anexo B'}]
   User andrea found
   ```


#### Refletindo sobre o código

O código fornecido não usa todo o potencial da orientação a objetos. Vamos fazer uma análise crítica deste código.

1. Você consegue identificar alguma redundância nos códigos (dentro de uma mesma classe ou em classes diferentes)?

2. O que aconteceria se fosse necessário armazenar outros atributos sobre estudantes e professores? (por exemplo, CPF, data de nascimento, telefone, etc?)

3. O que aconteceria na classe `Laboratory` se tivéssemos outras categorias de membros além de estudantes e professores (técnicos, administradores, etc.)?

Coloque as respostas em um arquivo README.md dentro de seu repositório de entrega e prepare-se para discutir em aula quando solicitado.



## Bibliografia


Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulos 11 e 12)
