/*    */ package fileutil;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class SpaceUtil
/*    */ {
/*    */   private String _FileName;
/*    */   private SFile _File;
/*    */   private static final Method _getFreeSpaceMethod;
/*    */   private static final Method _getTotalSpaceMethod;
/*    */   public static final boolean _isRootAccessible;
/*    */ 
/*    */   public SpaceUtil(String paramString)
/*    */   {
/* 32 */     this._FileName = paramString;
/* 33 */     this._File = new SFile(paramString);
/*    */   }
/*    */ 
/*    */   public String getPartition()
/*    */   {
/*    */     int i;
/* 38 */     if (FileUtil.OS == FileUtil.OS_TYPE.WINDOWS) {
/* 39 */       if ((i = this._FileName.indexOf("/", 1)) != -1) {
/* 40 */         return this._FileName.substring(0, i);
/*    */       }
/*    */     }
/* 43 */     else if ((i = this._FileName.indexOf(":\\")) != -1) {
/* 44 */       return this._FileName.substring(0, i + 1);
/*    */     }
/*    */ 
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */   public String getFreeSpace() {
/* 51 */     if (_getFreeSpaceMethod == null)
/* 52 */       return "";
/*    */     try
/*    */     {
/* 55 */       return getFileUnit(((Long)_getFreeSpaceMethod.invoke(this._File, new Object[0])).longValue()); } catch (Exception localException) {
/*    */     }
/* 57 */     return "";
/*    */   }
/*    */ 
/*    */   public String getUsedSpace()
/*    */   {
/* 62 */     if ((_getFreeSpaceMethod == null) || (_getTotalSpaceMethod == null))
/* 63 */       return "";
/*    */     try
/*    */     {
/* 66 */       return getFileUnit(((Long)_getTotalSpaceMethod.invoke(this._File, new Object[0])).longValue() - ((Long)_getFreeSpaceMethod.invoke(this._File, new Object[0])).longValue());
/*    */     } catch (Exception localException) {
/*    */     }
/* 69 */     return "";
/*    */   }
/*    */ 
/*    */   public static long getTotalSpace(SFile paramSFile)
/*    */   {
/* 74 */     if (_getTotalSpaceMethod == null)
/* 75 */       return 0L;
/*    */     try
/*    */     {
/* 78 */       return ((Long)_getTotalSpaceMethod.invoke(paramSFile, new Object[0])).longValue(); } catch (Exception localException) {
/*    */     }
/* 80 */     return 0L;
/*    */   }
/*    */ 
/*    */   public static String getFileUnit(long paramLong)
/*    */   {
/*    */     String str;
/* 86 */     if (0L >= paramLong) {
/* 87 */       str = "0 KB";
/*    */     }
/* 89 */     if (paramLong >> 10 < 1L)
/* 90 */       str = String.format("%5.2f Bytes", new Object[] { Float.valueOf((float)paramLong) });
/* 91 */     else if (paramLong >> 20 < 1L)
/* 92 */       str = String.format("%5.2f KB", new Object[] { Float.valueOf((float)paramLong / 1024.0F) });
/* 93 */     else if (paramLong >> 30 < 1L)
/* 94 */       str = String.format("%5.2f MB", new Object[] { Float.valueOf((float)paramLong / 1048576.0F) });
/*    */     else {
/* 96 */       str = String.format("%5.2f GB", new Object[] { Float.valueOf((float)paramLong / 1.073742E+009F) });
/*    */     }
/* 98 */     return str;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 15 */     Method localMethod1 = null;
/* 16 */     Method localMethod2 = null;
/* 17 */     boolean bool = false;
/*    */     try {
/* 19 */       Class localClass = Class.forName("java.io.File");
/* 20 */       localMethod1 = localClass.getDeclaredMethod("getFreeSpace", new Class[0]);
/* 21 */       localMethod2 = localClass.getDeclaredMethod("getTotalSpace", new Class[0]);
/* 22 */       bool = true;
/*    */     }
/*    */     catch (Throwable localThrowable) {
/*    */     }
/* 26 */     _getFreeSpaceMethod = localMethod1;
/* 27 */     _getTotalSpaceMethod = localMethod2;
/* 28 */     _isRootAccessible = bool;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.SpaceUtil
 * JD-Core Version:    0.6.0
 */