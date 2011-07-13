/*    */ package action.upload;
/*    */ 
/*    */ import action.FileStationHandler;
/*    */ import action.upload.params.UploadTaskParam;
/*    */ import java.io.IOException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.ProtocolException;
/*    */ import jsonutil.JSONUtil;
/*    */ import org.json.JSONObject;
/*    */ import webutil.HTTPStream;
/*    */ 
/*    */ public class UploadFileThread extends Thread
/*    */ {
/*    */   private UploadTaskParam _UTask;
/*    */   private HTTPStream _HttpConnect;
/* 22 */   private boolean _isCancel = false;
/*    */ 
/*    */   public UploadFileThread(HTTPStream paramHTTPStream, UploadTaskParam paramUploadTaskParam) {
/* 25 */     this._HttpConnect = paramHTTPStream;
/* 26 */     this._UTask = paramUploadTaskParam;
/* 27 */     this._isCancel = false;
/*    */   }
/*    */ 
/*    */   public void cancel()
/*    */   {
/* 34 */     this._UTask.cancel();
/* 35 */     this._isCancel = true;
/* 36 */     interrupt();
/* 37 */     this._HttpConnect.closeConnection();
/*    */   }
/*    */ 
/*    */   public void fail()
/*    */   {
/* 44 */     this._UTask.fail();
/* 45 */     interrupt();
/* 46 */     this._HttpConnect.closeConnection();
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 55 */       if (this._HttpConnect.checkSkipHandler(this._UTask)) {
/* 56 */         this._UTask.setJSONResponese(this._HttpConnect.getResponse());
/* 57 */         this._HttpConnect.closeConnection();
/* 58 */         if (null != this._UTask.getJSONResponese().optString("errno", null)) {
/* 59 */           fail();
/* 60 */           return;
/*    */         }
/* 62 */         if (true == this._UTask.getJSONResponese().optBoolean("isSkip")) {
/* 63 */           return;
/*    */         }
/*    */       }
/* 66 */       this._HttpConnect.uploadHander(this._UTask);
/* 67 */       this._UTask.setJSONResponese(this._HttpConnect.getResponse());
/* 68 */       this._HttpConnect.closeConnection();
/* 69 */       if (null != this._UTask.getJSONResponese().optString("errno", null))
/* 70 */         fail();
/*    */     }
/*    */     catch (MalformedURLException localMalformedURLException) {
/* 73 */       this._UTask.setJSONResponese(JSONUtil.setError("common", "error_badhost"));
/* 74 */       fail();
/* 75 */       FileStationHandler.log(localMalformedURLException);
/*    */     } catch (ProtocolException localProtocolException) {
/* 77 */       this._UTask.setJSONResponese(JSONUtil.setError("common", "error_badhost"));
/* 78 */       fail();
/* 79 */       FileStationHandler.log(localProtocolException);
/*    */     } catch (IOException localIOException) {
/* 81 */       if (false == this._isCancel) {
/* 82 */         this._UTask.setJSONResponese(JSONUtil.setError("common", "commfail"));
/* 83 */         fail();
/*    */       }
/* 85 */       FileStationHandler.log(localIOException);
/*    */     } catch (NullPointerException localNullPointerException) {
/* 87 */       this._UTask.setJSONResponese(JSONUtil.setError("error", "error_no_path"));
/* 88 */       fail();
/* 89 */       FileStationHandler.log(localNullPointerException);
/*    */     }
/*    */     catch (Exception localException) {
/* 92 */       if (false == this._isCancel) {
/* 93 */         if (null != this._UTask.getJSONResponese().optString("errno", null)) {
/* 94 */           this._UTask.setJSONResponese(JSONUtil.setError("common", "error_system"));
/*    */         }
/*    */ 
/* 97 */         fail();
/*    */       }
/* 99 */       FileStationHandler.log(localException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.upload.UploadFileThread
 * JD-Core Version:    0.6.0
 */