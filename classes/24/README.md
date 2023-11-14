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
//window.CodeRunner.init("wss://ancient-hollows-41316.herokuapp.com/")

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

@load.java: @load(java,@0)

@load
<script style="display: block" modify="false" run-once="true">
    fetch("@1")
    .then((response) => {
        if (response.ok) {
            response.text()
            .then((text) => {
                send.lia("LIASCRIPT:\n``` @0\n" + text + "\n```")
            })
        } else {
            send.lia("HTML: <span style='color: red'>Something went wrong, could not load <a href='@1'>@1</a></span>")
        }
    })
    "loading: @1"
</script>
@end
-->

<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/19/README.md
-->




# Programação Concorrente




> Este material faz parte de uma introdução ao paradigma de **programação concorrente**.

## Quiz

> AGUARDE instruções da professora antes de fazer este quiz!

1. Um programa concorrente cria 2 threads, X e Y, e aguarda que terminem suas execuções. 

   A thread X faz um cálculo e mostra o resultado `X=42`. 
   
   A thread Y faz outro cálculo e mostra o resultado `Y=81`. 
   
   A saída abaixo é uma possível saída desse programa?

   ```
   Y=81
   X=42
   ```

   - [(sim)] Sim
   - [(nao)] Não


2. O programa pode produzir esta saída?

   ```
   X=42
   Y=81
   ```

   - [(sim)] Sim
   - [(nao)] Não

3. Considere um programa em Java que declara uma classe `Simulator` derivada de `Thread`.

   O método `main` deste programa tem a seguinte linha de código: 

   `Simulator s = new Simulator();`

   Supondo que `main` execute sequencialmente até a linha acima, qual dos códigos abaixo ativa a execução concorrente deste programa?

   - [(run)] `s.run();`
   - [(start)] `s.start();`



## Concorrência



> Diariamente, preparar **café da manhã**: café com leite + pão torrado com geleia

Tarefas:

```
pegar xícara em armário
pegar leite em geladeira
pegar colher em gaveta
pegar café em pó em armário
pegar geleia em geladeira
pegar pão em armário
pegar faca em gaveta

servir leite na xícara
aquecer leite no microondas
adicionar café em pó ao leite com colher
cortar fatia de pão com faca
tostar fatia de pão na torradeira
passar geleia no pão com faca
```

Questões:

1. Quantos/quais são os processadores (que executam as tarefas)?
2. Podemos chegar ao mesmo resultado alterando a ordem de algumas tarefas?
3. Há tarefas que podem ser feitas ao mesmo tempo?
4. Há tarefas dependentes, que precisam ser feitas em sequência (uma depende do resultado de outra)?



## Threads


- Threads = fluxos de execução independentes em um programa / processo
- Recursos privados: contadores de programa, variáveis locais
- Recursos **compartilhados**

![Diagrama extraído de um livro de Sistemas Operacionais, comparando um processo "single-threaded" com um processo "multithreaded". Ambos os processos têm recursos (code, data, files) que são comuns às threads. O processo "single-threaded" tem apenas um fluxo de execução, enquanto o processo "multithreaded" tem 3 fluxos de execução, neste exemplo. Cada thread dispõe de seus próprios registradores e pilhas.](img/4_01_ThreadDiagram.jpg)

Fonte: Abraham Silberschatz, Greg Gagne, and Peter Baer Galvin, "Operating System Concepts, Ninth Edition ", Chapter 4 

## Recursos compartilhados

1. O que seriam "recursos" neste exemplo? 
2. Há tarefas independentes que **compartilham** recursos?
3. O que acontece se tivermos 4 pessoas preparando café da manhã na mesma cozinha?

```
pegar xícara em armário
pegar leite em geladeira
pegar colher em gaveta
pegar café em pó em armário
pegar geleia em geladeira
pegar pão em armário
pegar faca em gaveta

servir leite na xícara
aquecer leite no microondas
adicionar café em pó ao leite com colher
cortar fatia de pão com faca
tostar fatia de pão na torradeira
passar geleia no pão com faca
```


### Um meme :-)
Competição por recursos compartilhados virou até meme :-)

https://imgur.com/a/QGUdTw7

This is what we think threads do
![Imagem mostrando 10 filhotes de cachorro bem comportados e alinhados, cada um comendo ração em seu próprio prato.](img/main-qimg-d4ec222ac1a99b003f4fd5565f2382b8-pjlq.jpeg)

