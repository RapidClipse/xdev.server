/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
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
 * by the browser depending on which file formats it supports. See
 * <a href="http://en.wikipedia.org/wiki/HTML5_video#Table">wikipedia</a> for a
 * table of formats supported by different browsers.
 *
 * @author XDEV Software
 *		
 */
public class XdevVideo extends Video implements XdevComponent
{
	private final Extensions extensions = new Extensions();
	
	
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


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}
}
