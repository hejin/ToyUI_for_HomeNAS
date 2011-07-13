/*    */ package fileutil.filecomparator;
/*    */ 
/*    */ import fileutil.FileUtil;
/*    */ import fileutil.SFile;
/*    */ import java.text.Collator;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class FileNameComparator
/*    */   implements Comparator<SFile>
/*    */ {
/*    */   private boolean _isASC;
/*    */ 
/*    */   public FileNameComparator(boolean paramBoolean)
/*    */   {
/* 15 */     this._isASC = paramBoolean;
/*    */   }
/*    */ 
/*    */   public int compare(SFile paramSFile1, SFile paramSFile2) {
/* 19 */     if ((!paramSFile1.isFile()) && (paramSFile2.isFile())) {
/* 20 */       return -1;
/*    */     }
/* 22 */     if ((paramSFile1.isFile()) && (!paramSFile2.isFile())) {
/* 23 */       return 1;
/*    */     }
/* 25 */     return this._isASC ? compareName(paramSFile1, paramSFile2) : compareName(paramSFile2, paramSFile1);
/*    */   }
/*    */ 
/*    */   private int compareName(SFile paramSFile1, SFile paramSFile2) {
/* 29 */     return FileUtil.LOCALE.compare(paramSFile1.getAbsolutePath(), paramSFile2.getAbsolutePath());
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.filecomparator.FileNameComparator
 * JD-Core Version:    0.6.0
 */