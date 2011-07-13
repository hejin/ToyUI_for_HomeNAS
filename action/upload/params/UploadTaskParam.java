/*     */ package action.upload.params;
/*     */ 
/*     */ import action.upload.UploadTaskSizeHandler;
/*     */ import fileutil.SFile;
/*     */ import fileutil.filefilter.UploadFileFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import jsonutil.JSONUtil;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ public class UploadTaskParam
/*     */ {
/*     */   private Integer _TaskID;
/*     */   private SFile _TaskFile;
/*     */   private String _RemoteDir;
/*     */   private long _TaskSize;
/*     */   private TimeAndSpaceParam _TSParam;
/*     */   private STATUS _Status;
/*     */   private JSONObject _Responese;
/*     */   private ConnectionParam _CParam;
/*     */   private Integer _UFileSize;
/*     */   private Integer _UFileIndex;
/*     */   private ConcurrentHashMap<Integer, UploadFileParam> _UFileList;
/*     */   private UploadFileParam _CurrentUFile;
/*     */   private String _SkipFilenames;
/*  42 */   private UploadFileFilter _UFileFilter = new UploadFileFilter();
/*     */ 
/*     */   public UploadTaskParam(Integer paramInteger, String paramString1, String paramString2, ConnectionParam paramConnectionParam)
/*     */   {
/*  46 */     this._TaskID = paramInteger;
/*  47 */     this._TaskFile = new SFile(paramString1);
/*  48 */     this._RemoteDir = paramString2;
/*  49 */     this._TSParam = new TimeAndSpaceParam();
/*  50 */     this._Status = STATUS.NOT_STARTED;
/*  51 */     this._CParam = paramConnectionParam;
/*  52 */     this._UFileSize = new Integer(0);
/*  53 */     this._UFileIndex = new Integer(0);
/*  54 */     this._UFileList = new ConcurrentHashMap();
/*  55 */     this._CurrentUFile = null;
/*     */   }
/*     */ 
/*     */   public void startUpload(long paramLong) {
/*  59 */     setStatus(STATUS.PROCESSING);
/*  60 */     this._TSParam.setUploadStartTime(System.currentTimeMillis());
/*  61 */     this._TaskSize = new UploadTaskSizeHandler(this).getTaskSize(this._TaskFile);
/*     */ 
/*  63 */     this._TSParam.setTaskTotalBytes(this._TaskSize);
/*     */   }
/*     */ 
/*     */   public void initUploadFileList() {
/*  67 */     this._SkipFilenames = new String("");
/*  68 */     startUpload(System.currentTimeMillis());
/*  69 */     createUFileList(this._RemoteDir, this._TaskFile);
/*     */   }
/*     */ 
/*     */   private boolean addOneFile(String paramString, SFile paramSFile) {
/*  73 */     this._UFileList.put(this._UFileSize, new UploadFileParam(paramString, paramSFile));
/*     */ 
/*  75 */     UploadTaskParam localUploadTaskParam = this; Integer localInteger1 = localUploadTaskParam._UFileSize; Integer localInteger2 = localUploadTaskParam._UFileSize = Integer.valueOf(localUploadTaskParam._UFileSize.intValue() + 1);
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   private void addOneDir(String paramString, SFile paramSFile) {
/*  80 */     Vector localVector = paramSFile.listFileVector(this._UFileFilter);
/*  81 */     if (localVector != null) {
/*  82 */       paramString = paramString + "/" + paramSFile.getName();
/*  83 */       for (int i = 0; i < localVector.size(); i++)
/*  84 */         addOneFile(paramString, (SFile)localVector.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void createUFileList(String paramString, SFile paramSFile)
/*     */   {
/*  90 */     if ((!paramSFile.canRead()) || ((!paramSFile.isFile()) && (!paramSFile.isDirectory()))) {
/*  91 */       return;
/*     */     }
/*  93 */     if (false == addOneFile(paramString, paramSFile)) {
/*  94 */       skip();
/*  95 */       return;
/*     */     }
/*     */ 
/*  98 */     if (paramSFile.isDirectory())
/*  99 */       addOneDir(paramString, paramSFile);
/*     */     Integer localInteger1;
/* 101 */     synchronized (this._UFileIndex) {
/* 102 */       this._CurrentUFile = ((UploadFileParam)this._UFileList.get(this._UFileIndex));
/* 103 */       UploadTaskParam localUploadTaskParam = this; localInteger1 = localUploadTaskParam._UFileIndex; Integer localInteger2 = localUploadTaskParam._UFileIndex = Integer.valueOf(localUploadTaskParam._UFileIndex.intValue() + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeUploadFileList(Integer paramInteger) {
/* 108 */     if (this._UFileList.containsKey(paramInteger))
/* 109 */       this._UFileList.remove(paramInteger);
/*     */   }
/*     */ 
/*     */   public int getNextUplaodFile()
/*     */   {
/* 114 */     synchronized (this._UFileIndex) {
/* 115 */       if (this._UFileIndex.intValue() < this._UFileSize.intValue()) {
/* 116 */         this._CurrentUFile = ((UploadFileParam)this._UFileList.get(this._UFileIndex));
/*     */ 
/* 118 */         if (this._CurrentUFile.getFile().isDirectory()) {
/* 119 */           addOneDir(this._CurrentUFile.getRemoteDir(), this._CurrentUFile.getFile());
/*     */         }
/*     */ 
/* 122 */         removeUploadFileList(Integer.valueOf(this._UFileIndex.intValue() - 1));
/* 123 */         UploadTaskParam localUploadTaskParam = this; Integer localInteger1 = localUploadTaskParam._UFileIndex; Integer localInteger2 = localUploadTaskParam._UFileIndex = Integer.valueOf(localUploadTaskParam._UFileIndex.intValue() + 1);
/* 124 */         return 1;
/*     */       }
/*     */     }
/* 127 */     return -1;
/*     */   }
/*     */ 
/*     */   public void updateTimeAndSpace(long paramLong) {
/* 131 */     this._TSParam.UpdateTimeAndSpace(paramLong);
/*     */   }
/*     */ 
/*     */   public void skip() {
/* 135 */     setStatus(STATUS.SKIP);
/* 136 */     this._TSParam.stop();
/*     */   }
/*     */ 
/*     */   public void success() {
/* 140 */     setStatus(STATUS.SUCCESS);
/* 141 */     this._TSParam.success();
/*     */   }
/*     */ 
/*     */   public void cancel() {
/* 145 */     setStatus(STATUS.CANCEL);
/* 146 */     this._TSParam.stop();
/*     */   }
/*     */ 
/*     */   public void fail() {
/* 150 */     setStatus(STATUS.FAIL);
/* 151 */     this._TSParam.stop();
/*     */   }
/*     */ 
/*     */   public void setStatus(STATUS paramSTATUS) {
/* 155 */     this._Status = paramSTATUS;
/*     */   }
/*     */ 
/*     */   public void setJSONResponese(InputStreamReader paramInputStreamReader) throws IOException, JSONException
/*     */   {
/* 160 */     if (paramInputStreamReader != null)
/* 161 */       this._Responese = JSONUtil.transStreamToJSON(paramInputStreamReader);
/*     */   }
/*     */ 
/*     */   public void setJSONResponese(JSONObject paramJSONObject)
/*     */   {
/* 166 */     if (paramJSONObject != null)
/* 167 */       this._Responese = paramJSONObject;
/*     */   }
/*     */ 
/*     */   public int getTaskID()
/*     */   {
/* 172 */     return this._TaskID.intValue();
/*     */   }
/*     */ 
/*     */   public SFile getTaskFile() {
/* 176 */     return this._TaskFile;
/*     */   }
/*     */ 
/*     */   public ConnectionParam getConnectionParam() {
/* 180 */     return this._CParam;
/*     */   }
/*     */ 
/*     */   public TimeAndSpaceParam getTimeAndSpaceParam() {
/* 184 */     return this._TSParam;
/*     */   }
/*     */ 
/*     */   public boolean isOverwrite() {
/* 188 */     return this._CParam.isOverwrite();
/*     */   }
/*     */ 
/*     */   public STATUS getStatus() {
/* 192 */     return this._Status;
/*     */   }
/*     */ 
/*     */   public JSONObject getJSONResponese() {
/* 196 */     return this._Responese;
/*     */   }
/*     */ 
/*     */   public long getSize() {
/* 200 */     return this._TaskSize;
/*     */   }
/*     */ 
/*     */   public String getCurrentFileName() {
/* 204 */     synchronized (this._UFileIndex) {
/* 205 */       if (this._Status == STATUS.SKIP)
/* 206 */         return getSkipFilenames();
/* 207 */       if (null == this._CurrentUFile) {
/* 208 */         return "";
/*     */       }
/* 210 */       return this._CurrentUFile.getFile().getAbsolutePath();
/*     */     }
/*     */   }
/*     */ 
/*     */   public UploadFileParam getCurrentUFile() {
/* 215 */     synchronized (this._UFileIndex) {
/* 216 */       return this._CurrentUFile;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSkipFilenames(String paramString) {
/* 221 */     this._SkipFilenames = (this._SkipFilenames + paramString + " ");
/*     */   }
/*     */ 
/*     */   public String getSkipFilenames() {
/* 225 */     return this._SkipFilenames;
/*     */   }
/*     */ 
/*     */   public String getRemoteDir() {
/* 229 */     return this._RemoteDir;
/*     */   }
/*     */ 
/*     */   public static enum STATUS
/*     */   {
/*  22 */     NOT_STARTED, PROCESSING, SUCCESS, FAIL, CANCEL, SKIP;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.upload.params.UploadTaskParam
 * JD-Core Version:    0.6.0
 */