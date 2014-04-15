package au.org.scoutmaster.domain;

import javax.persistence.Embeddable;

import au.com.vaadinutils.domain.iColor;

/**
 * A wrapper class for the color pickers Color class so we can embed a Color in
 * a JPA Entity.
 *
 * @author bsutton
 *
 */
@Embeddable
public class Color implements iColor
{
	private int red;
	private int green;
	private int blue;
	private int alpha;

	public Color()
	{
		/** Default to White **/
		this.red = 255;
		this.green = 255;
		this.blue = 255;
		this.alpha = 255;
	}

	public Color(final com.vaadin.shared.ui.colorpicker.Color color)
	{
		setRed(color.getRed());
		setGreen(color.getGreen());
		setBlue(color.getBlue());
		setAlpha(color.getAlpha());
	}

	public String getCSS()
	{
		return new com.vaadin.shared.ui.colorpicker.Color(getRed(), getGreen(), getBlue(), getAlpha()).getCSS();
	}

	@Override
	public String toString()
	{
		return getCSS();
	}

	@Override
	public int getRed()
	{
		return this.red;
	}

	public void setRed(final int red)
	{
		this.red = red;
	}

	@Override
	public int getGreen()
	{
		return this.green;
	}

	public void setGreen(final int green)
	{
		this.green = green;
	}

	@Override
	public int getBlue()
	{
		return this.blue;
	}

	public void setBlue(final int blue)
	{
		this.blue = blue;
	}

	@Override
	public int getAlpha()
	{
		return this.alpha;
	}

	public void setAlpha(final int alpha)
	{
		this.alpha = alpha;
	}

}