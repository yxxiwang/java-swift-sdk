package com.rackspacecloud.client.cloudfiles;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
  
/*
This code is public domain: you are free to use, link and/or modify it in any way you want, for all purposes including commercial applications.
*/
public class WebClientDevWrapper {
    public static HttpClient getClient(HttpClient base) {
    	SchemeRegistry schemeRegistry = new SchemeRegistry();
    	schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    	schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
    	 
    	HttpParams params = new BasicHttpParams();
    	params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
    	params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
    	params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
    	HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    	 
    	ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
    	return  new DefaultHttpClient(cm, params);
    }
    
        public static HttpClient wrapClient(HttpClient base) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        	TrustManager easyTrustManager = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}
        		
        	};
        	SSLContext sslcontext = SSLContext.getInstance("TLS");
        	sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);
        	SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
        	SSLSocket socket = (SSLSocket) sf.createSocket();
        	socket.setEnabledCipherSuites(new String[] { "SSL_RSA_WITH_RC4_128_MD5" });
        	HttpParams params = new BasicHttpParams();
        	params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
        	InetSocketAddress address = new InetSocketAddress("172.16.10.70", 8080);
        	sf.connectSocket(socket, address, null, params);
        	ClientConnectionManager ccm = base.getConnectionManager();
        	SchemeRegistry sr = ccm.getSchemeRegistry();
          	sr.register(new Scheme("https", sf, 443));
          	return new DefaultHttpClient(ccm, base.getParams());
//                try {
//                        SSLContext ctx = SSLContext.getInstance("TLS");
//                        X509TrustManager tm = new X509TrustManager() {
//                                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
//                                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
//                                public X509Certificate[] getAcceptedIssuers() {
//                                        return null;
//                                }
//								@Override
//								public void checkClientTrusted(
//										X509Certificate[] chain, String authType)
//										throws CertificateException {
//									// TODO Auto-generated method stub
//									
//								}
//								@Override
//								public void checkServerTrusted(
//										X509Certificate[] chain, String authType)
//										throws CertificateException {
//									// TODO Auto-generated method stub
//									
//								}
//                        };
//                        ctx.init(null, new TrustManager[]{tm}, null);
//                        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//                        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                        ClientConnectionManager ccm = base.getConnectionManager();
//                        SchemeRegistry sr = ccm.getSchemeRegistry();
//                        sr.register(new Scheme("https", ssf, 443));
//                        return new DefaultHttpClient(ccm, base.getParams());
//                } catch (Exception ex) {
//                        ex.printStackTrace();
//                        return null;
//                }
        }
}