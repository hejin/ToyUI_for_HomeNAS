/*    */ package fileutil.filecomparator;
/*    */ 
/*    */ import fileutil.FileUtil;
/*    */ import fileutil.SFile;
/*    */ import fileutil.SpaceUtil;
/*    */ import java.text.Collator;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class FileSizeComparator
/*    */   implements Comparator<SFile>
/*    */ {
/*    */   private boolean _isASC;
/*    */   private boolean _isRoot;
/*    */ 
/*    */   public FileSizeComparator(boolean paramBoolean1, boolean paramBoolean2)
/*    */   {
/* 17 */     this._isASC = paramBoolean1;
/* 18 */     this._isRoot = paramBoolean2;
/*    */   }
/*    */ 
/*    */   public int compare(SFile paramSFile1, SFile paramSFile2) {
/* 22 */     if (this._isRoot) {
/* 23 */       return this._isASC ? compareDiskSize(paramSFile1, paramSFile2) : compareDiskSize(paramSFile2, paramSFile1);
/*    */     }
/* 25 */     if ((!paramSFile1.isFile()) && (!paramSFile2.isFile())) {
/* 26 */       return FileUtil.LOCALE.compare(paramSFile1.getName(), paramSFile2.getName());
/*    */     }
/* 28 */     return this._isASC ? compareSize(paramSFile1, paramSFile2) : compareSize(paramSFile2, paramSFile1);
/*    */   }
/*    */ 
/*    */   private int compareSize(SFile paramSFile1, SFile paramSFile2) {
/* 32 */     if ((!paramSFile1.isFile()) && (paramSFile2.isFile())) {
/* 33 */       return -1;
/*    */     }
/* 35 */     if ((paramSFile1.isFile()) && (!paramSFile2.isFile())) {
/* 36 */       return 1;
/*    */     }
/* 38 */     return paramSFile1.length() < paramSFile2.length() ? -1 : 1;
/*    */   }
/*    */ 
/*    */   private int compareDiskSize(SFile paramSFile1, SFile paramSFile2) {
/* 42 */     if (SpaceUtil._isRootAccessible) {
/* 43 */       return SpaceUtil.getTotalSpace(paramSFile1) < SpaceUtil.getTotalSpace(paramSFile2) ? -1 : 1;
/*    */     }
/* 45 */     return FileUtil.LOCALE.compare(paramSFile1.getName(), paramSFile2.getName());
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.filecomparator.FileSizeComparator
 * JD-Core Version:    0.6.0
 */