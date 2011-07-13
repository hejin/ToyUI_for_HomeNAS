/*     */ package action.list;
/*     */ 
/*     */ import fileutil.SFile;
/*     */ import fileutil.SpaceUtil;
/*     */ import fileutil.filecomparator.FileMTIMEComparator;
/*     */ import fileutil.filecomparator.FileNameComparator;
/*     */ import fileutil.filecomparator.FileSizeComparator;
/*     */ import fileutil.filecomparator.FileTypeComparator;
/*     */ import fileutil.filefilter.FileEntryFilter;
/*     */ import fileutil.filefilter.FileNameAndTypeFilter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Vector;
/*     */ import jsonutil.JSONUtil;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ public class ListHandler
/*     */ {
/*  28 */   private static final String[] LIST_TYPE_STRING = { "all", "dir", "file" };
/*     */   public static final String FILE_NAME = "filename";
/*     */   public static final String FILE_SIZE = "filesize";
/*     */   public static final String FILE_MTIME = "mt";
/*     */   public static final String FILE_CTIME = "ct";
/*     */   public static final String FILE_ATIME = "at";
/*     */   public static final String FILE_OWNER = "owner";
/*     */   public static final String FILE_GROUP = "group";
/*     */   public static final String FILE_PRIVILEGE = "privilege";
/*     */   public static final String FILE_FILETYPE = "type";
/*  43 */   private int LIST_START = 0;
/*  44 */   private int LIST_LIMIT = 50;
/*     */   private Vector<SFile> _Files;
/*     */ 
/*     */   public ListHandler()
/*     */   {
/*  52 */     this._Files = new Vector();
/*     */   }
/*     */ 
/*     */   public JSONObject getHostName()
/*     */     throws JSONException
/*     */   {
/*  64 */     return JSONUtil.setHostName();
/*     */   }
/*     */ 
/*     */   public Object getUserPath()
/*     */     throws JSONException
/*     */   {
/*  76 */     return JSONUtil.setUserPath(System.getProperty("user.home"));
/*     */   }
/*     */ 
/*     */   public JSONObject getSpaceInfo(JSONObject paramJSONObject)
/*     */     throws JSONException
/*     */   {
/*     */     String str;
/*  92 */     if (null == (str = paramJSONObject.optString("cwd", null))) {
/*  93 */       throw new IllegalArgumentException();
/*     */     }
/*  95 */     return JSONUtil.setDiskinformation(new SpaceUtil(str));
/*     */   }
/*     */ 
/*     */   public Object enumDirHandler(JSONObject paramJSONObject)
/*     */     throws JSONException, NullPointerException, IOException
/*     */   {
/* 116 */     SFile localSFile = null;
/*     */     String str1;
/* 118 */     if (null == (str1 = paramJSONObject.optString("node", null))) {
/* 119 */       throw new IllegalArgumentException();
/*     */     }
/* 121 */     if (str1.equals("local_fm_root")) {
/* 122 */       getRoot();
/* 123 */       sortDirAndFileList("filename", true, true);
/*     */       String str2;
/* 124 */       if (null != (str2 = System.getProperty("user.home"))) {
/* 125 */         localSFile = new SFile(str2);
/*     */       }
/* 127 */       return JSONUtil.setRootTree(this._Files, localSFile);
/*     */     }
/*     */ 
/* 130 */     enumDirList(str1);
/* 131 */     sortDirAndFileList("filename", true, false);
/* 132 */     return JSONUtil.setDirTree(this._Files, Boolean.valueOf(paramJSONObject.optString("isHome", null)).booleanValue());
/*     */   }
/*     */ 
/*     */   private void enumDirList(String paramString)
/*     */     throws SecurityException, NullPointerException, IOException
/*     */   {
/* 151 */     this._Files = new SFile(paramString).listFileVector(new FileEntryFilter());
/*     */   }
/*     */ 
/*     */   public JSONObject enumDirAndFileHandler(JSONObject paramJSONObject)
/*     */     throws JSONException, NullPointerException, IOException
/*     */   {
/*     */     String str1;
/* 172 */     if (null == (str1 = paramJSONObject.optString("target", null))) {
/* 173 */       throw new IllegalArgumentException();
/*     */     }
/* 175 */     int i = paramJSONObject.optInt("start", this.LIST_START);
/* 176 */     int j = paramJSONObject.optInt("limit", this.LIST_LIMIT);
/* 177 */     boolean bool = true;
/* 178 */     if (paramJSONObject.optString("dir").equals("DESC")) {
/* 179 */       bool = false;
/*     */     }
/* 181 */     String str2 = paramJSONObject.optString("sort");
/*     */ 
/* 183 */     String str3 = paramJSONObject.optString("need");
/*     */     LIST_TYPE localLIST_TYPE;
/* 184 */     if (str3.equals(LIST_TYPE_STRING[1]))
/* 185 */       localLIST_TYPE = LIST_TYPE.DIR_TYPE;
/* 186 */     else if (str3.equals(LIST_TYPE_STRING[2]))
/* 187 */       localLIST_TYPE = LIST_TYPE.FILE_TYPE;
/*     */     else {
/* 189 */       localLIST_TYPE = LIST_TYPE.ALL_TYPE;
/*     */     }
/*     */ 
/* 192 */     String str4 = paramJSONObject.optString("query");
/* 193 */     int k = filterDirAndFileList(str1, i, j, str2, bool, localLIST_TYPE, str4);
/*     */ 
/* 195 */     if (str1.equals("local_fm_root")) {
/* 196 */       return JSONUtil.setRootGrid(this._Files, k);
/*     */     }
/* 198 */     return JSONUtil.setDirAndFileListGrid(this._Files, k);
/*     */   }
/*     */ 
/*     */   private int filterDirAndFileList(String paramString1, int paramInt1, int paramInt2, String paramString2, boolean paramBoolean, LIST_TYPE paramLIST_TYPE, String paramString3)
/*     */     throws JSONException, NullPointerException, IOException
/*     */   {
/* 231 */     if ((null == paramString1) || (0 > paramInt1) || (0 >= paramInt2) || (null == paramString2))
/* 232 */       throw new IllegalArgumentException();
/*     */     int i;
/* 235 */     if (paramString1.equals("local_fm_root")) {
/* 236 */       i = getRoot();
/* 237 */       sortDirAndFileList(paramString2, paramBoolean, true);
/*     */     } else {
/* 239 */       SFile localSFile = new SFile(paramString1);
/* 240 */       if ((!localSFile.canRead()) || ((!localSFile.isFile()) && (!localSFile.isDirectory()))) {
/* 241 */         throw new NullPointerException();
/*     */       }
/*     */ 
/* 244 */       i = enumeDirAndFileList(localSFile, paramLIST_TYPE, paramString3);
/* 245 */       sortDirAndFileList(paramString2, paramBoolean, false);
/*     */     }
/* 247 */     sliceDirAndFileList(paramInt1, paramInt2);
/* 248 */     return i;
/*     */   }
/*     */ 
/*     */   private int getRoot()
/*     */   {
/* 257 */     File[] arrayOfFile = File.listRoots();
/* 258 */     if (null == arrayOfFile) {
/* 259 */       return 0;
/*     */     }
/* 261 */     for (int i = 0; i < arrayOfFile.length; i++) {
/* 262 */       this._Files.add(new SFile(arrayOfFile[i]));
/*     */     }
/*     */ 
/* 265 */     return this._Files.size();
/*     */   }
/*     */ 
/*     */   private int enumeDirAndFileList(SFile paramSFile, LIST_TYPE paramLIST_TYPE, String paramString)
/*     */     throws SecurityException, NullPointerException, IOException
/*     */   {
/* 289 */     FileNameAndTypeFilter localFileNameAndTypeFilter = new FileNameAndTypeFilter(paramLIST_TYPE, paramString);
/* 290 */     this._Files = paramSFile.listFileVector(localFileNameAndTypeFilter);
/* 291 */     return null == this._Files ? 0 : this._Files.size();
/*     */   }
/*     */ 
/*     */   private void sortDirAndFileList(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 306 */     if (paramString.equals("filesize"))
/* 307 */       Collections.sort(this._Files, new FileSizeComparator(paramBoolean1, paramBoolean2));
/* 308 */     else if (paramString.equals("mt"))
/* 309 */       Collections.sort(this._Files, new FileMTIMEComparator(paramBoolean1));
/* 310 */     else if (paramString.equals("type"))
/* 311 */       Collections.sort(this._Files, new FileTypeComparator(paramBoolean1, paramBoolean2));
/*     */     else
/* 313 */       Collections.sort(this._Files, new FileNameComparator(paramBoolean1));
/*     */   }
/*     */ 
/*     */   private void sliceDirAndFileList(int paramInt1, int paramInt2)
/*     */   {
/* 326 */     if (1 > this._Files.size()) {
/* 327 */       return;
/*     */     }
/* 329 */     int i = paramInt1 + paramInt2;
/* 330 */     this._Files = new Vector(this._Files.subList(paramInt1, i > this._Files.size() ? this._Files.size() : i));
/*     */   }
/*     */ 
/*     */   public static enum LIST_TYPE
/*     */   {
/*  40 */     ALL_TYPE, DIR_TYPE, FILE_TYPE;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.list.ListHandler
 * JD-Core Version:    0.6.0
 */