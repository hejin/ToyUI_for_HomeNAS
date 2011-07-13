/*    */ package action.upload.params;
/*    */ 
/*    */ import fileutil.SFile;
/*    */ 
/*    */ public class UploadFileParam
/*    */ {
/*    */   private SFile _File;
/*    */   private String _RemoteDir;
/*    */ 
/*    */   public UploadFileParam(String paramString, SFile paramSFile)
/*    */   {
/* 14 */     this._File = paramSFile;
/* 15 */     this._RemoteDir = paramString;
/*    */   }
/*    */ 
/*    */   public SFile getFile() {
/* 19 */     return this._File;
/*    */   }
/*    */ 
/*    */   public String getRemoteDir() {
/* 23 */     return this._RemoteDir;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.upload.params.UploadFileParam
 * JD-Core Version:    0.6.0
 */