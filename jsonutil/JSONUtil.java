/*     */ package jsonutil;
/*     */ 
/*     */ import action.FileStationHandler;
/*     */ import action.upload.params.TimeAndSpaceParam;
/*     */ import action.upload.params.UploadTaskParam;
/*     */ import fileutil.FileUtil;
/*     */ import fileutil.SFile;
/*     */ import fileutil.SpaceUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ public class JSONUtil
/*     */ {
/*  28 */   static String _TYPE_LOCAL = "local";
/*  29 */   static String _TYPE_LOCAL_HOME = "localh";
/*     */ 
/*     */   public static JSONObject setDirAndFileListGrid(Vector<SFile> paramVector, int paramInt) throws JSONException
/*     */   {
/*  33 */     JSONObject localJSONObject = new JSONObject();
/*  34 */     if (0 == paramVector.size())
/*  35 */       localJSONObject.put("items", new JSONArray());
/*     */     else {
/*  37 */       for (int i = 0; i < paramVector.size(); i++) {
/*  38 */         localJSONObject.append("items", setNoramilFileEntry((SFile)paramVector.get(i)));
/*     */       }
/*     */     }
/*  41 */     localJSONObject.put("total", paramInt);
/*  42 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public static JSONObject setRootGrid(Vector<SFile> paramVector, int paramInt) throws JSONException
/*     */   {
/*  47 */     JSONObject localJSONObject = new JSONObject();
/*  48 */     if (0 == paramVector.size())
/*  49 */       localJSONObject.put("items", new JSONArray());
/*     */     else {
/*  51 */       for (int i = 0; i < paramVector.size(); i++) {
/*  52 */         localJSONObject.append("items", SetOneRootEntry((SFile)paramVector.get(i)));
/*     */       }
/*     */     }
/*  55 */     localJSONObject.put("total", paramInt);
/*  56 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   private static void setOneEntry(Map paramMap, SFile paramSFile) {
/*  60 */     paramMap.put("file_id", paramSFile.getAbsolutePath());
/*  61 */     paramMap.put("mt", Long.valueOf(paramSFile.lastModified() / 1000L));
/*  62 */     paramMap.put("path", paramSFile.getAbsolutePath());
/*     */   }
/*     */ 
/*     */   private static Map SetOneRootEntry(SFile paramSFile) {
/*  66 */     HashMap localHashMap = new HashMap();
/*  67 */     setOneEntry(localHashMap, paramSFile);
/*  68 */     localHashMap.put("filename", paramSFile.getAbsolutePath());
/*  69 */     localHashMap.put("type", FileUtil.getTypeDescription(paramSFile));
/*  70 */     long l = SpaceUtil.getTotalSpace(paramSFile);
/*  71 */     localHashMap.put("filesize", (!SpaceUtil._isRootAccessible) || (0L == l) ? "" : SpaceUtil.getFileUnit(l));
/*     */ 
/*  73 */     localHashMap.put("icon", FileUtil.getImgFileExt(paramSFile.getAbsolutePath(), true));
/*  74 */     localHashMap.put("isdir", "root");
/*  75 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   private static Map setNoramilFileEntry(SFile paramSFile) {
/*  79 */     HashMap localHashMap = new HashMap();
/*  80 */     setOneEntry(localHashMap, paramSFile);
/*  81 */     localHashMap.put("filename", paramSFile.getName());
/*  82 */     localHashMap.put("filesize", SpaceUtil.getFileUnit(paramSFile.length()));
/*  83 */     localHashMap.put("type", paramSFile.isDirectory() ? "" : FileUtil.getTypeDescription(paramSFile));
/*     */ 
/*  85 */     localHashMap.put("icon", FileUtil.getImgFileExt(paramSFile.getName(), !paramSFile.isFile()));
/*  86 */     localHashMap.put("isdir", Boolean.valueOf(!paramSFile.isFile()));
/*  87 */     localHashMap.put("is_compressed", Boolean.valueOf(FileUtil.isCompressedFile(paramSFile.getName(), !paramSFile.isFile())));
/*  88 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public static Object setDirTree(Vector<SFile> paramVector, boolean paramBoolean) throws JSONException {
/*  92 */     JSONArray localJSONArray = new JSONArray();
/*  93 */     if (0 == paramVector.size()) {
/*  94 */       return new JSONObject().put("items", new JSONArray());
/*     */     }
/*  96 */     for (int i = 0; i < paramVector.size(); i++) {
/*  97 */       localJSONArray.put(setNormalDirTree((SFile)paramVector.get(i), true, paramBoolean));
/*     */     }
/*  99 */     return localJSONArray;
/*     */   }
/*     */ 
/*     */   private static void setOneDirTree(Map paramMap, SFile paramSFile, boolean paramBoolean) {
/* 103 */     paramMap.put("spath", paramSFile.getAbsolutePath());
/* 104 */     paramMap.put("leaf", Boolean.valueOf(false));
/* 105 */     paramMap.put("draggable", Boolean.valueOf(paramBoolean));
/* 106 */     paramMap.put("path", paramSFile.getAbsolutePath());
/* 107 */     paramMap.put("right", "R");
/* 108 */     paramMap.put("ftpright", Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   private static Map setNormalDirTree(SFile paramSFile, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 113 */     String str = paramBoolean2 ? _TYPE_LOCAL_HOME : _TYPE_LOCAL;
/* 114 */     HashMap localHashMap = new HashMap();
/* 115 */     localHashMap.put("text", paramSFile.getName());
/* 116 */     localHashMap.put("id", str + paramSFile.getAbsolutePath());
/* 117 */     localHashMap.put("type", str);
/* 118 */     localHashMap.put("qtip", paramSFile.getName());
/* 119 */     setOneDirTree(localHashMap, paramSFile, paramBoolean1);
/* 120 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public static Object setRootTree(Vector<SFile> paramVector, SFile paramSFile) throws JSONException
/*     */   {
/* 125 */     JSONArray localJSONArray = new JSONArray();
/* 126 */     if (0 == paramVector.size()) {
/* 127 */       return localJSONArray;
/*     */     }
/* 129 */     if (null != paramSFile) {
/* 130 */       localJSONArray.put(setRootTree(paramSFile, false, true));
/*     */     }
/* 132 */     for (int i = 0; i < paramVector.size(); i++) {
/* 133 */       localJSONArray.put(setRootTree((SFile)paramVector.get(i), false, false));
/*     */     }
/* 135 */     return localJSONArray;
/*     */   }
/*     */ 
/*     */   private static Map setRootTree(SFile paramSFile, boolean paramBoolean1, boolean paramBoolean2) {
/* 139 */     String str = paramBoolean2 ? _TYPE_LOCAL_HOME : _TYPE_LOCAL;
/* 140 */     HashMap localHashMap = new HashMap();
/* 141 */     localHashMap.put("text", paramBoolean2 ? paramSFile.getName() : paramSFile.getAbsolutePath());
/* 142 */     localHashMap.put("id", str + paramSFile.getAbsolutePath());
/* 143 */     localHashMap.put("type", str);
/* 144 */     localHashMap.put("qtip", paramBoolean2 ? paramSFile.getName() : paramSFile.getAbsolutePath());
/* 145 */     setOneDirTree(localHashMap, paramSFile, paramBoolean1);
/* 146 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public static JSONObject setDiskinformation(SpaceUtil paramSpaceUtil) throws JSONException {
/* 150 */     JSONObject localJSONObject = new JSONObject();
/* 151 */     localJSONObject.put("used", paramSpaceUtil.getUsedSpace());
/* 152 */     localJSONObject.put("free", paramSpaceUtil.getFreeSpace());
/* 153 */     localJSONObject.put("currVolume", paramSpaceUtil.getPartition());
/* 154 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public static JSONObject setHostName() throws JSONException {
/* 158 */     JSONObject localJSONObject = new JSONObject();
/* 159 */     localJSONObject.put("hostname", System.getProperty("user.name"));
/* 160 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public static JSONObject setUserPath(String paramString) throws JSONException {
/* 164 */     JSONObject localJSONObject = new JSONObject();
/* 165 */     localJSONObject.put("userpath", paramString);
/* 166 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public static JSONObject setError(String paramString1, String paramString2) {
/* 170 */     JSONObject localJSONObject = new JSONObject();
/*     */     try {
/* 172 */       HashMap localHashMap = new HashMap();
/* 173 */       localHashMap.put("key", paramString2);
/* 174 */       localHashMap.put("section", paramString1);
/* 175 */       localJSONObject.put("errno", localHashMap);
/*     */     } catch (JSONException localJSONException) {
/* 177 */       FileStationHandler.log(localJSONException);
/*     */     }
/* 179 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public static JSONObject setSuccess(boolean paramBoolean) throws JSONException {
/* 183 */     JSONObject localJSONObject = new JSONObject();
/* 184 */     localJSONObject.put("success", paramBoolean);
/* 185 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public static JSONObject transStreamToJSON(InputStreamReader paramInputStreamReader) throws IOException, JSONException
/*     */   {
/* 190 */     BufferedReader localBufferedReader = new BufferedReader(paramInputStreamReader);
/* 191 */     StringBuilder localStringBuilder = new StringBuilder();
/* 192 */     String str = null;
/* 193 */     while ((str = localBufferedReader.readLine()) != null) {
/* 194 */       localStringBuilder.append(str);
/*     */     }
/* 196 */     paramInputStreamReader.close();
/* 197 */     return new JSONObject(localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   public static JSONObject setUploadTaskParam(UploadTaskParam paramUploadTaskParam) throws JSONException {
/* 201 */     JSONObject localJSONObject = new JSONObject();
/* 202 */     localJSONObject.put("id", paramUploadTaskParam.getTaskID());
/* 203 */     localJSONObject.put("status", paramUploadTaskParam.getStatus());
/* 204 */     localJSONObject.put("name", paramUploadTaskParam.getTaskFile().getName());
/* 205 */     localJSONObject.put("size", paramUploadTaskParam.getSize());
/* 206 */     localJSONObject.put("isdir", paramUploadTaskParam.getTaskFile().isDirectory());
/* 207 */     localJSONObject.put("remotedir", paramUploadTaskParam.getRemoteDir());
/* 208 */     localJSONObject.put("timeLeft", paramUploadTaskParam.getTimeAndSpaceParam().getLeftTime());
/* 209 */     localJSONObject.put("rate", paramUploadTaskParam.getTimeAndSpaceParam().getVelocity());
/* 210 */     localJSONObject.put("bytesTotal", paramUploadTaskParam.getSize());
/* 211 */     localJSONObject.put("bytesLoaded", paramUploadTaskParam.getTimeAndSpaceParam().getUploadBytes());
/* 212 */     localJSONObject.put("response", paramUploadTaskParam.getJSONResponese());
/* 213 */     localJSONObject.put("curname", paramUploadTaskParam.getCurrentFileName());
/* 214 */     if (null != paramUploadTaskParam.getJSONResponese()) {
/* 215 */       localJSONObject.put("isSkip", paramUploadTaskParam.getJSONResponese().optBoolean("isSkip"));
/*     */     }
/* 217 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public static JSONObject setUploadFile(String paramString, Vector<File> paramVector, boolean paramBoolean)
/*     */   {
/* 222 */     JSONObject localJSONObject = new JSONObject();
/* 223 */     JSONArray localJSONArray = new JSONArray();
/*     */     try
/*     */     {
/* 226 */       for (int i = 0; i < paramVector.size(); i++) {
/* 227 */         localJSONArray.put(((File)paramVector.get(i)).getAbsolutePath());
/*     */       }
/* 229 */       localJSONObject.put("files", localJSONArray);
/* 230 */       localJSONObject.put("remoteDir", paramString);
/* 231 */       localJSONObject.put("blOverwrite", paramBoolean);
/*     */     } catch (JSONException localJSONException) {
/* 233 */       FileStationHandler.log(localJSONException);
/*     */     }
/* 235 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public static JSONObject setSelectError(Vector<File> paramVector) {
/* 239 */     JSONObject localJSONObject = new JSONObject();
/* 240 */     JSONArray localJSONArray = new JSONArray();
/*     */     try {
/* 242 */       for (int i = 0; i < paramVector.size(); i++) {
/* 243 */         localJSONArray.put(((File)paramVector.get(i)).getName());
/*     */       }
/* 245 */       localJSONObject.put("files", localJSONArray);
/*     */     } catch (JSONException localJSONException) {
/* 247 */       FileStationHandler.log(localJSONException);
/*     */     }
/* 249 */     return localJSONObject;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     jsonutil.JSONUtil
 * JD-Core Version:    0.6.0
 */