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
window.CodeRunner.init("wss://java-coderunner.andreaschwertne.repl.co/")


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




# Revisão

> Relembre o que vimos na segunda parte da disciplina... 

## Encapsulamento

Considere que o código abaixo esteja em um arquivo `Main.java`:


``` java
class Player {
  private String nickname;
  private int score;
  Player(String nickname) {
    this.nickname = nickname;
    this.score = 0;
  }
  public void addScore(int n) {
    score += n;
  }
  public int getScore() {
    return this.score;
  }
}

class Main {
  public static void main(String[] args) {
    Player p = new Player("mrrobot");
    System.out.println(p.getScore());
    p.addScore(5);
    System.out.println(p.getScore());
  }
}
```
@LIA.eval(`["Main.java"]`, `javac Main.java`, `java Main`)


{{1}}
********************************************************************************
Na programação orientada a objetos, uma classe serve como modelo para criação de objetos, que são habitualmente chamados de "instâncias" da classe.

Quantos objetos da classe Player são criados no código acima?

- [( )] 0
- [(x)] 1
- [( )] 2
- [( )] 5
******************************************************

O código cria 1 objeto da classe Player, usando `new`.

*******************************************************

********************************************************************************

{{2}}
********************************************************************************
Na programação orientada a objetos, **construtores** são usados na inicialização de objetos.

Assinale as afirmativas verdadeiras sobre construtores em Java, considerando o código acima.

- [[x]] Construtores em Java devem possuir o mesmo nome da classe e não possuir tipo de retorno.
- [[ ]] Na classe Main, temos a declaração de um construtor.
- [[x]] Na classe Player, temos a declaração de um construtor.
- [[x]] A execução de `new Player("mrrobot")` invoca um construtor de Player.
- [[ ]] Podemos inicializar a pontuação de um Player chamando `new Player(0)`.
- [[ ]] Na classe Main, podemos declarar um construtor para a classe Player.
- [[x]] Podemos declarar mais de um construtor para a classe Player.
- [[ ]] Toda classe em Java precisa declarar ao menos um construtor.
- [[ ]] Se não declararmos um construtor para uma classe, não poderemos instanciar esta classe.


********************************************************************************



{{3}}
********************************************************************************
Um kit de desenvolvimento Java (JDK) inclui, entre outros componentes, um compilador, que traduz o código Java para um código intermediário, e uma máquina virtual Java, que interpreta e gerencia a execução do código intermediário. O compilador e o interpretador são programas distintos que podem  ser executados em linha de comando.

Como podemos compilar o programa acima em linha de comando?

- [( )] `javac Player.java`
- [( )] `java Main`
- [(x)] `javac Main.java`
- [( )] `java Main.class`
******************************************************

Habitualmente, nos kits de desenvolvimento Java, o compilador é executado em linha de comando com `javac`. O comando `java`, por sua vez, executa a máquina virtual Java.

*******************************************************

********************************************************************************

{{4}}
********************************************************************************
Uma classe encapsula atributos e métodos em uma única unidade que pode ser reusada por outras classes.

Assinale as afirmativas verdadeiras sobre classes em Java, considerando o código acima.

- [[x]] A classe Player declara 2 atributos: nickname e score.
- [[x]] Se declararmos o método addScore como private, teremos erro na compilação do código acima.
- [[ ]] Não podemos chamar o método getScore dentro da classe Player.
- [[x]] Quando um objeto é criado, seus atributos são inicializados, constituindo o estado do objeto.

********************************************************************************


{{5}}
********************************************************************************
Sobre referências para objetos, assinale as afirmativas verdadeiras, considerando o código acima.

- [[ ]] Na classe Main, temos 5 referências para Player.
- [[x]] A variável p é uma referência para um objeto da classe Player.
- [[ ]] Se substituirmos `this.score = 0;` por `score = 0;`, teremos erro na compilação do código.
- [[x]] Podemos substituir `score += n;` por `this.score += n;` sem provocar erro na compilação do código.

********************************************************************************


## Herança

Analise o código abaixo:

``` java
class Animal {
  void eat() {
    System.out.print("This animal eats food.");
  }
}

class Bird extends Animal {
  void fly() {
    System.out.println("This bird can fly.");
  }

}

public class TestInheritance {
  public static void main(String[] args) {
    Bird sparrow = new Bird(); // um pardal !    
    sparrow.eat();
    sparrow.fly();
  }
}
```
@LIA.eval(`["TestInheritance.java"]`, `javac TestInheritance.java`, `java TestInheritance`)


{{1}}
********************************************************************************
Qual a saída deste programa?

- [( )] This animal eats food.
- [( )] This bird can fly.
- [(x)] This animal eats food.This bird can fly.
- [( )] Nenhuma, pois dá erro de compilação.

********************************************************************************


{{2}}
********************************************************************************
Selecione as afirmações FALSAS sobre este programa:

- [[ ]] Bird deriva de Animal.
- [[x]] Bird é um objeto da classe Animal.
- [[x]] Não podemos instanciar um objeto da classe Animal.
- [[x]] O código `Animal blue = new Bird();` causa erro de compilação.

********************************************************************************


{{3}}
********************************************************************************
As linhas de código abaixo podem ser inseridas no final do método main sem causar erro de compilação?

``` java
Animal blue = new Animal();
blue.fly();
```

- [( )] Sim
- [(x)] Não
******************************************************

Não. Embora a primeira linha seja válida, a segunda não é, porque foi instanciado um objeto da classe Animal, e esta classe não possui o método fly.

*******************************************************

********************************************************************************

{{4}}
********************************************************************************
Se declararmos um construtor sem argumentos em Animal e um construtor sem argumentos em Bird, o que acontecerá na execução de `Bird sparrow = new Bird();`?

