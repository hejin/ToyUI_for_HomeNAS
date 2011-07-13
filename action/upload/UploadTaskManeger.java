/*     */ package action.upload;
/*     */ 
/*     */ import action.upload.params.ConnectionParam;
/*     */ import action.upload.params.UploadTaskParam;
/*     */ import action.upload.params.UploadTaskParam.STATUS;
/*     */ import fileutil.SFile;
/*     */ import java.applet.Applet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import webutil.HTTPStream;
/*     */ 
/*     */ public class UploadTaskManeger
/*     */ {
/*     */   private UploadTaskThread _UTaskThread;
/*     */   private Applet _Applet;
/*     */   private HTTPStream _HttpConnect;
/*     */   private Integer _UTaskSize;
/*     */   private Integer _UTaskIndex;
/*     */   private ConcurrentHashMap<Integer, UploadTaskParam> _UTaskList;
/*     */ 
/*     */   public UploadTaskManeger(Applet paramApplet)
/*     */   {
/*  26 */     this._Applet = paramApplet;
/*  27 */     this._HttpConnect = new HTTPStream(paramApplet.getParameter("baseURL") + "webUI/java_upload.cgi");
/*  28 */     this._UTaskList = new ConcurrentHashMap();
/*  29 */     this._UTaskSize = new Integer(0);
/*  30 */     this._UTaskIndex = new Integer(0);
/*     */   }
/*     */ 
/*     */   public synchronized void addUploadTasks(String paramString, JSONArray paramJSONArray, ConnectionParam paramConnectionParam)
/*     */     throws JSONException
/*     */   {
/*  48 */     if ((null == this._UTaskThread) || (this._UTaskThread.isInterrupted()) || (!this._UTaskThread.isAlive()))
/*     */     {
/*  50 */       createUploadTaskThread();
/*  51 */       this._UTaskThread.addUploadTasks(paramString, paramJSONArray, paramConnectionParam);
/*  52 */       this._UTaskThread.setDaemon(true);
/*  53 */       this._UTaskThread.start();
/*     */     } else {
/*  55 */       this._UTaskThread.addUploadTasks(paramString, paramJSONArray, paramConnectionParam);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void cancelUTask(int paramInt)
/*     */     throws NullPointerException
/*     */   {
/*  69 */     this._UTaskThread.cancelTask(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void restartUTask(int paramInt)
/*     */     throws JSONException, NullPointerException
/*     */   {
/*  87 */     if (this._UTaskList.containsKey(Integer.valueOf(paramInt))) {
/*  88 */       JSONArray localJSONArray = new JSONArray();
/*  89 */       UploadTaskParam localUploadTaskParam = (UploadTaskParam)this._UTaskList.get(Integer.valueOf(paramInt));
/*  90 */       localJSONArray.put(localUploadTaskParam.getTaskFile().getAbsolutePath());
/*  91 */       if (localUploadTaskParam.getStatus() == UploadTaskParam.STATUS.FAIL) {
/*  92 */         addUploadTasks(localUploadTaskParam.getRemoteDir(), localJSONArray, localUploadTaskParam.getConnectionParam());
/*  93 */         cancelUTask(paramInt);
/*     */       }
/*     */     } else {
/*  96 */       throw new NullPointerException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void createUploadTaskThread()
/*     */   {
/* 104 */     this._UTaskThread = new UploadTaskThread(this, this._Applet, this._UTaskList, this._HttpConnect);
/*     */   }
/*     */ 
/*     */   public Integer getUTaskIndex()
/*     */   {
/* 114 */     synchronized (this._UTaskIndex) {
/* 115 */       return this._UTaskIndex;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Integer getUTaskSize()
/*     */   {
/* 125 */     synchronized (this._UTaskSize) {
/* 126 */       return this._UTaskSize;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void increaseUTaskIndex()
/*     */   {
/*     */     Integer localInteger1;
/* 134 */     synchronized (this._UTaskIndex) {
/* 135 */       UploadTaskManeger localUploadTaskManeger = this; localInteger1 = localUploadTaskManeger._UTaskIndex; Integer localInteger2 = localUploadTaskManeger._UTaskIndex = Integer.valueOf(localUploadTaskManeger._UTaskIndex.intValue() + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void increaseUTaskSize()
/*     */   {
/*     */     Integer localInteger1;
/* 143 */     synchronized (this._UTaskSize) {
/* 144 */       UploadTaskManeger localUploadTaskManeger = this; localInteger1 = localUploadTaskManeger._UTaskSize; Integer localInteger2 = localUploadTaskManeger._UTaskSize = Integer.valueOf(localUploadTaskManeger._UTaskSize.intValue() + 1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.upload.UploadTaskManeger
 * JD-Core Version:    0.6.0
 */