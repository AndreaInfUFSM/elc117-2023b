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





<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2023b/master/classes/gdx/README.md
-->




# libGDX

> libGDX is a cross-platform Java game development framework based on OpenGL (ES) that works on Windows, Linux, macOS, Android, your browser and iOS. 

- Crie um jogo com poucas linhas de código Java...
- ...mas com muitas, muitas dependências

- Projetos libGDX são gerenciados com Gradle

### Configuradores de projetos

Algumas alternativas para iniciar projeto

- Ferramenta oficial `gdx-setup`: https://libgdx.com/wiki/start/project-generation
- Ferramenta extra-oficial `gdx-liftoff`: https://github.com/tommyettinger/gdx-liftoff
- Clonar algum projeto existente
- ~Configurar projeto Gradle manualmente~

### Exemplo de projeto

- No Repl.it: projeto java-libgdx

  - Clone de https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example
  - Versão um pouco mais antiga da libGDX
  - Build para desktop compila mas não executa (sem acesso a ambiente gráfico)
  - Build para web compila, mas pode ultrapassar quota de cpu/memória

- Deploy web em: http://www-usr.inf.ufsm.br/~andrea/extended-drop-example/

### Documentação: Application Life Cycle

- https://libgdx.com/wiki/app/the-life-cycle
- *A libGDX application has a well defined life-cycle, governing the states of an application, like creating, pausing and resuming, rendering and disposing the application.*
- interface `ApplicatioListener` é muito importante

### Dissecando um projeto

Projeto: https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example

Estrutura do projeto:

- [assets](https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example/tree/master/assets): arquivos de imagens, áudio, etc.
- [core](https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example/tree/master/core): é nesta pasta que fica a maior parte do código do projeto
- [desktop](https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example/tree/master/desktop): código específico para versão desktop. Gerado automaticamente, em geral não precisa ser modificado
- [html](https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example/tree/master/html): código específico para versão web. Gerado automaticamente, em geral não precisa ser modificado

Obs.: Esta estrutura muda dependendo de como o projeto Gradle foi gerado (versões de gdx-setup, IDE, etc.)

#### Classes

- [`class Drop extends Game`](https://github.com/AndreaInfUFSM/java-libgdx-extoended-drop-example/blob/master/core/src/com/badlogic/drop/Drop.java): configurações iniciais do jogo
- [`class MainMenuScreen implements Screen`](https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example/blob/master/core/src/com/badlogic/drop/MainMenuScreen.java): tela de abertura, com uma mensagem
- [`class GameScreen implements Screen`](https://github.com/AndreaInfUFSM/java-libgdx-extended-drop-example/blob/master/core/src/com/badlogic/drop/GameScreen.java): tela de jogo, com gerenciamento dos assets e lógica de jogo


#### Classe `Drop`

- Sobrescreve método `create` para configurar principais componentes do jogo
- Passa adiante sua referência `this` para `MainMenuScreen`, que vai continuar a execução
- Tem atributos públicos (prática duvidosa, mas pode se justificar)
- Quem cria um objeto `Drop`? É o launcher específico para desktop, web, etc.

``` java
public class Drop extends Game {
	
	public SpriteBatch batch;
	public BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		// Use libGDX's default Arial font
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render(); // important!
	}
	
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
```

#### Classe `MainMenuScreen`

- No construtor, recebe acesso ao objeto que a criou
- Observe nos atributos:

  - `final`: modificador de acesso que impede alteração/sobrescrita
  - `static`: modificador que vincula atributo/método globalmente à classe (não a cada objeto)

- Método `render`

  - Implementa lógica do que acontece nesta tela
  - Cria outra tela (classe `GameScreen`) 

``` java
public class MainMenuScreen implements Screen {
	final Drop game;
	static private int WIDTH = 800;
	static private int HEIGHT = 480;
	
	OrthographicCamera camera;
	
	public MainMenuScreen(final Drop passed_game) {
		game = passed_game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.font.draw(game.batch, "Welcome to Drop!!", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
		game.batch.end();
		
		// If player activates the game, dispose of this menu.
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
}
```


#### Classe `GameScreen`

- É a classe mais importante deste jogo
- Tem muitos atributos e métodos
- Observe o construtor!
- Tem alguns valores hardcoded (prática duvidosa)
- Descubra!

  - Como são criadas as gotas em posições aleatórias?
  - Como é detectada a colisão balde-gota?

  

``` java
public class GameScreen implements Screen {
	final Drop game;
	
	Texture dropImage;
	Texture bucketImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	SpriteBatch batch;
	Rectangle bucket;
	Vector3 touchPos;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered;
	
	public GameScreen(final Drop passed_game) {
		game = passed_game; 
		
		// Load images, 64px each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		
		// Load the drop sfx and the rain background music
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		
		// Start playback of music in bg
		rainMusic.setLooping(true);
		rainMusic.play();
		
		// Init the camera objects.
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		touchPos = new Vector3();
		
		batch = new SpriteBatch();
		
		bucket = new Rectangle();
		// Screen Width - Image Width 
		bucket.width = 64;
		bucket.x = 800 / 2 - bucket.width / 2;
		bucket.y = 20;
		bucket.height = 64;
		
		// Create Raindrops and spawn the first one.
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	@Override
	public void render(float delta) {
		/* Clear screen with a dark blue color.
		 * Arguments to ClearColor are r g b, alpha
		 */
		Gdx.gl.glClearColor(0, 0, .2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 400);
		// Draw the bucket and all the drops.
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop: raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		game.batch.end();
		
		// Process any user input
		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - bucket.width / 2;
		}
		
		// Ensure that the bucket's within the screen bounds
		if (Gdx.input.isKeyPressed(Keys.LEFT)) 
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
			bucket.x += 200 * Gdx.graphics.getDeltaTime();
		if (bucket.x < 0) 
			bucket.x = 0;
		if (bucket.x > 800 - bucket.width) 
			bucket.x = 800 - bucket.width;
		
		// Check time since last raindrop. Do we need another?
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) 
			spawnRaindrop();
		
		// Update all the raindrops
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + raindrop.height < 0) 
				iter.remove();
			if (raindrop.overlaps(bucket)) {
				dropsGathered++;
				dropSound.play();
				iter.remove();
			}
		}
	}
	
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	
	@Override
	public void dispose() {
		// Clear all the "native" resources
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		rainMusic.play();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}
```
