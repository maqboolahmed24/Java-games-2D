package pbgLecture5lab_wrapperForJBox2D;

import java.awt.Color;
import javax.swing.JOptionPane;


//for music lol 
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;



import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import org.jbox2d.dynamics.joints.MouseJoint;


public class BasicPhysicsEngineUsingBox2D {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-02-05 (JBox2d version)
	 * Significant changes applied:
	 */

	// frame dimensions
	public static final int SCREEN_HEIGHT = 680;
	public static final int SCREEN_WIDTH = 640;
	public static final Dimension FRAME_SIZE = new Dimension(
			SCREEN_WIDTH, SCREEN_HEIGHT);
	public static final float WORLD_WIDTH=10;//metres
	public static final float WORLD_HEIGHT=SCREEN_HEIGHT*(WORLD_WIDTH/SCREEN_WIDTH);// meters - keeps world dimensions in same aspect ratio as screen dimensions, so that circles get transformed into circles as opposed to ovals
	public static final float GRAVITY=20f;
	public static final boolean ALLOW_MOUSE_POINTER_TO_DRAG_BODIES_ON_SCREEN=true;// There's a load of code in basic mouse listener to process this, if you set it to true

	public static World world; // Box2D container for all bodies and barriers 
	
	// sleep time between two drawn frames in milliseconds 
	public static final int DELAY = 20;
	public static final int NUM_EULER_UPDATES_PER_SCREEN_REFRESH=300;
	// estimate for time between two frames in seconds 
	public static final float DELTA_T = DELAY / 1000.0f;
	public static int convertWorldXtoScreenX(float worldX) {
		return (int) (worldX/WORLD_WIDTH*SCREEN_WIDTH);
	}
	public static int convertWorldYtoScreenY(float worldY) {
		// minus sign in here is because screen coordinates are upside down.
		return (int) (SCREEN_HEIGHT-(worldY/WORLD_HEIGHT*SCREEN_HEIGHT));
	}
	public static float convertWorldLengthToScreenLength(float worldLength) {
		return (worldLength/WORLD_WIDTH*SCREEN_WIDTH);
	}
	public static float convertScreenXtoWorldX(int screenX) {
		return screenX*WORLD_WIDTH/SCREEN_WIDTH;
	}
	public static float convertScreenYtoWorldY(int screenY) {
		return (SCREEN_HEIGHT-screenY)*WORLD_HEIGHT/SCREEN_HEIGHT;
	}
	public List<BasicParticle> particles;
	public List<BasicPolygon> polygons;
	public List<AnchoredBarrier> barriers;
	public List<ElasticConnector> connectors;
	public static MouseJoint mouseJointDef;
	public static enum LayoutMode {RECTANGLE};
	
	
	public BasicPhysicsEngineUsingBox2D() {
		
		world = new World(new Vec2(0, -GRAVITY));// create Box2D container for everything
		world.setContinuousPhysics(true);

		particles = new ArrayList<BasicParticle>();
		polygons = new ArrayList<BasicPolygon>();
		barriers = new ArrayList<AnchoredBarrier>();
		connectors=new ArrayList<ElasticConnector>();
		LayoutMode layout=LayoutMode.RECTANGLE;

		float linearDragForce=0.02f;
		float vx=0;
		float vy=0;
		float sx=(WORLD_WIDTH/2)-4;
		float sy=(WORLD_HEIGHT/2)-1.7f;		 
	
		 BasicPolygon pp2=new BasicRecangle(sx,sy,vx,vy, 0.1f,0.6f,Color.black, 100, linearDragForce); 
		 polygons.add(pp2);
		
		barriers = new ArrayList<AnchoredBarrier>();
		
		switch (layout) {
			case RECTANGLE: 
			{
				barriers.add(new AnchoredBarrier_StraightLine(0, 0, WORLD_WIDTH*7, 0, Color.red));
				barriers.add(new AnchoredBarrier_StraightLine(0, 0, (WORLD_WIDTH*7)+(WORLD_WIDTH/2.227f), 0, Color.red));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH*8, WORLD_HEIGHT, 0, WORLD_HEIGHT, Color.black));
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT, 0, 0, Color.black));
				{
					barriers.add(new AnchoredBarrier_StraightLine(0, (WORLD_HEIGHT/2)-2f, (WORLD_WIDTH/2)-1.7f, (WORLD_HEIGHT/2)-2f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine((WORLD_WIDTH/2)-1.7f, (WORLD_HEIGHT/2)-2, (WORLD_WIDTH/2)-0.7f, ((WORLD_HEIGHT/2)+0.5f)-2, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine((WORLD_WIDTH/2)+0.7f, ((WORLD_HEIGHT/2)+0.5f)-2, (WORLD_WIDTH/2)+1.7f, (WORLD_HEIGHT/2)-2, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine((WORLD_WIDTH/2)+1.7f, (WORLD_HEIGHT/2)-2, WORLD_WIDTH-1, (WORLD_HEIGHT/2)-2, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH-1, (WORLD_HEIGHT/2)-2, WORLD_WIDTH-0.45f, (WORLD_HEIGHT/2)-1.5f, Color.black));
				}
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+1.5f, (WORLD_HEIGHT/2)-0.5f, WORLD_WIDTH, (WORLD_HEIGHT/2)-2, Color.black));
				{
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+2, (WORLD_HEIGHT/2)-2, WORLD_WIDTH+2.5f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+2.5f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+3f, (WORLD_HEIGHT/2)-2, Color.black));
					
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+3, (WORLD_HEIGHT/2)-2, WORLD_WIDTH+3.5f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+3.5f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+4f, (WORLD_HEIGHT/2)-2, Color.black));
					
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+4, (WORLD_HEIGHT/2)-2, WORLD_WIDTH+4.5f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+4.5f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+5f, (WORLD_HEIGHT/2)-2, Color.black));
					
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+5, (WORLD_HEIGHT/2)-2, WORLD_WIDTH+5.5f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+5.5f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+6f, (WORLD_HEIGHT/2)-2, Color.black));
					
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+6, (WORLD_HEIGHT/2)-2, WORLD_WIDTH+6.5f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+6.5f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+7f, (WORLD_HEIGHT/2)-2, Color.black));
					
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+7, (WORLD_HEIGHT/2)-2, WORLD_WIDTH+7.5f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+7.5f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+8f, (WORLD_HEIGHT/2)-2, Color.black));
				}
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+8f, (WORLD_HEIGHT/2)-2f, WORLD_WIDTH+9f, (WORLD_HEIGHT/2)-2, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+9f, (WORLD_HEIGHT/2)-2f, WORLD_WIDTH+10f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+10f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+10.5f, (WORLD_HEIGHT/2)-0.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+12f, (WORLD_HEIGHT/2)+0.5f, WORLD_WIDTH+15f, (WORLD_HEIGHT/2)+0.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+12f, (WORLD_HEIGHT/2)+0.5f, WORLD_WIDTH+12f, (WORLD_HEIGHT/2), Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+15f, (WORLD_HEIGHT/2)+0.5f, WORLD_WIDTH+15.5f, (WORLD_HEIGHT/2), Color.black));
				
				{
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+18f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+19.5f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+19.5f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+20f, (WORLD_HEIGHT/2)-2f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+18f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+17.6f, (WORLD_HEIGHT/2)-2.5f, Color.black));
					
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+15.7f, (WORLD_HEIGHT/2)-3f, WORLD_WIDTH+14.5f, (WORLD_HEIGHT/2)-3f, Color.black));
					
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+18f, (WORLD_HEIGHT/2)-4.5f, WORLD_WIDTH+19.5f, (WORLD_HEIGHT/2)-4.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+19.5f, (WORLD_HEIGHT/2)-4.5f, WORLD_WIDTH+20.5f, (WORLD_HEIGHT/2)-3.5f, Color.black));
					
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+24f, (WORLD_HEIGHT/2)-4.5f, WORLD_WIDTH+26.5f, (WORLD_HEIGHT/2)-4.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+26.5f, (WORLD_HEIGHT/2)-4.5f, WORLD_WIDTH+32f, (WORLD_HEIGHT/2)-1.5f, Color.black));
				}
				{
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+39f, (WORLD_HEIGHT/2)-2.5f, WORLD_WIDTH+42f, (WORLD_HEIGHT/2)-2.5f, Color.black));
					barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+42f, (WORLD_HEIGHT/2)-3f, WORLD_WIDTH+42f, (WORLD_HEIGHT/2)-2.5f, Color.black));
					
					 polygons.add(new BasicRecangle(WORLD_WIDTH+41f,(WORLD_HEIGHT/2)-1f,0,0, 0.48f,0.48f,Color.RED, 500, 0.02f));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+42f, (WORLD_HEIGHT/2)-3f, WORLD_WIDTH+42.6f, (WORLD_HEIGHT/2)-3f, Color.black));

					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+42.6f, (WORLD_HEIGHT/2)-3f, WORLD_WIDTH+42.6f, (WORLD_HEIGHT/2)-2.52f, Color.black));		
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+42.6f, (WORLD_HEIGHT/2)-2.52f, WORLD_WIDTH+45f, (WORLD_HEIGHT/2)-2.5f, Color.black));
					 
					 polygons.add(new BasicRecangle(WORLD_WIDTH+44f,(WORLD_HEIGHT/2)-1f,0,0, 0.48f,0.48f,Color.magenta, 500, 0.02f));
					 
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+45f, (WORLD_HEIGHT/2)-2.5f, WORLD_WIDTH+45f, (WORLD_HEIGHT/2)-3f, Color.black));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+45f, (WORLD_HEIGHT/2)-3f, WORLD_WIDTH+45.6f, (WORLD_HEIGHT/2)-3f, Color.black));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+45.6f, (WORLD_HEIGHT/2)-3f, WORLD_WIDTH+45.6f, (WORLD_HEIGHT/2)-2.52f, Color.black));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+45.6f, (WORLD_HEIGHT/2)-2.52f, WORLD_WIDTH+49f, (WORLD_HEIGHT/2)-2.5f, Color.black));
					 
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+49f, (WORLD_HEIGHT/2)-2.5f, WORLD_WIDTH+51f, (WORLD_HEIGHT/2)-2.2f, Color.black));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+51f, (WORLD_HEIGHT/2)-2.2f, WORLD_WIDTH+53f, (WORLD_HEIGHT/2)-1.5f, Color.black));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+53f, (WORLD_HEIGHT/2)-1.5f, WORLD_WIDTH+55f, (WORLD_HEIGHT/2)-0.4f, Color.black));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+55f, (WORLD_HEIGHT/2)-0.4f, WORLD_WIDTH+57f, (WORLD_HEIGHT/2)+1.2f, Color.black));
					 
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+64.5f, (WORLD_HEIGHT/2), WORLD_WIDTH+64.5f, 0, Color.black));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+64.5f, (WORLD_HEIGHT/2), WORLD_WIDTH+70f, (WORLD_HEIGHT/2), Color.black));
					 barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH+70f, (WORLD_HEIGHT/2), WORLD_WIDTH+70f, (WORLD_HEIGHT), Color.black));	 
				}
				break;
			}
		}
	}

	
	public static void main(String[] args) throws Exception {
		final BasicPhysicsEngineUsingBox2D game = new BasicPhysicsEngineUsingBox2D();
		final BasicView view = new BasicView(game);
		JEasyFrame frame = new JEasyFrame(view, "Basic Physics Engine");
		frame.addKeyListener(new BasicKeyListener());
		view.addMouseMotionListener(new BasicMouseListener());
		game.startThread(view);
	}
	
	
	public boolean timerStopped = false;
	public void stopTimer() {
	    timerStopped = true;
	}
	
	
	public float elapsedTime = 0.0f;
	private void startThread(final BasicView view) throws InterruptedException {
		final BasicPhysicsEngineUsingBox2D game=this;
		
		game.env();
		
		bgMusic("musicResouces/bgmusic.wav");
		JOptionPane.showMessageDialog(null, "Maximize the Window for Better Experience", "Welcome to my Game", JOptionPane.INFORMATION_MESSAGE);
		JOptionPane.showMessageDialog(null, "Right = Accelerate\nLeft = Decelerate\nSpace = Brake\nRight/Left + Shift = Boost the speed\nRight + Up = Perform front wheelie\nRight + Down = Perform rear wheelie", "Key Controls", JOptionPane.INFORMATION_MESSAGE);
		JOptionPane.showMessageDialog(null, "rule1- Complete the track to win the game\nrule2- Don't get fall from the main track\nrule3- Don't follow the rules", "Challenge", JOptionPane.INFORMATION_MESSAGE);
		
		while (true) {
			game.torquerotation();
			game.gameover();
			game.win();
			
			game.update();
			view.repaint();
			if (!timerStopped) {
		        elapsedTime += DELTA_T;
		    }
			
			Toolkit.getDefaultToolkit().sync();
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
			}
		}
	}

	
	public void env()
	{
		float linearDragForce=0.02f;
		float vx=0;
		float vy=0;
		float sx=(WORLD_WIDTH/2)-4;
		float sy=(WORLD_HEIGHT/2)-1.7f;
		
		 BasicParticle p01 = new BasicParticle(sx,sy,0,0,.2f,Color.black, 0.8f, linearDragForce);
		 particles.add(p01);
	 
		 BasicParticle p02 = new BasicParticle(sx+0.5f,sy,vx,vy,.2f,Color.black, 0.8f, linearDragForce);
		 particles.add(p02);		 
		 
	   {
		   	BasicParticle p1 = particles.get(0);
			BasicParticle p2 = particles.get(1);
			BasicPolygon base = polygons.get(0);

			RevoluteJointDef jointDef=new RevoluteJointDef();
		 
			jointDef.bodyA = p1.body;
			jointDef.bodyB = base.body;
			jointDef.collideConnected = false; 
			
			jointDef.localAnchorA=new Vec2(0,0);
			jointDef.localAnchorB=new Vec2(-0.3f,0); 
			world.createJoint(jointDef);
		 
			jointDef.bodyA = p2.body;
			jointDef.bodyB = base.body;
			jointDef.collideConnected = false; 
			jointDef.localAnchorA=new Vec2(0,0);
			jointDef.localAnchorB=new Vec2(0.3f,0); 
			world.createJoint(jointDef);
		 } 
	}

	
	public boolean printt = false;
	public void gameover() 
	{
	    boolean oo = true;
	   
	    for (BasicPolygon pp1 : polygons) 
	    {
	        if (polygons.get(0).body.getPosition().y < (WORLD_HEIGHT / 15)) 
	        {
	            continue;
	        } 
	        else 
	        {
	            oo = false;
	            break;
	        }
	    }
	    if (oo && !printt) 
	    {
	    	soundeffect("musicResouces/oohnoo.wav");
	    	int gameover = JOptionPane.showConfirmDialog(null, "Would you like to restart the game?", "GAME OVER", JOptionPane.YES_NO_OPTION);
	        
	    	if (gameover == JOptionPane.YES_OPTION) 
	        {
	        	stopTimer();
	            resetGame();  
	        }
	        
	        else if (gameover == JOptionPane.NO_OPTION)
	        {
	        	int loss = JOptionPane.showConfirmDialog(null, "You want to close window?", "CLOSE?", JOptionPane.YES_NO_OPTION);
		        
	        	if (loss == JOptionPane.YES_OPTION) 
		        {
		        	System.exit(0);
		        }
		        
		        else if (loss == JOptionPane.NO_OPTION)
		        {
		        	elapsedTime = 0;
		        	stopTimer();
		        	printt=true;
		        }
	        }
	    }
	}
	
	
	public void win() 
	{
		boolean oo=true;
	    for (BasicPolygon pp1  : polygons) 
	    {
	    	if ( oo && polygons.get(0).body.getPosition().x>((WORLD_WIDTH*7)+(WORLD_WIDTH/1.5f))) 
	    	{	
	    		continue;   	
	    	}
	    	else 
	    	{
	    		oo=false;
	    		break;
	    	}	    	
	    }
	    if(oo && !printt) 
	    {
	    	soundeffect("musicResouces/clap.wav");
	    	int win = JOptionPane.showConfirmDialog(null, "          Your Score:"+Math.round(elapsedTime)+ "\nWould you like to try again for a better score?", "YOU WON", JOptionPane.YES_NO_OPTION);
	        if (win == JOptionPane.YES_OPTION) 
	        {
	        	stopTimer();
	            resetGame();  
	        }
	        else if (win == JOptionPane.NO_OPTION)
	        {
	        	int winn = JOptionPane.showConfirmDialog(null, "          Your Score:"+Math.round(elapsedTime)+ "\nYou want to close window?", "CLOSE?", JOptionPane.YES_NO_OPTION);
		        if (winn == JOptionPane.YES_OPTION) 
		        {
		        	System.exit(0);
		        }
		        else if (winn == JOptionPane.NO_OPTION)
		        {
		        	elapsedTime = 0;
		        	stopTimer();
		        	printt=true;    
		        }
	        }
	    }  
	}
	 	
	
	public void torquerotation() 
	{
		BasicParticle pp = particles.get(0);
	    BasicParticle qq = particles.get(1);

	    float ltorque = 3f;
	    float rtorque = -3f;

	    if (BasicKeyListener.isRotateLeftKeyPressed() == true) {
	        pp.body.applyTorque(ltorque);
	        qq.body.applyTorque(ltorque);
	        
	        if (BasicKeyListener.isShiftKeyPressed() == true) 
	        {
	            pp.body.applyTorque(-(rtorque*2));
	            qq.body.applyTorque(-(rtorque*2));
	        }

	        if (BasicKeyListener.isSpaceKeyPressed() == true) {
	            pp.body.setAngularVelocity(0);
	            qq.body.setAngularVelocity(0);
	        }
	    }
	    else if (BasicKeyListener.isRotateRightKeyPressed() == true) 
	    {
	        pp.body.applyTorque(rtorque);
	        qq.body.applyTorque(rtorque);

	        if (BasicKeyListener.isShiftKeyPressed() == true) 
	        {
	            pp.body.applyTorque(rtorque * 2);
	            qq.body.applyTorque(rtorque * 2);
	        }
	        
	        if (BasicKeyListener.isSpaceKeyPressed() == true) 
	        {
	            pp.body.setAngularVelocity(0);
	            qq.body.setAngularVelocity(0);
	        }
	        
	        if (BasicKeyListener.isDownKeyPressed() == true) 
	        {
	        	float wheelieTorque = -10;
	            for (BasicPolygon pp1 : polygons) 
	            {
	                pp1.body.applyTorque(wheelieTorque);
	            }
	        }
	        
	        if (BasicKeyListener.isThrustKeyPressed() == true) 
	        {
	        	float wheelieTorque = 10;
	            for (BasicPolygon pp1 : polygons) 
	            {
	                pp1.body.applyTorque(wheelieTorque);
	            }   
	        }
	    }
	    
	    if (BasicKeyListener.isSpaceKeyPressed() == true) {
	    	pp.body.setAngularVelocity(0);
            qq.body.setAngularVelocity(0);
	    }	
	}
	
	
	public void resetGame() 
	{
	    world = new World(new Vec2(0, -GRAVITY));
	    particles.clear();
	    polygons.clear();
	    barriers.clear();
	    connectors.clear();

	    BasicPhysicsEngineUsingBox2D newGame = new BasicPhysicsEngineUsingBox2D();
	    elapsedTime = 0.0f;
	    printt = false;
	    timerStopped = false;
	    
	    this.particles = newGame.particles;
	    this.polygons = newGame.polygons;
	    this.barriers = newGame.barriers;
	    this.connectors = newGame.connectors;
	    this.env();
	    this.gameover();
}
	

	public void bgMusic(String path) 
	{
	    try 
	    {
	        File musicFile = new File(path);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.loop(Clip.LOOP_CONTINUOUSLY);
	        clip.start();
	    } 
	    catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	
	public void soundeffect(String path) 
	{
	    try 
	    {
	        File musicFile = new File(path);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } 
	    catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	
	public void update() {
		int VELOCITY_ITERATIONS=NUM_EULER_UPDATES_PER_SCREEN_REFRESH;
		int POSITION_ITERATIONS=NUM_EULER_UPDATES_PER_SCREEN_REFRESH;
		for (BasicParticle p:particles) {
			// give the objects an opportunity to add any bespoke forces, e.g. rolling friction
			p.notificationOfNewTimestep();
		}
		for (BasicPolygon p:polygons) {
			// give the objects an opportunity to add any bespoke forces, e.g. rolling friction
			p.notificationOfNewTimestep();
		}
		world.step(DELTA_T, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}

}


