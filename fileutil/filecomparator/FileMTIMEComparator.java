/*    */ package fileutil.filecomparator;
/*    */ 
/*    */ import fileutil.SFile;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class FileMTIMEComparator
/*    */   implements Comparator<SFile>
/*    */ {
/*    */   private boolean _isASC;
/*    */ 
/*    */   public FileMTIMEComparator(boolean paramBoolean)
/*    */   {
/* 13 */     this._isASC = paramBoolean;
/*    */   }
/*    */ 
/*    */   public int compare(SFile paramSFile1, SFile paramSFile2) {
/* 17 */     return this._isASC ? compareMTIME(paramSFile1, paramSFile2) : compareMTIME(paramSFile2, paramSFile1);
/*    */   }
/*    */ 
/*    */   private int compareMTIME(SFile paramSFile1, SFile paramSFile2) {
/* 21 */     return paramSFile1.lastModified() < paramSFile2.lastModified() ? -1 : 1;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.filecomparator.FileMTIMEComparator
 * JD-Core Version:    0.6.0
 */