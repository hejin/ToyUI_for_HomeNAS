/*    */ package action.upload;
/*    */ 
/*    */ import action.upload.params.ConnectionParam;
/*    */ import jsonutil.JSONUtil;
/*    */ import org.json.JSONArray;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ public class UploadHandler
/*    */ {
/*    */   private UploadTaskManeger _UManeger;
/*    */ 
/*    */   public UploadHandler(UploadTaskManeger paramUploadTaskManeger)
/*    */   {
/* 20 */     this._UManeger = paramUploadTaskManeger;
/*    */   }
/*    */ 
/*    */   public JSONObject startUTask(JSONObject paramJSONObject)
/*    */     throws JSONException
/*    */   {
/*    */     String str1;
/*    */     String str2;
/* 36 */     if ((null == (str1 = paramJSONObject.optString("remoteDir", null))) || (null == (str2 = paramJSONObject.optString("localPaths", null))))
/*    */     {
/* 38 */       throw new IllegalArgumentException();
/*    */     }
/* 40 */     boolean bool = paramJSONObject.optBoolean("overwrite", false);
/* 41 */     JSONArray localJSONArray = new JSONArray(str2);
/* 42 */     ConnectionParam localConnectionParam = new ConnectionParam(bool);
/* 43 */     this._UManeger.addUploadTasks(str1, localJSONArray, localConnectionParam);
/* 44 */     return JSONUtil.setSuccess(true);
/*    */   }
/*    */ 
/*    */   public JSONObject cancelUTask(JSONObject paramJSONObject)
/*    */     throws JSONException, NullPointerException
/*    */   {
/*    */     int i;
/* 64 */     if (-1 == (i = paramJSONObject.optInt("id", -1))) {
/* 65 */       throw new IllegalArgumentException();
/*    */     }
/* 67 */     this._UManeger.cancelUTask(i);
/* 68 */     return JSONUtil.setSuccess(true);
/*    */   }
/*    */ 
/*    */   public JSONObject restartUTask(JSONObject paramJSONObject)
/*    */     throws JSONException, NullPointerException
/*    */   {
/*    */     int i;
/* 87 */     if (-1 == (i = paramJSONObject.optInt("id", -1))) {
/* 88 */       throw new IllegalArgumentException();
/*    */     }
/* 90 */     this._UManeger.restartUTask(i);
/* 91 */     return JSONUtil.setSuccess(true);
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.upload.UploadHandler
 * JD-Core Version:    0.6.0
 */