- [( )] Somente o construtor de Bird será chamado.
- [(x)] O construtor de Animal será chamado primeiro e o construtor de Bird será chamado depois.
- [( )] O construtor de Bird será chamado primeiro e o construtor de Animal será chamado depois.

********************************************************************************


{{5}}
********************************************************************************
Assinale as afirmações verdadeiras:

- [[x]] Se Animal tivesse um atributo private, este atributo não poderia ser acessado em Bird.
- [[x]] Atributos e métodos protected são acessíveis a classes herdadas e também a qualquer classe dentro do mesmo pacote.
- [[x]] Construtores não são herdados por subclasses.
- [[ ]] Podemos usar super para ter acesso a um método private da superclasse.



********************************************************************************


{{6}}
********************************************************************************
Qual das linhas abaixo pode ser inserida na classe Bird para sobrescrever (override) o método eat?


- [( )] `void eat(String something) { }`
- [(x)] `void eat() { }`

********************************************************************************



## Na libGDX

Considere os seguintes códigos extraídos da libGDX: 

Fonte: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/ApplicationListener.java

``` java
public interface ApplicationListener {
	/** Called when the {@link Application} is first created. */
	public void create ();

	/** Called when the {@link Application} is resized. This can happen at any point during a non-paused state but will never
	 * happen before a call to {@link #create()}.
	 * 
	 * @param width the new width in pixels
	 * @param height the new height in pixels */
	public void resize (int width, int height);

	/** Called when the {@link Application} should render itself. */
	public void render ();

	/** Called when the {@link Application} is paused, usually when it's not active or visible on-screen. An Application is also
	 * paused before it is destroyed. */
	public void pause ();

	/** Called when the {@link Application} is resumed from a paused state, usually when it regains focus. */
	public void resume ();

	/** Called when the {@link Application} is destroyed. Preceded by a call to {@link #pause()}. */
	public void dispose ();
}
```

Fonte: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/Game.java

``` java
public abstract class Game implements ApplicationListener {
	protected Screen screen;

	@Override
	public void dispose () {
		if (screen != null) screen.hide();
	}

	@Override
	public void pause () {
		if (screen != null) screen.pause();
	}

	@Override
	public void resume () {
		if (screen != null) screen.resume();
	}

	@Override
	public void render () {
		if (screen != null) screen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize (int width, int height) {
		if (screen != null) screen.resize(width, height);
	}

	/** Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
	 * screen, if any.
	 * @param screen may be {@code null} */
	public void setScreen (Screen screen) {
		if (this.screen != null) this.screen.hide();
		this.screen = screen;
		if (this.screen != null) {
			this.screen.show();
			this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	/** @return the currently active {@link Screen}. */
	public Screen getScreen () {
		return screen;
	}
}
```

{{1}}
********************************************************************************
A linha de código abaixo é válida em um código que usa a classe Game?

``` java
Game game = new Game();
```

- [( )] Sim
- [(x)] Não
******************************************************

Não, pois a classe Game é abstrata.

*******************************************************

********************************************************************************


{{2}}
********************************************************************************
Uma classe derivada de Game precisa sobrescrever o método dispose?

- [( )] Sim
- [(x)] Não
******************************************************

Não, pois este método não é abstract, é um método concreto (implementado).

*******************************************************

********************************************************************************


{{3}}
********************************************************************************
A classe Game deriva de Object?

- [(x)] Sim
- [( )] Não
******************************************************

Sim, todas as classes em Java derivam de Object.

*******************************************************

********************************************************************************

{{4}}
********************************************************************************
Na classe Game temos um exemplo de sobrecarga de método?

- [( )] Sim
- [(x)] Não
******************************************************

Não. A sobrecarga ocorre quando, na mesma classe, temos métodos com o mesmo nome, mas com argumentos diferentes.

*******************************************************

********************************************************************************


## Polimorfismo

Considere o seguinte código:

``` java
public class Example {
  // Method with one parameter
  void display(int x) {
    System.out.println("Method with one parameter: " + x);
  }

  // Method with two parameters of different types
  void display(String str, int num) {
    System.out.println("Method with two parameters: " + str + ", " + num);
  }

  public static void main(String[] args) {
    Example example = new Example();
    example.display(10);
    example.display("Hello", 20);
  }
}
```
@LIA.eval(`["Example.java"]`, `javac Example.java`, `java Example`)


{{1}}
********************************************************************************
No código acima, temos sobrecarga ou sobrescrita de método?

- [(x)] Sobrecarga (overload)
- [( )] Sobrescrita (override)
******************************************************

O método display é sobrecarregado: há 2 implementações diferentes para um mesmo nome de método, para argumentos diferentes.

*******************************************************

********************************************************************************



{{2}}
********************************************************************************
Na classe Example acima, podemos explorar polimorfismo declarando os métodos `void run() {}` e `int run() { return 2; }`?

- [( )] Sim
- [(x)] Não
******************************************************

Não podemos sobrecarregar o método `run` desta forma, pois é preciso distinguir os métodos pelos seus argumentos, não pelo tipo de retorno.

*******************************************************

********************************************************************************


{{3}}
********************************************************************************
Qual a saída do código abaixo?


``` java
class Animal {
  private int me;
  void eat() {
    System.out.print("This animal eats food.");
  }
}

class Bird extends Animal {
  void eat() {
    System.out.println("This animal eats worms.");
    
  }

}

public class Main {
  public static void main(String[] args) {
    Animal animal = new Bird(); 
    animal.eat();    
  }
}
```
@LIA.eval(`["Main.java"]`, `javac Main.java`, `java Main`)


- [( )] This animal eats food.
- [(x)] This animal eats worms.

********************************************************************************








