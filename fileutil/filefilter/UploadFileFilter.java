/*    */ package fileutil.filefilter;
/*    */ 
/*    */ import fileutil.SFile;
/*    */ 
/*    */ public class UploadFileFilter
/*    */   implements SFileFilter
/*    */ {
/*    */   public boolean accept(SFile paramSFile)
/*    */   {
/* 11 */     return (paramSFile.canRead()) && ((paramSFile.isFile()) || (paramSFile.isDirectory()));
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.filefilter.UploadFileFilter
 * JD-Core Version:    0.6.0
 */