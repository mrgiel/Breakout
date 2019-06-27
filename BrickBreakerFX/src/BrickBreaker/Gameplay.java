package BrickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author HP
 */
public class Gameplay extends JPanel implements KeyListener, ActionListener{
    private boolean play = false;
    private boolean start = false;

    private int score = 0;
    private int totalBricks = 21;
    
    private int delay = 8;
    
    private int playerX = 310;
    
    private int ballposX = 0;
    private int ballposY = 530;
    
    double ballXdir = -2;
    double ballYdir = -3;
    private int level = 1;
    
    
    private MapGenerator map;
    private MyMouseMotionListener theMouseListener;  
    private Timer timer;
    
    public Gameplay(){
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        theMouseListener = new MyMouseMotionListener();
        addMouseMotionListener(theMouseListener);
        
    }
    
    public void paint(Graphics g){
        //Background
      g.setColor(Color.BLACK);
      g.fillRect(1, 1, 692, 592);
      //Map
      map.draw((Graphics2D)g);
        //Paddle
      g.setColor(Color.GREEN);
      g.fillRect(playerX,550,100,8);
        //Ball
      g.setColor(Color.RED);
      g.fillOval(ballposX, ballposY, 20,20);
      //score
      g.setColor(Color.GREEN);
      g.setFont(new Font("serif",Font.BOLD, 25));
      g.drawString("Score: "+score, 580, 30);
      g.drawString("Level: "+level, 10, 30);

       if(!start){
          ballYdir = 0;
          g.setColor(Color.GREEN);
          g.setFont(new Font("serif",Font.BOLD, 30));
          g.drawString("Press Enter to Start!", 200, 300);
       }
        
          
      if(totalBricks <= 0){ // Winst
          
          play = false;
          ballposY = 530;
          g.setColor(Color.GREEN);
          g.setFont(new Font("serif",Font.BOLD, 30));
          g.drawString("You Won, Scores: "+score, 200, 300);
          g.setFont(new Font("serif",Font.BOLD, 20));
          g.drawString("Press Enter to Continue", 250, 350);          
      }
   
      if(ballposY > 570){ //AF!
          play = false;
          ballXdir = 0;
          ballYdir = 0;
          g.setColor(Color.GREEN);
          g.setFont(new Font("serif",Font.BOLD, 30));
          g.drawString("Game Over, Scores: "+score, 200, 300);
          
          g.setFont(new Font("serif",Font.BOLD, 20));
          g.drawString("Press Enter to Restart", 250, 350);
          
      }
      
      g.dispose();
    }
 
    
    private class MyMouseMotionListener implements MouseMotionListener{

        @Override
        public void mouseMoved(MouseEvent e) {
            playerX = e.getX()-50;
            e.consume();
            if(!play){
                play=false;
              ballposX = playerX +40;  
            }
        }
        

        @Override
        public void mouseDragged(MouseEvent e) {}
    }
    
    
    public void mousePressed(KeyEvent e) {
        play();
    }    
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==78){
            totalBricks = 0;
        }
        /*
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >=600){
                playerX = 600;
            } else{
                    moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX <=10){
                playerX = 10;
            } else{
                moveLeft();  
            }    
        }*/
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play && totalBricks > 0){ //Verloren of Beginnen
                
                level = 1;
                start= true;
                play = true;
                ballposY = 530;
                ballposX = playerX +40;
                ballXdir = -2;
                ballYdir = -3;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);
                
                repaint();
                play();
            }
            if(!play && totalBricks == 0){ //Gewonnen
                
                level = level +1;
                start= true;
                play = true;
                ballposY = 530;
                ballposX = playerX +40;
                ballXdir = ballXdir*1.25;
                ballYdir = ballYdir*1.25;
                score = score;
                totalBricks = 21;
                map = new MapGenerator(3,7);
                
                repaint();
                play();
            }
        }
            
   }  

    public void play(){
        play=true;
        
    }
    public void start(){
        play=true;
    }
    
    public void moveRight(){
    if(play){
    playerX+=20;
}
    }

    public void moveLeft(){
    if(play){
    playerX-=20;        
    }
    }
        
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX,550, 100, 8))){
             ballYdir =-ballYdir;   
            }
            
            A:  for(int i = 0; i < map.map.length; i++){
                    for(int j= 0; j<map.map[0].length;j++){
                        if(map.map[i][j] > 0){
                          int brickX = j* map.brickWidth+80;
                          int brickY = i*map.brickHeight+50;
                          int brickWidth = map.brickWidth;
                          int brickHeight = map.brickHeight;
                      
                      Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                      Rectangle ballRect = new Rectangle(ballposX, ballposY, 20,20);
                      Rectangle brickRect = rect;
                      
                      if(ballRect.intersects(brickRect)){
                          map.setBrickValue(0,i,j);
                          totalBricks--;
                          score+= 1;
                          
                          if(ballposX+19 <= brickRect.x || ballposX +1 >= brickRect.x + brickRect.width){
                             ballXdir = -ballXdir; 
                          }else{
                              ballYdir= -ballYdir;
                          }
                          break A;
                      }
                  }  
                }
                
            }
            
               ballposX += ballXdir;
               ballposY += ballYdir;
               if(ballposX < 0) {
               ballXdir = -ballXdir;
   }
                if(ballposY < 0) {
                ballYdir = -ballYdir;
   }
            if(ballposX > 670){
                ballXdir = -ballXdir;
            }            
        }
        repaint();
  }
    
    
    
    
    
    
    
    
    
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}   
}
