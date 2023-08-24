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
  files.push([order[0], `@input(0)`])
if (order[1])
  files.push([order[1], `@input(1)`])
if (order[2])
  files.push([order[2], `@input(2)`])
if (order[3])
  files.push([order[3], `@input(3)`])
if (order[4])
  files.push([order[4], `@input(4)`])
if (order[5])
  files.push([order[5], `@input(5)`])
if (order[6])
  files.push([order[6], `@input(6)`])
if (order[7])
  files.push([order[7], `@input(7)`])
if (order[8])
  files.push([order[8], `@input(8)`])
if (order[9])
  files.push([order[9], `@input(9)`])


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
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/03b/README.md
-->

# Programação Funcional em Haskell

> Teste o que você entendeu até agora!

## Primeiro quiz

As questões a seguir são corrigidas automaticamente.






{{1}}
********************************************************************************
É possível executar um código Haskell que não tenha uma função `main`?

- [(x)] Sim
- [( )] Não
******************************************************

Sim, é possível. Você não precisa definir uma função `main` para poder executar um código Haskell, já que é possível executar qualquer função chamando-a no interpretador interativo.

*******************************************************


********************************************************************************



{{2}}
********************************************************************************
Se você executar o código abaixo no interpretador interativo GHCi, qual será o resultado?

```haskell
length "abc" + length [1,2,3]
```

[[6]]
******************************************************

A função `length` obtém a quantidade de elementos de uma lista, que pode ser de qualquer tipo. Uma String em Haskell é uma lista de Char, portanto a função se aplica tanto a "abc" (3 elementos), como a [1,2,3] (também 3 elementos).

*******************************************************

********************************************************************************



{{3}}
********************************************************************************

Marque abaixo as opções que designam listas válidas em Haskell:

- [[x]] `['a','b','c']`
- [[ ]] `["idade", 10]`
- [[x]] `[[1,2],[8,9]]`
******************************************************

Em Haskell, listas são homogêneas, compostas por dados do mesmo tipo. Por isso, a lista `["idade", 10]` não é válida, mas as outras alternativas são.

*******************************************************

********************************************************************************

{{4}}
********************************************************************************
Na definição de uma função em Haskell, é necessário escrever `return` para retornar o resultado de uma função?

- [( )] Sim
- [(x)] Não
******************************************************

Não, ao contrário de muitas linguagens que suportam funções, em Haskell não se usa `return` para explicitar que a função deve retornar algum valor para o ponto em que foi chamada. Esse retorno acontece implicitamente.

*******************************************************

********************************************************************************


{{5}}
********************************************************************************

Considere esta definição:

``` haskell
func :: String -> Bool 
func s = length s > 10
```

O resultado de `func "teste"` será:

- [( )] 10
- [( )] True
- [( )] 5
- [(x)] False
******************************************************

Esta função será `True` se o tamanho da String for maior que 10, portanto o resultado para a String "teste" será `False`.

*******************************************************

********************************************************************************

{{6}}
********************************************************************************

Considere esta definição:

``` haskell
func :: Int -> Char -> String
func n c = replicate n c
```

O tipo de retorno desta função é:

- [( )] Int
- [( )] Char
- [(x)] String
******************************************************

Haskell permite escrever funções tipadas ou não. No caso de funções tipadas, os tipos envolvidos ficam na primeira linha de definição da função. Para saber o tipo de cada argumento e o tipo do resultado, você deve fazer uma correspondência entre a linha de cima e a linha de baixo: o primeiro tipo corresponde ao primeiro argumento e assim por diante, sendo que o último tipo é o tipo do resultado.

*******************************************************

********************************************************************************


{{6}}
********************************************************************************

Considere novamente esta definição:

``` haskell
func :: Int -> Char -> String
func n c = replicate n c
```

Quais dos usos destas função são inválidos?

