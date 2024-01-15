package pbgLecture5lab_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;


public class BasicRecangle extends BasicPolygon  {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-02-05 (JBox2d version)
	 * Significant changes applied:
	 */

	public BasicRecangle(float sx, float sy, float vx, float vy, float height, float width, Color col, float mass, float rollingFriction) {
		super(sx, sy, vx, vy, height, col, mass, rollingFriction,mkRegularRecangle(4, height,width),4);
	}

	public static Path2D.Float mkRegularRecangle(int n, float height, float width) {
		
		Path2D.Float p = new Path2D.Float();
		p.moveTo(-width/2,-height/2);
	    p.lineTo(width/2,-height/2);
	    p.lineTo(width/2,height/2);
	    p.lineTo(-width/2,height/2);
	    p.lineTo(-width/2,-height/2);
		p.closePath();
		return p;
	}
}
