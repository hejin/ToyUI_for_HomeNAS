/*     */ package webutil;
/*     */ 
/*     */ import action.FileStationHandler;
/*     */ import action.upload.params.UploadFileParam;
/*     */ import action.upload.params.UploadTaskParam;
/*     */ import fileutil.FileUtil;
/*     */ import fileutil.SFile;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URL;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.util.Random;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import javautil.Version;
/*     */ 
/*     */ public class HTTPStream
/*     */ {
/*  33 */   private String _ServerAddr = null;
/*  34 */   private String _Boundary = null;
/*  35 */   private HttpURLConnection _ServerConnection = null;
/*     */   private DataOutputStream _OutputStream;
/*  37 */   private InputStreamReader _InputStream = null;
/*     */   private static Method setLongFixedLengthStreamingMode;
/*  39 */   private static int BUFFERSIZE = 131072;
/*     */ 
/*     */   public HTTPStream(String paramString)
/*     */   {
/*  59 */     this._ServerAddr = paramString;
/*     */   }
/*     */ 
/*     */   public void closeConnection()
/*     */   {
/*     */     try
/*     */     {
/*  67 */       if (this._OutputStream != null)
/*     */       {
/*  69 */         this._OutputStream.close();
/*     */ 
/*  71 */         this._OutputStream = null;
/*     */       }
/*  73 */       if (this._InputStream != null) {
/*  74 */         this._InputStream.close();
/*  75 */         this._InputStream = null;
/*     */       }
/*  77 */       if (this._ServerConnection != null) {
/*  78 */         this._ServerConnection.disconnect();
/*  79 */         this._ServerConnection = null;
/*     */       }
/*     */     } catch (IOException localIOException) {
/*  82 */       FileStationHandler.log(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStreamReader getResponse()
/*     */     throws UnsupportedEncodingException, IOException
/*     */   {
/*  99 */     if (null == this._ServerConnection) {
/* 100 */       throw new IOException();
/*     */     }
/* 102 */     String str = this._ServerConnection.getContentEncoding();
/* 103 */     if (null != str) {
/* 104 */       if (-1 != str.indexOf("gzip")) {
/* 105 */         this._InputStream = new InputStreamReader(new GZIPInputStream(this._ServerConnection.getInputStream()), "utf-8");
/*     */       }
/* 107 */       else if (-1 != str.indexOf("deflate")) {
/* 108 */         this._InputStream = new InputStreamReader(new InflaterInputStream(this._ServerConnection.getInputStream()), "utf-8");
/*     */       }
/*     */     }
/*     */     else {
/* 112 */       this._InputStream = new InputStreamReader(this._ServerConnection.getInputStream(), "utf-8");
/*     */     }
/*     */ 
/* 115 */     return this._InputStream;
/*     */   }
/*     */ 
/*     */   private String generateRandomBoundary()
/*     */   {
/* 124 */     StringBuffer localStringBuffer = new StringBuffer();
/* 125 */     localStringBuffer.append("---------------------------1");
/* 126 */     Random localRandom = new Random();
/* 127 */     for (int i = 0; i < 13; i++) {
/* 128 */       localStringBuffer.append(localRandom.nextInt(10));
/*     */     }
/* 130 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private HttpURLConnection openConnection()
/*     */     throws MalformedURLException, IOException, ProtocolException
/*     */   {
/* 147 */     HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(this._ServerAddr).openConnection();
/*     */ 
/* 149 */     setHTTPProfile(localHttpURLConnection);
/* 150 */     return localHttpURLConnection;
/*     */   }
/*     */ 
/*     */   private void setHTTPProfile(HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 164 */     paramHttpURLConnection.setRequestMethod("POST");
/* 165 */     paramHttpURLConnection.setDoInput(true);
/* 166 */     paramHttpURLConnection.setDoOutput(true);
/* 167 */     paramHttpURLConnection.setUseCaches(false);
/* 168 */     paramHttpURLConnection.setDefaultUseCaches(false);
/* 169 */     paramHttpURLConnection.setRequestProperty("Accept", "*/*");
/* 170 */     paramHttpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");
/* 171 */     paramHttpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
/* 172 */     paramHttpURLConnection.setRequestProperty("Keep-Alive", "300");
/* 173 */     paramHttpURLConnection.setRequestProperty("Connection", "keep-alive");
/* 174 */     this._Boundary = generateRandomBoundary();
/* 175 */     paramHttpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this._Boundary);
/*     */   }
/*     */ 
/*     */   private DataOutputStream getOutputStream(HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 192 */     return new DataOutputStream(paramHttpURLConnection.getOutputStream());
/*     */   }
/*     */ 
/*     */   private void prepareParams(StringBuffer paramStringBuffer, UploadTaskParam paramUploadTaskParam, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 210 */     paramStringBuffer.append("--" + this._Boundary + "\r\n");
/* 211 */     if (paramBoolean) {
/* 212 */       writeParam(paramStringBuffer, "blCheckSkip", String.valueOf(paramBoolean));
/*     */     }
/* 214 */     writeParam(paramStringBuffer, "isDir", String.valueOf(!paramUploadTaskParam.getCurrentUFile().getFile().isFile()));
/*     */ 
/* 216 */     writeParam(paramStringBuffer, "filesize", String.valueOf(paramUploadTaskParam.getCurrentUFile().getFile().length()));
/*     */ 
/* 218 */     writeParam(paramStringBuffer, "path", paramUploadTaskParam.getCurrentUFile().getRemoteDir());
/* 219 */     writeFileParam(paramStringBuffer, paramUploadTaskParam.getCurrentUFile().getFile());
/*     */   }
/*     */ 
/*     */   private void writeParam(StringBuffer paramStringBuffer, String paramString1, String paramString2)
/*     */   {
/* 233 */     if (null == paramString1) {
/* 234 */       throw new IllegalArgumentException();
/*     */     }
/* 236 */     if (null == paramString2) {
/* 237 */       paramString2 = "";
/*     */     }
/* 239 */     paramStringBuffer.append("Content-Disposition: form-data; name=\"" + paramString1 + "\"");
/* 240 */     paramStringBuffer.append("\r\n\r\n");
/* 241 */     paramStringBuffer.append(paramString2).append("\r\n");
/* 242 */     paramStringBuffer.append("--" + this._Boundary + "\r\n");
/*     */   }
/*     */ 
/*     */   private void writeFileParam(StringBuffer paramStringBuffer, SFile paramSFile)
/*     */     throws IOException
/*     */   {
/* 258 */     String str1 = paramSFile.isFile() ? "file" : "dir";
/* 259 */     String str2 = FileUtil.getMimetype(paramSFile.getName());
/* 260 */     paramStringBuffer.append("Content-Disposition: form-data; name=\"" + str1 + "\"; " + "filename=\"");
/*     */ 
/* 262 */     paramStringBuffer.append(paramSFile.getName());
/* 263 */     paramStringBuffer.append("\"\r\nContent-Type: " + str2 + "\r\n\r\n");
/*     */   }
/*     */ 
/*     */   private void sendFile(DataOutputStream paramDataOutputStream, UploadTaskParam paramUploadTaskParam)
/*     */     throws IOException
/*     */   {
/* 279 */     if (paramUploadTaskParam.getCurrentUFile().getFile().isFile())
/* 280 */       uploadFile(paramDataOutputStream, paramUploadTaskParam);
/*     */     else
/* 282 */       paramUploadTaskParam.updateTimeAndSpace(paramUploadTaskParam.getCurrentUFile().getFile().length());
/*     */   }
/*     */ 
/*     */   private void uploadFile(DataOutputStream paramDataOutputStream, UploadTaskParam paramUploadTaskParam)
/*     */     throws IOException, NullPointerException
/*     */   {
/*     */     FileInputStream localFileInputStream;
/*     */     FileChannel localFileChannel;
/*     */     ByteBuffer localByteBuffer;
/*     */     try
/*     */     {
/* 303 */       localFileInputStream = new FileInputStream(paramUploadTaskParam.getCurrentUFile().getFile());
/* 304 */       localFileChannel = localFileInputStream.getChannel();
/* 305 */       localByteBuffer = ByteBuffer.allocateDirect(BUFFERSIZE * 2);
/*     */     } catch (Exception localException) {
/* 307 */       throw new NullPointerException();
/*     */     }
/* 309 */     byte[] arrayOfByte = new byte[BUFFERSIZE];
/*     */     int i;
/* 311 */     while ((i = localFileChannel.read(localByteBuffer)) != -1) {
/* 312 */       if (i == 0)
/*     */         continue;
/* 314 */       localByteBuffer.position(0);
/* 315 */       localByteBuffer.limit(i);
/* 316 */       while (localByteBuffer.hasRemaining()) {
/* 317 */         int j = Math.min(localByteBuffer.remaining(), BUFFERSIZE);
/* 318 */         localByteBuffer.get(arrayOfByte, 0, j);
/* 319 */         paramDataOutputStream.write(arrayOfByte, 0, j);
/* 320 */         paramDataOutputStream.flush();
/* 321 */         paramUploadTaskParam.updateTimeAndSpace(j);
/*     */       }
/* 323 */       localByteBuffer.clear();
/*     */     }
/*     */     try {
/* 326 */       localFileInputStream.close();
/*     */     } catch (IOException localIOException) {
/* 328 */       throw new NullPointerException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setStreamingMode(long paramLong)
/*     */   {
/*     */     try
/*     */     {
/* 340 */       setLongFixedLengthStreamingMode.invoke(this._ServerConnection, new Object[] { Long.valueOf(paramLong) });
/*     */     }
/*     */     catch (Exception localException) {
/* 343 */       if (paramLong <= 2147483647L)
/* 344 */         this._ServerConnection.setFixedLengthStreamingMode((int)paramLong);
/*     */       else
/* 346 */         this._ServerConnection.setChunkedStreamingMode(BUFFERSIZE);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean checkSkipHandler(UploadTaskParam paramUploadTaskParam)
/*     */     throws MalformedURLException, ProtocolException, IOException, NullPointerException
/*     */   {
/* 367 */     if ((paramUploadTaskParam.isOverwrite()) || (!paramUploadTaskParam.getCurrentUFile().getFile().isFile())) {
/* 368 */       return false;
/*     */     }
/* 370 */     StringBuffer localStringBuffer = new StringBuffer();
/* 371 */     this._ServerConnection = openConnection();
/* 372 */     prepareParams(localStringBuffer, paramUploadTaskParam, true);
/*     */ 
/* 374 */     byte[] arrayOfByte = localStringBuffer.toString().getBytes("utf-8");
/* 375 */     setStreamingMode(arrayOfByte.length);
/*     */ 
/* 377 */     this._OutputStream = getOutputStream(this._ServerConnection);
/* 378 */     this._OutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/* 379 */     return true;
/*     */   }
/*     */ 
/*     */   public void uploadHander(UploadTaskParam paramUploadTaskParam)
/*     */     throws MalformedURLException, ProtocolException, IOException, NullPointerException
/*     */   {
/* 397 */     StringBuffer localStringBuffer = new StringBuffer();
/* 398 */     this._ServerConnection = openConnection();
/* 399 */     prepareParams(localStringBuffer, paramUploadTaskParam, false);
/*     */ 
/* 401 */     byte[] arrayOfByte = localStringBuffer.toString().getBytes("utf-8");
/* 402 */     if (paramUploadTaskParam.getCurrentUFile().getFile().isFile())
/* 403 */       setStreamingMode(arrayOfByte.length + paramUploadTaskParam.getCurrentUFile().getFile().length());
/*     */     else {
/* 405 */       setStreamingMode(arrayOfByte.length);
/*     */     }
/* 407 */     this._OutputStream = getOutputStream(this._ServerConnection);
/* 408 */     this._OutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/* 409 */     sendFile(this._OutputStream, paramUploadTaskParam);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  42 */     Version localVersion = new Version(System.getProperty("java.version"));
/*     */ 
/*  44 */     setLongFixedLengthStreamingMode = null;
/*  45 */     if (localVersion.compare(new Version("1.7.0")) >= 0)
/*     */       try {
/*  47 */         Class localClass = Class.forName("java.net.HttpURLConnection");
/*  48 */         Class[] arrayOfClass = new Class[1];
/*     */ 
/*  50 */         arrayOfClass[0] = Long.TYPE;
/*  51 */         setLongFixedLengthStreamingMode = localClass.getDeclaredMethod("setFixedLengthStreamingMode", arrayOfClass);
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     webutil.HTTPStream
 * JD-Core Version:    0.6.0
 */