package com.sanelith.datatypes;

import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericTexture;

/**
 * 
 * @author Christofer Heimonen
 *
 */
public class GuiWaterDrop {
	
	public static final int WATER_FILLED = 0;
	public static final int WATER_DEPLETED = 1;
	
	private int type;
	private GenericTexture texture;
	
	public GuiWaterDrop(GenericContainer container, int type) {
		this.type = type;
		texture = new GenericTexture();
		setType(this.type);
		container.addChild(texture);
	}
	
	/**
	 * Sets the water drop type. TODO: Change from dropbox links to local files.
	 * @param type
	 */
	public void setType(int type) {
		switch(type) {
		case GuiWaterDrop.WATER_FILLED : 	
			texture.setUrl("https://dl.dropbox.com/u/44954718/myWaterDrop.png");
			break;
		case GuiWaterDrop.WATER_DEPLETED :
			texture.setUrl("https://dl.dropbox.com/u/44954718/myWaterDropDepleted.png");
			break;
		}
	}
}
