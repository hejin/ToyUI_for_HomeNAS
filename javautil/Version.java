/*     */ package javautil;
/*     */ 
/*     */ public class Version
/*     */ {
/*   5 */   int _version = 0;
/*   6 */   int _revision = 0;
/*   7 */   int _subrevision = 0;
/*   8 */   int _suffix = 0;
/*     */ 
/*     */   public Version(String paramString) {
/*  11 */     parse(paramString);
/*     */   }
/*     */ 
/*     */   public void parse(String paramString)
/*     */   {
/*  19 */     this._version = 0;
/*  20 */     this._revision = 0;
/*  21 */     this._subrevision = 0;
/*  22 */     this._suffix = -1;
/*  23 */     int i = 0;
/*  24 */     int j = 0;
/*  25 */     int k = paramString.length();
/*  26 */     while ((i < k) && (Character.isDigit(paramString.charAt(i)))) {
/*  27 */       i++;
/*     */     }
/*  29 */     this._version = Integer.parseInt(paramString.substring(j, i));
/*  30 */     if ((i < k) && (paramString.charAt(i) == '.')) {
/*  31 */       i++; j = i;
/*  32 */       while ((i < k) && (Character.isDigit(paramString.charAt(i)))) {
/*  33 */         i++;
/*     */       }
/*  35 */       this._revision = Integer.parseInt(paramString.substring(j, i));
/*     */     }
/*  37 */     if ((i < k) && (paramString.charAt(i) == '.')) {
/*  38 */       i++; j = i;
/*  39 */       while ((i < k) && (Character.isDigit(paramString.charAt(i)))) {
/*  40 */         i++;
/*     */       }
/*  42 */       this._subrevision = Integer.parseInt(paramString.substring(j, i));
/*     */     }
/*  44 */     if (i < k) {
/*  45 */       i++; j = i;
/*     */       try {
/*  47 */         this._suffix = Integer.parseInt(paramString.substring(j));
/*     */       } catch (Exception localException) {
/*  49 */         this._suffix = -1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int compare(Version paramVersion)
/*     */   {
/*  62 */     if (this._version < paramVersion._version) {
/*  63 */       return -1;
/*     */     }
/*  65 */     if (this._version > paramVersion._version) {
/*  66 */       return 1;
/*     */     }
/*  68 */     if (this._revision < paramVersion._revision) {
/*  69 */       return -1;
/*     */     }
/*  71 */     if (this._revision > paramVersion._revision) {
/*  72 */       return 1;
/*     */     }
/*  74 */     if (this._subrevision < paramVersion._subrevision) {
/*  75 */       return -1;
/*     */     }
/*  77 */     if (this._subrevision > paramVersion._subrevision) {
/*  78 */       return 1;
/*     */     }
/*  80 */     if (this._suffix < paramVersion._suffix) {
/*  81 */       return -1;
/*     */     }
/*  83 */     if (this._suffix > paramVersion._suffix) {
/*  84 */       return 1;
/*     */     }
/*     */ 
/*  87 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean isInRange(Version paramVersion1, Version paramVersion2)
/*     */   {
/*  94 */     return (compare(paramVersion1) >= 0) && (compare(paramVersion2) <= 0);
/*     */   }
/*     */ 
/*     */   public boolean isInRange(String paramString1, String paramString2)
/*     */   {
/* 101 */     Version localVersion1 = new Version(paramString1);
/* 102 */     Version localVersion2 = new Version(paramString2);
/* 103 */     return (compare(localVersion1) >= 0) && (compare(localVersion2) <= 0);
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     javautil.Version
 * JD-Core Version:    0.6.0
 */