/*     */ package fileutil;
/*     */ 
/*     */ import java.text.Collator;
/*     */ import javax.swing.JFileChooser;
/*     */ 
/*     */ public class FileUtil
/*     */ {
/*  11 */   private static final String[] SUPPORTED_ICON = { "3fr", "3g2", "3gp", "7z", "ac3", "actproj", "ad", "akp", "amr", "applescript", "applix", "arw", "as", "asax", "asc", "ascii", "ascx", "asf", "asm", "asmx", "asp", "aspx", "asr", "avi", "bin", "binary", "bmp", "bz2", "c", "cc", "cdr", "core", "cpp", "cr2", "crw", "cs", "csv", "cxx", "daa", "dat", "dcr", "deb", "divx", "dng", "doc", "docx", "dvi", "dvr-ms", "erf", "exe", "f", "flv", "font", "gf", "gif", "gz", "h", "htm", "html", "ico", "ifo", "img", "info", "iso", "java", "jpe", "jpeg", "jpg", "js", "jsx", "k25", "kdc", "l", "log", "m2t", "m2ts", "m4v", "make", "man", "mds", "mef", "mhtml", "midi", "mkv", "moc", "mos", "mov", "mp4", "mpe", "mpeg", "mpg", "mrw", "mts", "nef", "nrg", "o", "orf", "otf", "pdf", "pef", "php", "pk", "pl", "png", "ppt", "pptx", "ps", "psd", "ptx", "py", "qt", "raf", "rar", "raw", "rm", "rmvb", "rpm", "rtf", "rw2", "s", "sr2", "srf", "swf", "tar", "tbz", "tex", "tgz", "tif", "tiff", "tp", "trp", "ts", "ttc", "ttf", "txt", "txt2", "ufo", "vob", "wmv", "wri", "x3f", "xhtml", "xls", "xlst", "xvid", "y", "zip" };
/*     */   private static final String DIR_IMG = "folder.png";
/*     */   private static final String FILE_IMG = "misc.png";
/*  26 */   private static final String[] SUPPORTED_ARCH = { "zip", "gz", "tar", "tgz", "bz2", "rar" };
/*     */   public static JFileChooser FILETYPE_TABLE;
/*  30 */   public static Collator LOCALE = Collator.getInstance();
/*     */   public static OS_TYPE OS;
/*     */ 
/*     */   public static String returnFileExt(String paramString)
/*     */   {
/*  49 */     if (null == paramString) {
/*  50 */       return null;
/*     */     }
/*  52 */     int i = paramString.lastIndexOf(".");
/*  53 */     if (-1 != i) {
/*  54 */       i++;
/*     */ 
/*  56 */       if (i != paramString.length()) {
/*  57 */         return paramString.substring(i);
/*     */       }
/*     */     }
/*  60 */     return "";
/*     */   }
/*     */ 
/*     */   public static String getImgFileExt(String paramString, boolean paramBoolean) {
/*  64 */     if (null == paramString)
/*  65 */       return "misc.png";
/*  66 */     if (paramBoolean) {
/*  67 */       return "folder.png";
/*     */     }
/*  69 */     String str = returnFileExt(paramString);
/*  70 */     if (null == str) {
/*  71 */       return "misc.png";
/*     */     }
/*  73 */     str = str.toLowerCase();
/*  74 */     for (int i = 0; i < SUPPORTED_ICON.length; i++) {
/*  75 */       if (SUPPORTED_ICON[i].equals(str)) {
/*  76 */         return str + ".png";
/*     */       }
/*     */     }
/*  79 */     return "misc.png";
/*     */   }
/*     */ 
/*     */   public static String getMimetype(String paramString) {
/*  83 */     return "application/octet-stream";
/*     */   }
/*     */ 
/*     */   public static boolean isCompressedFile(String paramString, boolean paramBoolean) {
/*  87 */     String str = "";
/*  88 */     if ((paramBoolean) || (null == paramString) || (-1 == paramString.indexOf("."))) {
/*  89 */       return false;
/*     */     }
/*  91 */     str = returnFileExt(paramString);
/*  92 */     for (int i = 0; i < SUPPORTED_ARCH.length; i++) {
/*  93 */       if (0 == str.compareToIgnoreCase(SUPPORTED_ARCH[i])) {
/*  94 */         return true;
/*     */       }
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   public static String getTypeDescription(SFile paramSFile) {
/*     */     try {
/* 102 */       return (OS != OS_TYPE.WINDOWS) || (FILETYPE_TABLE == null) ? returnFileExt(paramSFile.getName()) : FILETYPE_TABLE.getTypeDescription(paramSFile);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 109 */     return "";
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  34 */       FILETYPE_TABLE = new JFileChooser();
/*     */     } catch (Throwable localThrowable) {
/*  36 */       FILETYPE_TABLE = null;
/*     */     }
/*     */ 
/*  44 */     OS = -1 == System.getProperty("os.name").indexOf("Windows") ? OS_TYPE.OTHERS : OS_TYPE.WINDOWS;
/*     */   }
/*     */ 
/*     */   public static enum OS_TYPE
/*     */   {
/*  41 */     WINDOWS, OTHERS;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.FileUtil
 * JD-Core Version:    0.6.0
 */