- [( )] func 3 'z'
- [(x)] func (2^2) "a"
- [( )] func (length [1,2,3]) 'a'
- [(x)] func pi 'r'
******************************************************

O tipo do argumento `n` é `Int` e do argumento `c` é `Char`. Assim, o segundo item é inválido porque o segundo argumento não é do tipo Char, mas sim do tipo String. O último item é inválido porque a função constante pi, que está no lugar do primeiro argumento, não é do tipo Int. 

*******************************************************

********************************************************************************



## Segundo quiz

**ATENÇÃO!!!**

> Esta parte **não tem correção automática** e deve ser feita em aula, junto com toda a turma! Você deverá responder depois de entrar no Classroom deste material de aula, conforme instruções da professora.


{{1}}
********************************************************************************

A função `toUpper :: Char -> Char` está definida em um módulo padrão do GHC, acessível se você incluir `import Data.Char` no início do programa ou no GHCi. Esta função recebe um único caracter e retorna seu equivalente em maiúscula.

Suponha que você tenha a String "Paradigmas" e queira obter "PARADIGMAS" (tudo em maiúsculas). Qual o código em Haskell que faz isso?


- [(a)] a) toUpper "Paradigmas"
- [(b)] b) map toUpper "Paradigmas"


********************************************************************************


{{2}}
********************************************************************************

Qual será o resultado de `filter (> pi) [1.5, 2.5, 3.5, 4.5]`?

- [(a)] a) [2.5,3.5,4.5,5.5]
- [(b)] b) [3.5,4.5]
- [(c)] c) []


********************************************************************************



{{3}}
********************************************************************************

Suponha que você tenha uma lista de nomes (por exemplo,`["Fulano", "Beltrano"]`) e queira obter todos os nomes em maiúsculas (`["FULANO", "BELTRANO"]`).  Qual o código em Haskell que faz isso?


- [(a)] a) `filter toUpper ["Fulano", "Beltrano"]`
- [(b)] c) `map toUpper ["Fulano", "Beltrano"]`
- [(c)] b) `map (map toUpper) ["Fulano", "Beltrano"]`


********************************************************************************



{{4}}
********************************************************************************

Suponha  um registro de presença representado por uma **tupla** em Haskell (tuplas são valores delimitados por parênteses e separados por vírgulas). Por exemplo, a tupla `("Fulano",True)` indica que Fulano está presente. 

Agora suponha uma lista de presenças de uma turma inteira: 

`[("Fulano",True),("Beltrano",False)]`. 

Qual dos códigos abaixo obtém a quantidade de alunos presentes?


- [(a)] a) `length (filter (\(n,p) -> p) [("Fulano",True),("Beltrano",False)])`
- [(b)] b) `length (map (\(n,p) -> p) [("Fulano",True),("Beltrano",False)])`


********************************************************************************

{{5}}
Você ficou com dúvida? Ou não estava na aula?
 <details>
  <summary>Gabarito</summary>
  <p>1b: Você precisa usar map para aplicar toUpper a cada caracter da String</p>
  <p>2a: Este filter aplica a condição (> pi) a cada elemento da lista, selecionando 3.5 e 4.5, que são maiores que 3.14159</p>
  <p>3c: Note que toUpper se aplica a um único Char. Uma String é uma lista de Char, ou seja [Char]. Aqui neste exemplo, temos uma lista de String, ou seja [String] ou [[Char]]. Por isso, o (map toUpper) mais interno corresponde a uma conversão de uma única String para maiúscula, e o map mais externo aplica o map interno a cada String da lista de String.</p>
  <p>4a: Para selecionar os alunos presentes, aplicamos filter. O resultado será uma lista somente com alunos presentes (p == True), que será passada para length, que retornará o número de elementos. A condição neste filter usa uma sintaxe desconhecida até então, mas note que conhecer isso não é imprescindível para resolver esta questão, pois a única diferença entre as opções é o uso de map ou filter.</p>    
</details> 
