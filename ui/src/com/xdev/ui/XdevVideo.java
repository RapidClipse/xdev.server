/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import java.util.List;

import com.vaadin.server.Resource;
import com.vaadin.ui.Video;


/**
 * The Video component translates into an HTML5 &lt;video&gt; element and as
 * such is only supported in browsers that support HTML5 media markup. Browsers
 * that do not support HTML5 display the text or HTML set by calling
 * {@link #setAltText(String)}.
 *
 * A flash-player fallback can be implemented by setting HTML content allowed (
 * {@link #setHtmlContentAllowed(boolean)} and calling
 * {@link #setAltText(String)} with the flash player markup. An example of flash
 * fallback can be found at the <a href=
 * "https://developer.mozilla.org/En/Using_audio_and_video_in_Firefox#Using_Flash"
 * >Mozilla Developer Network</a>.
 *
 * Multiple sources can be specified. Which of the sources is used is selected
 * by the browser depending on which file formats it supports. See <a
 * href="http://en.wikipedia.org/wiki/HTML5_video#Table">wikipedia</a> for a
 * table of formats supported by different browsers.
 *
 * @author XDEV Software
 *
 */
public class XdevVideo extends Video
{
	/**
	 *
	 */
	public XdevVideo()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 *            The caption for this video.
	 * @param source
	 *            The Resource containing the video to play.
	 */
	public XdevVideo(final String caption, final Resource source)
	{
		super(caption,source);
	}
	
	
	/**
	 * @param caption
	 *            The caption for this video.
	 */
	public XdevVideo(final String caption)
	{
		super(caption);
	}
	
	
	/**
	 * @return the first source pointed to in this media
	 */
	public Resource getSource()
	{
		final List<Resource> sources = getSources();
		if(sources != null && sources.size() > 0)
		{
			return sources.get(0);
		}
		return null;
	}
}
