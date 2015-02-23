package radiusserver.se.nexus.interview.radius;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRadiusPackage {

	@Test
	public void testSimpleParse() {
		byte buffer[] = new byte[200];
		// Code
		buffer[0] = 1;
		// identifier
		buffer[1] = 123;
		// Length - 1
		buffer[2] = 0;
		buffer[3] = 1;
		
		RadiusPackage radiusPackage;
		try {
			radiusPackage = new RadiusPackage(buffer, 200);
			assertEquals(1, radiusPackage.code);
			assertEquals(123, radiusPackage.identifier);
			assertEquals(1, radiusPackage.length);
		} catch (SilentlyIgnoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testSimpleParse2() {
		byte buffer[] = new byte[200];
		// Code
		buffer[0] = 1;
		// identifier
		buffer[1] = 123;
		// Length - 255
		buffer[2] = 0;
		buffer[3] = (byte)255;
		
		RadiusPackage radiusPackage;
		try {
			radiusPackage = new RadiusPackage(buffer, 200);
			assertEquals(1, radiusPackage.code);
			assertEquals(123, radiusPackage.identifier);
			assertEquals(255, radiusPackage.length);
		} catch (SilentlyIgnoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testSimpleParse3() {
		byte buffer[] = new byte[200];
		// Code
		buffer[0] = 1;
		// identifier
		buffer[1] = 123;
		// Length - 256
		buffer[2] = 1;
		buffer[3] = 0;
		
		RadiusPackage radiusPackage;
		try {
			radiusPackage = new RadiusPackage(buffer, 200);
			assertEquals(1, radiusPackage.code);
			assertEquals(123, radiusPackage.identifier);
			assertEquals(256, radiusPackage.length);
		} catch (SilentlyIgnoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}


}
