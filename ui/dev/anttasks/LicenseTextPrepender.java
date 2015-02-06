
package anttasks;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * Custom ant task for generating the license information on top of all java
 * source files.
 */
public class LicenseTextPrepender extends Task
{
	
	private File			inputfile;
	private File			srcdir;
	
	private List<String>	licenseText	= new ArrayList<String>();
	
	
	@Override
	public void execute() throws BuildException
	{
		if(!inputfile.exists())
		{
			throw new BuildException("The specified license file +" + inputfile + " does not exist");
		}
		if(!srcdir.isDirectory())
		{
			throw new BuildException("The specified source destination " + srcdir
					+ " is not a directory.");
		}
		
		licenseText = readLicenseText(inputfile);
		
		traverse(srcdir);
	}
	
	
	private void traverse(File file)
	{
		// recursively traverse src directory, ignore svn-Directories (starting
		// with a .)
		if(file.isDirectory() && !file.getName().startsWith("."))
		{
			String[] children = file.list();
			for(int i = 0; i < children.length; i++)
			{
				if(file.isDirectory())
				{
					traverse(new File(file,children[i]));
				}
				else if(file.isFile())
				{
					processFile(file);
				}
			}
		}
		else if(file.isFile())
		{
			processFile(file);
		}
		
	}
	
	
	private void processFile(File file)
	{
		if(file.getName().endsWith(".java"))
		{
			this.log("Processing: " + file.getAbsoluteFile() + File.pathSeparator);
			List<String> source = readSource(file);
			writeSource(file,licenseText,source);
		}
	}
	
	
	private List<String> readLicenseText(File file) throws BuildException
	{
		List<String> list = new ArrayList<String>();
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while((line = in.readLine()) != null)
			{
				list.add(line);
			}
			in.close();
		}
		catch(IOException e)
		{
			throw new BuildException(e);
		}
		return list;
	}
	
	
	private List<String> readSource(File file) throws BuildException
	{
		List<String> list = new ArrayList<String>();
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			boolean inLeadingComment = true;
			while((line = in.readLine()) != null)
			{
				if(inLeadingComment)
				{
					if(line.trim().startsWith("package") || line.trim().startsWith("import"))
					{
						inLeadingComment = false;
					}
				}
				if(!inLeadingComment)
				{
					list.add(line);
				}
			}
			in.close();
		}
		catch(IOException e)
		{
			throw new BuildException(e);
		}
		return list;
	}
	
	
	private void writeSource(File outputFile, List<String> licenseText, List<String> source)
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
			List<String> output = new ArrayList<String>();
			output.addAll(licenseText);
			output.addAll(source);
			for(String line : output)
			{
				out.write(line);
				out.newLine();
			}
			out.close();
		}
		catch(IOException e)
		{
			throw new BuildException(e);
		}
	}
	
	
	public void setInputfile(File inputfile)
	{
		this.inputfile = inputfile;
	}
	
	
	public void setSrcdir(File srcdir)
	{
		this.srcdir = srcdir;
	}
}
