/*    */ package fileutil.filecomparator;
/*    */ 
/*    */ import fileutil.FileUtil;
/*    */ import fileutil.SFile;
/*    */ import java.text.Collator;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class FileTypeComparator
/*    */   implements Comparator<SFile>
/*    */ {
/*    */   private boolean _isASC;
/*    */   private boolean _isRoot;
/*    */ 
/*    */   public FileTypeComparator(boolean paramBoolean1, boolean paramBoolean2)
/*    */   {
/* 17 */     this._isASC = paramBoolean1;
/* 18 */     this._isRoot = paramBoolean2;
/*    */   }
/*    */ 
/*    */   public int compare(SFile paramSFile1, SFile paramSFile2) {
/* 22 */     if (!this._isRoot) {
/* 23 */       if ((!paramSFile1.isFile()) && (paramSFile2.isFile())) {
/* 24 */         return -1;
/*    */       }
/* 26 */       if ((paramSFile1.isFile()) && (!paramSFile2.isFile())) {
/* 27 */         return 1;
/*    */       }
/* 29 */       if ((!paramSFile1.isFile()) && (!paramSFile2.isFile())) {
/* 30 */         return -1;
/*    */       }
/*    */     }
/* 33 */     return this._isASC ? compareType(paramSFile1, paramSFile2) : compareType(paramSFile2, paramSFile1);
/*    */   }
/*    */ 
/*    */   private int compareType(SFile paramSFile1, SFile paramSFile2)
/*    */   {
/* 38 */     String str1 = paramSFile1.getTypeDescription();
/* 39 */     String str2 = paramSFile2.getTypeDescription();
/* 40 */     return FileUtil.LOCALE.compare(str1, str2);
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.filecomparator.FileTypeComparator
 * JD-Core Version:    0.6.0
 */