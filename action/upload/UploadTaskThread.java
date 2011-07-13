/*     */ package action.upload;
/*     */ 
/*     */ import action.FileStationHandler;
/*     */ import action.upload.params.ConnectionParam;
/*     */ import action.upload.params.UploadTaskParam;
/*     */ import java.applet.Applet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import jsonutil.JSONUtil;
/*     */ import jsutil.JSUtil;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import webutil.HTTPStream;
/*     */ 
/*     */ public class UploadTaskThread extends Thread
/*     */ {
/*     */   private Applet _Applet;
/*     */   private UploadTaskManeger _UTaskManeger;
/*     */   private boolean _isUpdateUTaskList;
/*     */   private ConcurrentHashMap<Integer, UploadTaskParam> _UTaskList;
/*     */   private UploadFileThread _CurrentUFileThread;
/*     */   private UploadTaskParam _CurrentUTask;
/*     */   private HTTPStream _HttpConnect;
/*     */   private long _TimeBeforeCheckNextFile;
/*     */ 
/*     */   public UploadTaskThread(UploadTaskManeger paramUploadTaskManeger, Applet paramApplet, ConcurrentHashMap<Integer, UploadTaskParam> paramConcurrentHashMap, HTTPStream paramHTTPStream)
/*     */   {
/*  37 */     this._UTaskManeger = paramUploadTaskManeger;
/*  38 */     this._Applet = paramApplet;
/*  39 */     this._HttpConnect = paramHTTPStream;
/*  40 */     this._UTaskList = paramConcurrentHashMap;
/*  41 */     this._CurrentUTask = null;
/*  42 */     this._TimeBeforeCheckNextFile = 100L;
/*     */   }
/*     */ 
/*     */   private synchronized void createUploadFileThread(UploadTaskParam paramUploadTaskParam)
/*     */   {
/*  52 */     this._CurrentUFileThread = new UploadFileThread(this._HttpConnect, paramUploadTaskParam);
/*     */   }
/*     */ 
/*     */   public synchronized UploadTaskParam getNextUploadFile()
/*     */   {
/*  62 */     while (this._UTaskManeger.getUTaskIndex().intValue() < this._UTaskManeger.getUTaskSize().intValue()) {
/*  63 */       while (true == this._isUpdateUTaskList) {
/*     */         try {
/*  65 */           Thread.sleep(60L);
/*     */         } catch (InterruptedException localInterruptedException) {
/*  67 */           FileStationHandler.log(localInterruptedException);
/*     */         }
/*     */       }
/*  70 */       int i = this._UTaskManeger.getUTaskIndex().intValue();
/*  71 */       if (!this._UTaskList.containsKey(Integer.valueOf(i))) {
/*  72 */         this._UTaskManeger.increaseUTaskIndex();
/*  73 */         continue;
/*     */       }
/*  75 */       this._CurrentUTask = ((UploadTaskParam)this._UTaskList.get(Integer.valueOf(i)));
/*  76 */       if (this._CurrentUTask.getStatus() == UploadTaskParam.STATUS.NOT_STARTED) {
/*  77 */         this._CurrentUTask.initUploadFileList();
/*  78 */         if (!JSUtil.evalJSON(this._Applet, "onOpen", this._CurrentUTask))
/*  79 */           this._CurrentUFileThread.fail();
/*     */       }
/*  81 */       else if (((UploadTaskParam)this._UTaskList.get(Integer.valueOf(i))).getStatus() == UploadTaskParam.STATUS.PROCESSING) {
/*  82 */         if (-1 == this._CurrentUTask.getNextUplaodFile())
/*  83 */           if (!this._CurrentUTask.getSkipFilenames().equals("")) {
/*  84 */             this._CurrentUTask.skip();
/*     */           } else {
/*  86 */             this._CurrentUTask.success();
/*  87 */             if (!JSUtil.evalJSON(this._Applet, "onComplete", this._CurrentUTask))
/*     */             {
/*  89 */               this._CurrentUFileThread.fail();
/*     */             }
/*  91 */             this._UTaskManeger.increaseUTaskIndex();
/*  92 */             continue;
/*     */           }
/*     */       }
/*  95 */       else if (this._CurrentUTask.getStatus() == UploadTaskParam.STATUS.CANCEL) {
/*  96 */         this._UTaskManeger.increaseUTaskIndex();
/*  97 */         continue;
/*     */       }
/*  99 */       if (this._CurrentUTask.getStatus() == UploadTaskParam.STATUS.FAIL) {
/* 100 */         if (!JSUtil.evalJSON(this._Applet, "onError", this._CurrentUTask)) {
/* 101 */           this._CurrentUFileThread.fail();
/*     */         }
/* 103 */         this._UTaskManeger.increaseUTaskIndex();
/* 104 */         continue;
/*     */       }
/*     */ 
/* 107 */       if (this._CurrentUTask.getStatus() == UploadTaskParam.STATUS.SKIP) {
/* 108 */         this._CurrentUTask.setJSONResponese(JSONUtil.setError("mvcp", "mvcp_file_too_big"));
/*     */ 
/* 110 */         if (!JSUtil.evalJSON(this._Applet, "onError", this._CurrentUTask)) {
/* 111 */           this._CurrentUFileThread.fail();
/*     */         }
/* 113 */         this._UTaskManeger.increaseUTaskIndex();
/* 114 */         continue;
/*     */       }
/* 116 */       return this._CurrentUTask;
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized void cancelTask(int paramInt)
/*     */     throws NullPointerException
/*     */   {
/* 131 */     if (this._UTaskList.containsKey(Integer.valueOf(paramInt))) {
/* 132 */       UploadTaskParam localUploadTaskParam = (UploadTaskParam)this._UTaskList.get(Integer.valueOf(paramInt));
/* 133 */       if (localUploadTaskParam.getStatus() == UploadTaskParam.STATUS.PROCESSING) {
/* 134 */         this._CurrentUFileThread.cancel();
/* 135 */         this._UTaskManeger.increaseUTaskIndex();
/* 136 */         this._CurrentUTask = null;
/*     */       }
/* 138 */       localUploadTaskParam.cancel();
/* 139 */       this._UTaskList.remove(Integer.valueOf(paramInt));
/*     */     } else {
/* 141 */       throw new NullPointerException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void addUploadTasks(String paramString, JSONArray paramJSONArray, ConnectionParam paramConnectionParam)
/*     */     throws JSONException
/*     */   {
/* 161 */     this._isUpdateUTaskList = true;
/* 162 */     int i = this._UTaskManeger.getUTaskSize().intValue();
/*     */ 
/* 164 */     for (int k = 0; k < paramJSONArray.length(); k++) {
/* 165 */       int j = this._UTaskManeger.getUTaskSize().intValue();
/* 166 */       UploadTaskParam localUploadTaskParam = new UploadTaskParam(Integer.valueOf(j), paramJSONArray.getString(k), paramString, paramConnectionParam);
/*     */ 
/* 168 */       this._UTaskList.put(Integer.valueOf(j), localUploadTaskParam);
/* 169 */       if (!JSUtil.evalJSON(this._Applet, "onSelect", localUploadTaskParam)) {
/* 170 */         this._CurrentUFileThread.fail();
/*     */       }
/* 172 */       this._UTaskManeger.increaseUTaskSize();
/*     */     }
/* 174 */     if (!JSUtil.eval(this._Applet, "onAllSelect", i + "," + this._UTaskManeger.getUTaskSize()))
/*     */     {
/* 176 */       this._CurrentUFileThread.fail();
/*     */     }
/* 178 */     this._isUpdateUTaskList = false;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     while (true)
/*     */     {
/* 187 */       if ((this._CurrentUFileThread != null) && (this._CurrentUFileThread.isAlive())) {
/* 188 */         if (!JSUtil.evalJSON(this._Applet, "onProgress", this._CurrentUTask))
/* 189 */           this._CurrentUFileThread.fail();
/*     */         try
/*     */         {
/* 192 */           sleep(this._TimeBeforeCheckNextFile);
/*     */         } catch (InterruptedException localInterruptedException) {
/* 194 */           FileStationHandler.log(localInterruptedException);
/* 195 */         }continue;
/*     */       }
/* 197 */       if (null == (this._CurrentUTask = getNextUploadFile())) {
/* 198 */         if (JSUtil.eval(this._Applet, "onAllComplete", null)) break;
/* 199 */         this._CurrentUFileThread.fail(); break;
/*     */       }
/*     */ 
/* 203 */       createUploadFileThread(this._CurrentUTask);
/*     */ 
/* 206 */       this._TimeBeforeCheckNextFile = (this._CurrentUTask.getCurrentUFile().getFile().length() > 8388608L ? 1000L : 60L);
/*     */ 
/* 208 */       this._CurrentUFileThread.start();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.upload.UploadTaskThread
 * JD-Core Version:    0.6.0
 */