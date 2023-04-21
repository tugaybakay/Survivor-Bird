package com.tugaybakay.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	Circle birdCircle;
	ShapeRenderer shapeRenderer;
	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture tree;
	float treeX;
	float screenWidth;
	float screenHeight;
	float velocity = 1;
	float gravity= 0.4f;
	float velocityOfEnemies = 6.5F;
	float velocityOfTree = 4;
	int score = 0;
	int scoredEnemy = 0;
	Random random = new Random();
	float birdX;
	float birdY;
	byte gameStatement = 0;
	byte numberOfEnemies = 3;
	float[] enemyX = new float[numberOfEnemies];
	float[] backgroundX = new float[2];
	float[] enemyY1 = new float[numberOfEnemies];
 	float[] enemyY2 = new float[numberOfEnemies];
	 float[] enemyY3 = new float[numberOfEnemies];
	BitmapFont font;
 	Circle[] enemyCircle1 = new Circle[numberOfEnemies];
	Circle[] enemyCircle2 = new Circle[numberOfEnemies];
	Circle[] enemyCircle3 = new Circle[numberOfEnemies];
	Texture enemy;
	float distance;
	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		background = new Texture("background.png");
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(2.5f);
		tree = new Texture("tree.png");
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		distance = screenWidth/2;
		treeX = screenWidth;
		bird = new Texture("bird.png");
		birdX = screenWidth/6.5f;
		birdY = screenHeight/3;
		enemy = new Texture("enemy.png");
		for(int i=0;i<2;i++){
			backgroundX[i] = screenWidth * i;
		}
		for(int i=0;i<numberOfEnemies;i++){
			enemyX[i] = screenWidth + (distance * i);
			enemyY1[i] = random.nextFloat() * screenHeight/2 + screenHeight/2;
			enemyY2[i] = screenHeight/2;
			enemyY3[i] = screenHeight/2 - random.nextFloat()*screenHeight/2;
			enemyCircle1[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();
		}
	}

	@Override
	public void render () {
		batch.begin();

		for(int i=0;i<2;i++){
			batch.draw(background, backgroundX[i], 0,screenWidth,screenHeight);
		}
		batch.draw(tree,treeX,screenHeight/10,screenWidth/8,screenHeight/2.5f);
		batch.draw(bird,birdX,birdY,screenWidth/18,screenHeight/9);
		font.draw(batch,String.valueOf(score),screenWidth/13,screenHeight/5);
		birdCircle.set(birdX+screenWidth/36,birdY+screenHeight/18,screenWidth/40);

		if(gameStatement == 1){
			if(enemyX[scoredEnemy] < birdX){
				score++;
				if(scoredEnemy<2)scoredEnemy++;
				else scoredEnemy = 0;
			}
			gravityForBird();
			drawEnemies();
			collisionControl();
			if(Gdx.input.justTouched() && velocity>0){
				velocity = -screenHeight/80;
			}
		}else if(gameStatement==2){
			font.draw(batch,"Game Over! Tap to play again",screenWidth/20,screenHeight/1.5f);
			birdX = screenWidth/6.5f;
			birdY = screenHeight/3;
			/*for(int i=0;i<2;i++){
				backgroundX[i] = screenWidth * i;
			}*/
			for(int i=0;i<numberOfEnemies;i++){
				enemyX[i] = screenWidth + (distance * i);
				enemyY1[i] = random.nextFloat() * screenHeight/2 + screenHeight/2;
				enemyY2[i] = screenHeight/2;
				enemyY3[i] = screenHeight/2 - random.nextFloat()*screenHeight/2;
				enemyCircle1[i] = new Circle();
				enemyCircle2[i] = new Circle();
				enemyCircle3[i] = new Circle();
			}
			velocity = 1;
			scoredEnemy = 0;
			score = 0;

			if(Gdx.input.justTouched())gameStatement=1;
		}else if(Gdx.input.justTouched()){
			gameStatement=1;
		}
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

	public void gravityForBird(){
		if(birdY>0 && birdY<screenHeight-screenHeight/20){
			birdY -= velocity;
			velocity += gravity;
		}else gameStatement=2;
	}

	public void drawEnemies(){

		for(int i=0;i<numberOfEnemies;i++){
			batch.draw(enemy,enemyX[i],enemyY1[i],screenWidth/18,screenHeight/9);
			batch.draw(enemy,enemyX[i],enemyY2[i],screenWidth/18,screenHeight/9);
			batch.draw(enemy,enemyX[i],enemyY3[i],screenWidth/18,screenHeight/9);
			enemyCircle1[i].set(enemyX[i]+screenWidth/36,enemyY1[i]+screenHeight/18,screenWidth/40);
			enemyCircle2[i].set(enemyX[i]+screenWidth/36,enemyY2[i]+screenHeight/18,screenWidth/40);
			enemyCircle3[i].set(enemyX[i]+screenWidth/36,enemyY3[i]+screenHeight/18,screenWidth/40);

		}
		for(int i =0;i<numberOfEnemies;i++){
			enemyX[i] -= velocityOfEnemies;
			if(enemyX[i]<0){
				enemyX[i]=screenWidth+distance;
				enemyY1[i] = random.nextFloat() * screenHeight/2 + screenHeight/2+screenHeight/9;
				enemyY2[i] = screenHeight/2;
				enemyY3[i] = screenHeight/2 -screenHeight/9 - random.nextFloat()*screenHeight/2;
			}
		}
		treeX -= velocityOfTree;
		if(treeX<-screenWidth/8)treeX=screenWidth;
		for(int i=0;i<2;i++){
			backgroundX[i] -= velocityOfTree;
			if(backgroundX[i]<=(-screenWidth))backgroundX[i] = screenWidth-10;
		}
	}

	public void collisionControl(){
		for(int i=0;i<numberOfEnemies;i++){
			if(Intersector.overlaps(birdCircle,enemyCircle1[i]) || Intersector.overlaps(birdCircle,enemyCircle2[i]) || Intersector.overlaps(birdCircle,enemyCircle3[i])){
				gameStatement = 2;
			}
		}
		}


}
