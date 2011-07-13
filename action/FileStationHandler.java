/*    */ package action;
/*    */ 
/*    */ import action.list.ListHandler;
/*    */ import action.upload.UploadHandler;
/*    */ import action.upload.UploadTaskManeger;
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ import jsonutil.JSONUtil;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ public class FileStationHandler
/*    */   implements PrivilegedExceptionAction<Object>
/*    */ {
/*    */   private UploadTaskManeger _UManeger;
/*    */   private String _JsonParams;
/*    */ 
/*    */   public FileStationHandler(String paramString, UploadTaskManeger paramUploadTaskManeger)
/*    */   {
/* 22 */     this._JsonParams = paramString;
/* 23 */     this._UManeger = paramUploadTaskManeger;
/*    */   }
/*    */ 
/*    */   public Object run() {
/*    */     try {
/* 28 */       JSONObject localJSONObject = new JSONObject(this._JsonParams);
/* 29 */       String str = localJSONObject.optString("action");
/* 30 */       if (str.equals("getfiles"))
/* 31 */         return new ListHandler().enumDirAndFileHandler(localJSONObject);
/* 32 */       if (str.equals("getdirectories"))
/* 33 */         return new ListHandler().enumDirHandler(localJSONObject);
/* 34 */       if (str.equals("gethostname"))
/* 35 */         return new ListHandler().getHostName();
/* 36 */       if (str.equals("getuserpath"))
/* 37 */         return new ListHandler().getUserPath();
/* 38 */       if (str.equals("getfs"))
/* 39 */         return new ListHandler().getSpaceInfo(localJSONObject);
/* 40 */       if (str.equals("uploadprogress"))
/* 41 */         return new UploadHandler(this._UManeger).startUTask(localJSONObject);
/* 42 */       if (str.equals("cancelupload"))
/* 43 */         return new UploadHandler(this._UManeger).cancelUTask(localJSONObject);
/* 44 */       if (str.equals("restartupload")) {
/* 45 */         return new UploadHandler(this._UManeger).restartUTask(localJSONObject);
/*    */       }
/* 47 */       return JSONUtil.setError("error", "error_system_busy");
/*    */     }
/*    */     catch (NullPointerException localNullPointerException) {
/* 50 */       log(localNullPointerException);
/* 51 */       return JSONUtil.setError("error", "error_no_path");
/*    */     }
/*    */     catch (Exception localException) {
/* 54 */       log(localException);
/* 55 */     }return JSONUtil.setError("error", "error_system_busy");
/*    */   }
/*    */ 
/*    */   public static void log(Exception paramException)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.FileStationHandler
 * JD-Core Version:    0.6.0
 */