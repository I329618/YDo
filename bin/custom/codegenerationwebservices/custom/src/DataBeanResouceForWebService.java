/**
 *
 */
package src;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.AbstractResource;


/**
 * @author I328935
 *
 */
public class DataBeanResouceForWebService extends AbstractResource
{

	@Override
	protected String readResource()
	{
		return "Hi!!";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.core.io.Resource#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.core.io.InputStreamSource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException
	{
		// YTODO Auto-generated method stub
		return null;
	}

}
