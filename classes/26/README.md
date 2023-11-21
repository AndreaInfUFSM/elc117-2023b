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

link:     custom.css
          https://fonts.googleapis.com/css?family=Quattrocento%20Sans

link:     https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.css

script:   https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.js

-->

<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/19/README.md
-->




# Programação em grandes projetos

> Um panorama sobre aspectos de programação em grandes projetos

Você teve algum problema instalando Java e/ou libGDX?

- [(x)] Sim
- [( )] Não

## Desenvolvimento local com Java

- Programação orientada a objetos e Java são usados em grandes projetos de software
- Grandes projetos geralmente demandam um **ambiente de desenvolvimento local**

  - várias ferramentas instaladas no seu computador

- *A Software Development Kit, or an SDK, is a collection of tools that you need to develop an application for a specific software framework* (https://www.jetbrains.com/help/idea/sdk.html)

### Java Development Kit (JDK)


Para desenvolvimento com Java, é preciso um Java SDK (também chamado JDK - Java Development Kit), que inclui

- compilador (comando `javac`)
- interpretador (comando `java`): é a máquina virtual Java (JVM - Java Virtual Machine) 
- bibliotecas de classes (vários .class reunidos em arquivos .jar)
- outras ferramentas auxiliares: jar, jshell, etc.
- JRE (Java Runtime Environment): apenas para execução, não para compilação/desenvolvimento

### Distribuições/fornecedores
Há muitas distribuições/fornecedores de JDKs: https://sdkman.io/jdks

- Exemplos: Oracle JDK, OpenJDK, etc.
- Disponíveis para diferentes sistemas operacionais
- Para diferentes versões de Java: https://en.wikipedia.org/wiki/Java_version_history


## Gerenciadores de projetos Java

- Uma artigo: https://medium.com/javarevisited/manage-your-dependencies-in-java-48fee2ee5333


- Grandes projetos possuem muitos componentes/dependências, por exemplo:

  - bibliotecas/frameworks em Java
  - bibliotecas nativas
  - ferramentas auxiliares (jar, etc.)
  - arquivos auxiliares (assets, etc.)
  - ...

- Como gerenciar isso?

  - onde encontrar as dependências?
  - que comandos executar? em que ordem? com quais opções?

### Build automation tools

- Ant: mais antigo
- Maven: muito usado
- Gradle: mais moderno

  - arquivo `build.gradle`: configurações
  - scripts `gradlew` / `gradlew.bat`: instalam e chamam Gradle
  - ficam na raiz do projeto
  - necessário permissão de execução

### Exemplo de projeto

Um projeto gerenciado com Gradle: https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example



## libGDX

> libGDX is a cross-platform Java game development framework based on OpenGL (ES) that works on Windows, Linux, macOS, Android, your browser and iOS. 

- Crie um jogo com poucas linhas de código Java...
- ...mas com muitas, muitas dependências

- Projetos libGDX são gerenciados com Gradle

### Configuradores de projetos

Algumas Alternativa para iniciar projeto

- Ferramenta oficial `gdx-setup`: https://libgdx.com/wiki/start/project-generation
- Ferramenta extra-oficial `gdx-liftoff`: https://github.com/tommyettinger/gdx-liftoff
- Clonar projeto existente
- ~Configurar projeto Gradle manualmente~

### Exemplo de projeto

- No Repl.it: projeto java-libgdx

  - Clone de https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example
  - Versão um pouco mais antiga da libGDX
  - Build para desktop compila mas não executa (sem acesso a ambiente gráfico)
  - Build para web compila, mas pode ultrapassar quota de cpu/memória

- Deploy web em: http://www-usr.inf.ufsm.br/~andrea/extended-drop-example/


## Resolvendo problemas de configuração 

- Problemas são parte do processo de aprendizado
- Você não é uma exceção :-) https://www.reddit.com/r/learnprogramming/comments/s31h5v/i_always_struggle_with_installing_and_configuring/
- Comece pela mensagem de erro

  - Mensagens de erro não devem ser ignoradas ou lidas sem atenção
  - Nem sempre ajudam a resolver o problema, mas pelo menos são um ponto de partida na busca da solução
  - Concentre-se no início da mensagem (não só no final)
  - Busque a mensagem no Google, mas de forma esperta (acrescente palavras de contexto, versões, etc.)

- Considere a "pilha" de software envolvida

  - Em cima do hardware, temos o SO
  - Em cima do SO, temos o JDK e o Gradle
  - Em cima do JDK, temos o IDE (VS Code, IntelliJ IDEA, etc.)
  - Todos podem ter várias versões: quais você está usando? há mais de uma versão instalada?
  - Em qual parte está o problema? Substitua uma parte por outra equivalente

- Para compilar/executar um programa, precisamos

  - Permissão do SO
  - Ambiente configurado para encontrar compilador/JVM (JAVA_HOME, PATH)
  - Ambiente configurado para encontrar dependências do programa (classpath)
  - Alguma dessas condições não está satisfeita?

- Quando seguir tutoriais / dicas / fóruns...

  - se ligue nas datas e versões
  - raciocine, não só copie/cole

- Mais adiante... aprenda sobre containers (Docker, etc.), outra forma de lidar com dependências e configurações de ambiente


### Especificamente com libGDX


- Gradle tasks: algumas tarefas configuradas com `gdx-setup.jar`

  - `gradlew desktop:run`: compila código e executa versão para desktop
  - `gradlew html:dist`: compila código e gera versão para web (para posterior deploy)

#### Entenda possíveis origens de erros

- Na compilação do código Java

  - Códigos simples de exemplo geralmente não geram erros nesta etapa
  - Se houver problema de dependência, compilação vai indicar algum `undefined`

- Na execução para desktop

  - Pode dar erro se algum asset não for encontrado, se faltar alguma dependência, se o ambiente gráfico não puder ser inicializado (Repl.it, por exemplo), etc.

- Na geração de versão para web

  - Alguns recursos não são traduzíveis para web: https://libgdx.com/wiki/html5-backend-and-gwt-specifics
  - Verifique detalhes com `gradlew checkGwt`

#### Busque ajuda

- Tenha em mente que seu erro talvez não seja reprodutível por outras pessoas (ambientes diferentes)

  - Por isso é importante tentar alternativas (e descrevê-las) no seu ambiente local

- Informe versões e resultado completo de uma tentativa frustrada
- Discord do libGDX tem muita gente iniciante e experiente