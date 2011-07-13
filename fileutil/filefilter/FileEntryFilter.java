/*    */ package fileutil.filefilter;
/*    */ 
/*    */ import fileutil.SFile;
/*    */ 
/*    */ public class FileEntryFilter
/*    */   implements SFileFilter
/*    */ {
/*    */   public boolean accept(SFile paramSFile)
/*    */   {
/* 12 */     if ((paramSFile.isHidden()) || ((!paramSFile.isFile()) && (!paramSFile.isDirectory()))) {
/* 13 */       return false;
/*    */     }
/* 15 */     return paramSFile.isDirectory();
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.filefilter.FileEntryFilter
 * JD-Core Version:    0.6.0
 */