package com.rackspacecloud.client.cloudfiles;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.haxx.curl.CurlGlue;
import net.haxx.curl.CurlWrite;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class MyTest implements CurlWrite {
	public int handleString(byte s[]) {
		/* output everything */
		System.out.println("IIIIIIIIIII -------------- OOOOOOOOOOOOOOOOOOO");
		try {
			System.out.write(s);
		} catch (java.io.IOException moo) {
			// nothing
		}
		return 0;
	}

	@Test
	public void testURL() throws IOException {
		// URL url = new URL("http://172.16.10.52:8080/auth/v1.0");
		HttpURLConnection con = (HttpURLConnection) new URL(
				"http://172.16.10.71:8080/auth/v1.0").openConnection();
		con.setRequestMethod("GET");
		con.addRequestProperty("X-Storage-User", "system:root");
		con.addRequestProperty("X-Storage-Pass", "testpass");
		// con.getOutputStream().write("LOGIN".getBytes("UTF-8"));
		InputStream response = con.getInputStream();

		// InputStream response = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response));
		for (String line; (line = reader.readLine()) != null;) {
			System.out.println(line);
		}
		Map<String, List<String>> heads = con.getHeaderFields();
		// for (int i = 0; i < heads.size(); i++)
		System.out.println(heads.get("X-Storage-Url").get(0));
		System.out.println(heads.get("X-Storage-Token").get(0));
		System.out.println(heads.get("X-Auth-Token").get(0));

		reader.close();
	}

	@Test
	public void testCurl() {
		// Bank account number and PIN - fake of course :)
		String account = new String("1234567");
		String pinCode = new String("1234");
		CurlGlue cg;
		try {
			// test cw = new test();
			// Register callback write function
			cg = new CurlGlue();
			// cg.setopt(CurlGlue.CURLOPT_WRITEFUNCTION, cw);
			// Login to the bank's secure Web site, posting account number and
			// PIN code
			cg.setopt(CurlGlue.CURLOPT_URL,
					"https://172.16.10.70:8080/auth/v1.0");
			cg.setopt(CurlGlue.CURLOPT_SSLVERSION, 3);
			cg.setopt(CurlGlue.CURLOPT_SSL_VERIFYPEER, 0); // attention!
			// Insecure mode!!
			cg.setopt(CurlGlue.CURLOPT_VERBOSE, 1);
			cg.setopt(CurlGlue.CURLOPT_FOLLOWLOCATION, 1);
			cg.setopt(CurlGlue.CURLOPT_POST, 1);
			cg.setopt(CurlGlue.CURLOPT_COOKIEJAR, "cookie.txt");
			// cg.setopt(CurlGlue.CURLOPT_POSTFIELDS,
			// "pag=login&pagErr=/index.html&usuario=" + account
			// + "&clave=" + pinCode + "&irAmodulo=1");
			cg.setopt(CurlGlue.CURLOPT_HEADER,
					"X-Storage-User=system:root&X-Storage-Pass=testpass");
			cg.perform();

			// // Access the bank account balance re-using the session ID stored
			// in
			// // memory inside a cookie
			// cg
			// .setopt(CurlGlue.CURLOPT_URL,
			// "https://www.santander.com.mx/SuperNetII/servlet/Cchequeras");
			// cg.setopt(CurlGlue.CURLOPT_SSLVERSION, 3);
			// cg.setopt(CurlGlue.CURLOPT_SSL_VERIFYPEER, 0); // attention!
			// // Insecure mode!!
			// cg.setopt(CurlGlue.CURLOPT_VERBOSE, 1);
			// cg.setopt(CurlGlue.CURLOPT_FOLLOWLOCATION, 1);
			// cg.setopt(CurlGlue.CURLOPT_COOKIEFILE, "cookie.txt");
			// cg.perform();

			// The cookie.txt file is actually created now
			// cg.finalize();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void statAcc() throws KeyManagementException, NoSuchAlgorithmException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
		httpclient = WebClientDevWrapper.wrapClient(httpclient);
		HttpHead httphead = new HttpHead("https://172.16.10.70:8080/v1/AUTH_316d8c49-76bd-4573-a0b6-6bcad670a63a");
		httphead.setHeader("X-Auth-Token", "AUTH_tke7c326f48e2a4d5daec79a83335584a2");
		httphead.setHeader("Accept", "application/json");
		HttpResponse response = httpclient.execute(httphead);
		FilesResponse fresponse = new FilesResponse(response);

		HttpEntity entity = response.getEntity();
		System.out.println("Status Line: " + response.getStatusLine());

		Header[] responseHeaders = response.getAllHeaders();
		for (int i = 0; i < responseHeaders.length; i++)
			System.out.println(responseHeaders[i]);
		
//		System.out.println(fresponse.getContainerObjectCount()); 
	}
	
	@Test
	public void testHttp() throws IOException, KeyManagementException,
			NoSuchAlgorithmException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient = WebClientDevWrapper.wrapClient(httpclient);
		HttpGet httpget = new HttpGet("https://172.16.10.70:8080/auth/v1.0");
		httpget.setHeader("X-Storage-User", "system:root");
		httpget.setHeader("X-Storage-Pass", "testpass");
		httpget.setHeader("Accept", "application/json");

		HttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();
		System.out.println("Status Line: " + response.getStatusLine());

		Header[] responseHeaders = response.getAllHeaders();
		for (int i = 0; i < responseHeaders.length; i++)
			System.out.println(responseHeaders[i]);

		System.out.println(response.getStatusLine());
		System.out.println(EntityUtils.toByteArray(entity).toString());

		// if (entity != null) {
		// InputStream instream = entity.getContent();
		// int l;
		// byte[] tmp = new byte[2048];
		// while ((l = instream.read(tmp)) != -1) {
		// System.out.println(tmp);
		// }
		// }
	}

	@Test
	public void testLogin() throws IOException {
		FilesClient client = new FilesClient("system:root", "testpass",
				FilesUtil.getProperty("account"));
		try {
			// assertFalse(client.login());
			boolean rt = client.login();
			System.out.println("login result:" + rt);
		} catch (Exception e) {
			// fail(e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testContainerCreation() {
		FilesClient client = new FilesClient("system:root", "testpass",
				FilesUtil.getProperty("account"));
		try {
			assertTrue(client.login());
			System.out.println("login success");
			// String containerName = createTempContainerName("container");
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy_MM_dd_HH_mm_ss_SSSS");
			String containerName = "test-wx-container-"
					+ sdf.format(new Date(System.currentTimeMillis()));

			// Make sure it's not there
			assertFalse(client.containerExists(containerName));
			System.out.println("Make sure it's not there");
			// Add it
			// logger.error("Creating the container");
			client.createContainer(containerName);
			// logger.error("URL:\n" + client.getStorageURL() + "/" +
			// containerName + "\n");
			// Thread.sleep(10000);
			System.out.println("createContainer done!");

			// See that it's there
			assertTrue(client.containerExists(containerName));
			assertNotNull(client.getContainerInfo(containerName));

			// Try Adding it again
			try {
				client.createContainer(containerName);
				fail("Allowed duplicate container creation");
			} catch (FilesContainerExistsException fcee) {
				// Hooray!
			}

			// See that it's still there
			assertTrue(client.containerExists(containerName));

			// Delete it
			assertTrue(client.deleteContainer(containerName));

			// Make sure it's gone
			assertFalse(client.containerExists(containerName));

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
