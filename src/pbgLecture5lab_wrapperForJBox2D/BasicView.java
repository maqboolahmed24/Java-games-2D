package pbgLecture5lab_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import org.jbox2d.common.Vec2;

public class BasicView extends JComponent {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-01-28
	 * Significant changes applied:
	 */
	// background colour
	public static final Color BG_COLOR = Color.orange;

	private BasicPhysicsEngineUsingBox2D game;

	public BasicView(BasicPhysicsEngineUsingBox2D game) {
		this.game = game;
	}
	
	@Override
	public void paintComponent(Graphics g0) {
	    BasicPhysicsEngineUsingBox2D game;
	    synchronized (this) {
	        game = this.game;
	    }
	    Graphics2D g = (Graphics2D) g0;

	    
	    Vec2 particlePosition = game.polygons.get(0).body.getPosition(); // Assuming BasicParticle 01 is the first element in the list
	    double oX = getWidth()/3 - BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(particlePosition.x);
	    double oY = getHeight()/4 - BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(particlePosition.y);

	    int offsetX=(int)oX;
	    int offsetY=(int)oY;
	    		
	    
	    g.translate(offsetX, offsetY);

	    Color[] gradientColors = {Color.pink, Color.yellow, Color.orange, Color.green, Color.BLUE, Color.MAGENTA};
	    float[] gradientFractions = {0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
	    
	    LinearGradientPaint gradientPaint = new LinearGradientPaint(
	            new Point2D.Float(offsetX, offsetY),
	            new Point2D.Float(getWidth()-offsetX, offsetY),
	            gradientFractions,
	            gradientColors);
	    
	    g.setPaint(gradientPaint);
	    g.fillRect(-offsetX, -offsetY, getWidth(), getHeight());

	    for (BasicParticle p : game.particles)
	        p.draw(g);
	    for (BasicPolygon p : game.polygons)
	        p.draw(g);
	    for (ElasticConnector c : game.connectors)
	        c.draw(g);
	    for (AnchoredBarrier b : game.barriers)
	        b.draw(g);
	    
	    g.translate(-offsetX, -offsetY);
	    drawTimer(g);
	}

	public void drawTimer(Graphics2D g) {
	    g.setColor(Color.white);
	    g.setFont(new Font("Arial", Font.LAYOUT_RIGHT_TO_LEFT, 35));
	    g.drawString("Score: " + String.format("%.1f", game.elapsedTime), 20, 30);
	}

	@Override
	public Dimension getPreferredSize() {
		return BasicPhysicsEngineUsingBox2D.FRAME_SIZE;
	}
	
	public synchronized void updateGame(BasicPhysicsEngineUsingBox2D game) {
		this.game=game;
	}
}