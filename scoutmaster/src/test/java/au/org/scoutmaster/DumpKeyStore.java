package au.org.scoutmaster;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import com.ibm.icu.text.UTF16.StringComparator;

public class DumpKeyStore
{

	@Test
	public void test()
	{

		try
		{
			// Load the JDK's cacerts keystore file
			String filename = System.getProperty("java.home")
					+ "/lib/security/cacerts".replace('/', File.separatorChar);
			FileInputStream is = new FileInputStream(filename);
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			String password = "changeit";
			keystore.load(is, password.toCharArray());

			// This class retrieves the most-trusted CAs from the keystore
			PKIXParameters params = new PKIXParameters(keystore);

			// Get the set of trust anchors, which contain the most-trusted CA
			// certificates
			Iterator it = params.getTrustAnchors().iterator();

			ArrayList<String> certs = new ArrayList<>();
			int count = 0;
			while (it.hasNext())
			{
				TrustAnchor ta = (TrustAnchor) it.next();
				// Get certificate
				java.security.cert.X509Certificate cert = ta.getTrustedCert();
				certs.add(cert.getSubjectDN().getName());
				count++;
			}
			certs.sort(new StringComparator());
			for (String cert : certs)
			{
				System.out.println(cert);
			}
			System.out.println("found : " + count);
		}
		catch (KeyStoreException e)
		{
		}
		catch (NoSuchAlgorithmException e)
		{
		}
		catch (InvalidAlgorithmParameterException e)
		{
		}
		catch (IOException e)
		{
		}
		catch (java.security.cert.CertificateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