This is what they actually do
![Imagem mostrando os filhotes de cachorro em um cenário menos comportado, alguns compartilhando um prato de ração](img/main-qimg-894140118e18d3956018a841ce82dba1-pjlq.jpeg)

### Um exemplo clássico

Um problema clássico de concorrência que ilustra competição por recursos compartilhados:

![Diagrama extraído de uma aula sobre concorrência em um curso no MIT. O diagrama possui um retângulo representando um espaço de memória compartilhado, contendo os saldos de 3 contas bancárias. Outros 4 retângulos externos representam caixas eletrônicos fazendo operações sobre as contas, sendo que 3 deles fazem operações de depósito e consulta de saldo sobre a primeira conta.](img/shared-memory-bank-account.png)

Fonte: https://web.mit.edu/6.005/www/fa14/classes/20-queues-locks/synchronization/


### Classe ContaBancaria

- Classe ContaBancaria encapsula atributo saldo e métodos para movimentar uma conta
- Thread principal cria uma conta e passa referência para threads t1 e t2
- Threads t1 e t2 compartilham a conta e fazem operações concorrentes sobre ela

![Imagem dividida em duas partes. Na parte esquerda, uma classe ContaBancaria em UML, contando um atributo saldo e dois métodos reposita e retira. Na parte direita, uma linha do tempo de execução concorrente, com 3 threads: a thread main cria um objeto da classe ContaBancaria, inicializando seu saldo com 1000; a thread t1 faz um depósito de 300 e a thread t2 faz uma retirada de 100.](img/Selection_016.png)


### ContaBancaria em Java

> Este exemplo tem um problema de execução concorrente!

Classe `Conta`: saldo e métodos para movimentar a conta

``` java
class Conta {

  private float saldo = 0f;

  public Conta(float inicial) {
    saldo = inicial;
  }

  public float getSaldo() {
    return saldo;
  }

  public void deposita(float valor) {
    saldo += valor;
  }

  public void retira(float valor) {
    saldo -= valor;
  }
```

Classe `Operacoes` faz múltiplos depósitos e retiradas

``` java
class Operacoes implements Runnable {

  private Conta c;
  private int n;
  private int valor_credito;
  private int valor_debito;

  public Operacoes(Conta c, int n, int valor_credito, int valor_debito) {
    this.c = c;
    this.n = n;
    this.valor_credito = valor_credito;
    this.valor_debito = valor_debito;
  }

  @Override
  public void run() {
    for (int i = 0; i < n; i++) {
      c.deposita(valor_credito);
      c.retira(valor_debito);
    }
  }
}
```

Classe principal:

- cria conta 
- passa conta para transação
- cria threads que com acesso a objeto compartilhado

``` java
class TransacoesBancarias {

  public static void main(String[] args) {
  
    Conta c = new Conta(0);
    // 1000 depósitos de 10 e 1000 retiradas de 5 = 5000
    Runnable operacoes = new Operacoes(c, 1000, 10, 5);
    
    // Cria 2 objetos da classe Thread
    Thread thread1 = new Thread(operacoes); // 5000
    Thread thread2 = new Thread(operacoes); // 5000

    // Ativa execução das threads
    thread1.start();
    thread2.start();

    // Aguarda término das threads
    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Saldo deveria ser 10000
    System.out.println("Saldo final: " + c.getSaldo());
  }
}
```

### Zoom na execução

- Sistema operacional escalona execução das threads
- Tarefas podem ser interrompidas para que outras executem
- Ver "preemption": https://en.wikipedia.org/wiki/Preemption_%28computing%29
- Depósitos e retiradas acarretam várias instruções na CPU
- A seguir, veremos um "zoom" em uma execução em SO preemptivo

![Mesma imagem mostrada anteriormente, com a classe ContaBancaria representada em UML e com um diagrama de execução concorrente de 3 threads. Sobre esta imagem, foi adicionada a imagem de um detetive usando uma lupa para examinar a execução em maior detalhe.](img/Selection_017.png)


                 {{1}}
************************************************

saldo inicial compartilhado = 1000, t1 escalonado primeiro, t2 depois: saldo = 1200

![Imagem detalhando a execução concorrente das threads t1 e t2, que acessam uma conta bancária compartilhada. As operações de depósito e retirada são decompostas em instruções do processador: load, add, sub, store.  Em uma possível execução concorrente, a thread t1 faz load 1000, add 300 e store 1300. Já a thread t2 faz load 1300, sub 100 e store 1200. O resultado é 1200.](img/saldo1200.png)


************************************************

                 {{2}}
************************************************

