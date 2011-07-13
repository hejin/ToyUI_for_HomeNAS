/*    */ package fileutil.filefilter;
/*    */ 
/*    */ import action.list.ListHandler;
		import action.list.ListHandler.LIST_TYPE;
		import fileutil.SFile;
/*    */ 
/*    */ public class FileNameAndTypeFilter
/*    */   implements SFileFilter
/*    */ {
/*    */   private ListHandler.LIST_TYPE _ListType;
/*    */   private String _strQuery;
/*    */ 
/*    */   public FileNameAndTypeFilter(ListHandler.LIST_TYPE paramLIST_TYPE, String paramString)
/*    */   {
/* 15 */     this._strQuery = paramString;
/* 16 */     this._ListType = paramLIST_TYPE;
/*    */   }
/*    */ 
/*    */   public boolean accept(SFile paramSFile) {
/* 20 */     if ((paramSFile.isHidden()) || ((!paramSFile.isFile()) && (!paramSFile.isDirectory()))) {
/* 21 */       return false;
/*    */     }
/* 23 */     if (this._ListType == ListHandler.LIST_TYPE.DIR_TYPE) {
/* 24 */       if (paramSFile.isFile())
/* 25 */         return false;
/*    */     }
/* 27 */     else if ((this._ListType == ListHandler.LIST_TYPE.FILE_TYPE) && 
/* 28 */       (paramSFile.isDirectory())) {
/* 29 */       return false;
/*    */     }
/*    */ 
/* 34 */     return (this._strQuery == "") || (-1 != indexOfIgnoreCase(paramSFile.getName(), this._strQuery));
/*    */   }
/*    */ 
/*    */   public int indexOfIgnoreCase(String paramString1, String paramString2)
/*    */   {
/* 40 */     int i = 0;
/* 41 */     int j = paramString2.length();
/*    */ 
/* 43 */     while (paramString1.length() > i + j - 1) {
/* 44 */       if (paramString1.regionMatches(true, i, paramString2, 0, j)) {
/* 45 */         return i;
/*    */       }
/* 47 */       i++;
/*    */     }
/* 49 */     return -1;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.filefilter.FileNameAndTypeFilter
 * JD-Core Version:    0.6.0
 */