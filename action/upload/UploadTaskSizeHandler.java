/*    */ package action.upload;
/*    */ 
/*    */ import action.upload.params.UploadTaskParam;
/*    */ import action.upload.params.UploadTaskParam.STATUS;
/*    */ import fileutil.SFile;
/*    */ import fileutil.filefilter.UploadFileFilter;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class UploadTaskSizeHandler
/*    */ {
/*    */   private long _tasksize;
/*    */   private UploadTaskParam _UTask;
/*    */ 
/*    */   public UploadTaskSizeHandler(UploadTaskParam paramUploadTaskParam)
/*    */   {
/* 17 */     this._tasksize = 0L;
/* 18 */     this._UTask = paramUploadTaskParam;
/*    */   }
/*    */ 
/*    */   public long getTaskSize(SFile paramSFile) {
/* 22 */     if ((!paramSFile.isDirectory()) && (!paramSFile.isFile())) {
/* 23 */       return 0L;
/*    */     }
/* 25 */     this._tasksize = paramSFile.length();
/* 26 */     if (paramSFile.isDirectory()) {
/* 27 */       getRecursiveSize(paramSFile);
/*    */     }
/* 29 */     return this._tasksize;
/*    */   }
/*    */ 
/*    */   private void getRecursiveSize(SFile paramSFile) {
/* 33 */     Vector localVector = paramSFile.listFileVector(new UploadFileFilter());
/* 34 */     if (null == localVector) {
/* 35 */       return;
/*    */     }
/*    */ 
/* 38 */     for (int i = 0; i < localVector.size(); i++) {
/* 39 */       SFile localSFile = (SFile)localVector.get(i);
/* 40 */       this._tasksize += localSFile.length();
/* 41 */       if ((localSFile.isDirectory()) && (this._UTask.getStatus() != UploadTaskParam.STATUS.CANCEL))
/* 42 */         getRecursiveSize(localSFile);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.upload.UploadTaskSizeHandler
 * JD-Core Version:    0.6.0
 */