saldo inicial compartilhado = 1000, t1 sofre preempção, t2 executa: saldo = 900 ?!

![Imagem de outra possível execução concorrente. A thread t1 faz load 1000, add 300 e store 1300.No entanto, a thread t2 é escalonada ao mesmo tempo e faz load 1000 antes que t1 faça store na memória compartilhada. Portanto, quando t2 faz sub 100, o resultado será 900. Este resultado é escrito (store) por t2 após t1, portanto o saldo final será 900.](img/saldo900.png)


************************************************


### Race condition

- Condição de corrida: concorrência no acesso a recursos compartilhados provoca diferentes resultados, dependendo da ordem
- Resultados possivelmente indesejados / inconsistentes (bug difícil de identificar e corrigir!)
- Ver: https://en.wikipedia.org/wiki/Race_condition
- Soluções geralmente exigem sincronização entre threads 



![Imagem esquemática de 2 cruzamentos entre estradas com vias paralelas. Na metade esquerda da imagem, é ilustrado um conflito quando um carro de uma estrada chega no cruzamento e se encontra com um carro vindo da outra estrada, em um ponto em que 2 vias coincidem. O cruzamento representa um recurso compartilhado. Na metade direita da imagem, é ilustrada uma situação em que não há conflito, quando carros passam no cruzamento em vias que não coincidem (um carro indo e outro voltando na mesma estrada).](img/The-conflict-and-concurrent-lanes.jpeg)

Fonte: https://www.researchgate.net/figure/The-conflict-and-concurrent-lanes_fig14_304612287
  


## Sincronização entre threads




Problema

- Inconsistência de dados
- Condição de corrida (race condition)

Solução

- Identificar dados compartilhados e seção crítica (onde ocorre conflito)
- Aplicar exclusão mútua / locks, semáforos, etc.


![Imagem de uma placa de trânsito indicando: "Atenção" "Ponte estreita" "Um veículo por vez"](img/ponte.png)

Fonte: [Notícia Portal RIC](https://ric.com.br/prja/seguranca/acidentes/caminhonete-passa-por-cima-de-carro-e-mata-motorista-em-ponte-estreita-de-araucaria/)


![Imagem de uma ponte estreita, onde só passa um veículo.](img/ponte-estreita.jpg)

Fonte: [Notícia Grupo RBJ de Comunicação](https://rbj.com.br/condutor-de-caminhao-ignora-sinalizacao-e-atinge-toldo-da-ponte-estreita-em-marmeleiro/)


### Exclusão mútua em Java


- Palavra-chave `synchronized` e/ou recursos do pacote `java.util.concurrent` (locks, atomic)

- Exclusão mútua com `synchronized`: 

  - palavra-chave adicionada a seções críticas (métodos / blocos de código)
  - quando threads fazem operações `syncrhonized` ao mesmo tempo sobre um mesmo dado/objeto

    - só uma executa por vez
    - outras aguardam liberação

- É uma (pequena) limitação à concorrência

- Em SO serão vistos outros mecanismos de sincronização / exclusão mútua (em C)


``` java
class Conta {

  private float saldo = 0f;

  public Conta(float inicial) {
    saldo = inicial;
  }

  public float getSaldo() {
    return saldo;
  }

  public synchronized void deposita(float valor) {
    saldo += valor;
  }

  public synchronized void retira(float valor) {
    saldo -= valor;
  }
```

### Em outras linguagens

- Threads e sincronização são recursos de programação que existem em muitas linguagens

- Por exemplo:

  - TypeScript: http://web.mit.edu/6.031/www/fa21/classes/23-mutual-exclusion/
  - C: https://courses.cs.washington.edu/courses/cse333/20au/lectures/26-threads-code/lock_example.cc.html


### Entrevista com criador da linguagem Elixir

- Elixir é uma linguagem de programação funcional criada por José Valim, um desenvolvedor brasileiro
- Antes de criar Elixir, ele trabalhava como mantenedor da plataforma Ruby on Rails
- A criação de Elixir foi motivada por problemas de concorrência identificados em Ruby on Rails


Entrevista (março/2023): https://youtu.be/LrwsBqOovnE

"Por que você decidiu criar o Elixir?" https://youtu.be/LrwsBqOovnE?t=1236

<iframe width="1009" height="568" src="https://www.youtube.com/embed/LrwsBqOovnE?start=1236" title="DEV Talks #01 - José Valim" frameborder="0" allow="autoplay; clipboard-write; picture-in-picture; web-share" allowfullscreen></iframe>


## Bibliografia



Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulo 13)


