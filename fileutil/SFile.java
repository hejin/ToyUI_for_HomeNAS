/*     */ package fileutil;
/*     */ 
/*     */ import fileutil.filefilter.SFileFilter;
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SFile extends File
/*     */ {
/*     */   String _AbsolutePath;
/*     */   String _Name;
/*     */   String _TypeDescription;
/*     */   Boolean _isFile;
/*     */   Boolean _isDirectory;
/*     */   Boolean _isHidden;
/*     */   Boolean _Exists;
/*     */   Boolean _CanRead;
/*     */   Long _Length;
/*     */   Long _LastModified;
/*     */ 
/*     */   public SFile(File paramFile, String paramString)
/*     */   {
/*  22 */     super(paramFile, paramString);
/*  23 */     init();
/*     */   }
/*     */ 
/*     */   public SFile(String paramString) {
/*  27 */     super(paramString);
/*  28 */     init();
/*     */   }
/*     */ 
/*     */   public SFile(URI paramURI) {
/*  32 */     super(paramURI);
/*  33 */     init();
/*     */   }
/*     */ 
/*     */   public SFile(File paramFile) {
/*  37 */     super(paramFile.getAbsolutePath());
/*  38 */     init();
/*     */   }
/*     */ 
/*     */   public void init() {
/*  42 */     this._Exists = null;
/*  43 */     this._CanRead = null;
/*  44 */     this._Length = null;
/*  45 */     this._Name = null;
/*  46 */     this._LastModified = null;
/*  47 */     this._AbsolutePath = null;
/*  48 */     this._isFile = null;
/*  49 */     this._isDirectory = null;
/*  50 */     this._isHidden = null;
/*  51 */     this._TypeDescription = null;
/*     */   }
/*     */ 
/*     */   public boolean exists() {
/*  55 */     if (null == this._Exists) {
/*  56 */       this._Exists = Boolean.valueOf(super.exists());
/*     */     }
/*  58 */     return this._Exists.booleanValue();
/*     */   }
/*     */ 
/*     */   public boolean canRead() {
/*  62 */     if (null == this._CanRead) {
/*  63 */       this._CanRead = Boolean.valueOf(super.canRead());
/*     */     }
/*  65 */     return this._CanRead.booleanValue();
/*     */   }
/*     */ 
/*     */   public long length() {
/*  69 */     if (null == this._Length) {
/*  70 */       this._Length = Long.valueOf(super.length());
/*     */     }
/*  72 */     return this._Length.longValue();
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  76 */     if (null == this._Name) {
/*  77 */       this._Name = super.getName();
/*     */     }
/*  79 */     return this._Name;
/*     */   }
/*     */ 
/*     */   public long lastModified() {
/*  83 */     if (null == this._LastModified) {
/*  84 */       this._LastModified = Long.valueOf(super.lastModified());
/*     */     }
/*  86 */     return this._LastModified.longValue();
/*     */   }
/*     */ 
/*     */   public String getAbsolutePath() {
/*  90 */     if (null == this._AbsolutePath) {
/*  91 */       this._AbsolutePath = super.getAbsolutePath();
/*     */     }
/*  93 */     return this._AbsolutePath;
/*     */   }
/*     */ 
/*     */   public boolean isDirectory() {
/*  97 */     if (null == this._isDirectory) {
/*  98 */       this._isDirectory = Boolean.valueOf(super.isDirectory());
/*     */     }
/* 100 */     return this._isDirectory.booleanValue();
/*     */   }
/*     */ 
/*     */   public boolean isFile() {
/* 104 */     if (null == this._isFile) {
/* 105 */       this._isFile = Boolean.valueOf(super.isFile());
/*     */     }
/* 107 */     return this._isFile.booleanValue();
/*     */   }
/*     */ 
/*     */   public boolean isHidden() {
/* 111 */     if (null == this._isHidden) {
/* 112 */       this._isHidden = Boolean.valueOf(super.isHidden());
/*     */     }
/* 114 */     return this._isHidden.booleanValue();
/*     */   }
/*     */ 
/*     */   public String getTypeDescription() {
/* 118 */     if (null == this._TypeDescription) {
/* 119 */       this._TypeDescription = FileUtil.getTypeDescription(this);
/*     */     }
/* 121 */     return this._TypeDescription;
/*     */   }
/*     */ 
/*     */   public Vector<SFile> listFileVector() {
/* 125 */     File[] arrayOfFile1 = super.listFiles();
/* 126 */     if (arrayOfFile1 == null) {
/* 127 */       return null;
/*     */     }
/* 129 */     Vector localVector = new Vector(arrayOfFile1.length);
/* 130 */     for (File localFile : arrayOfFile1) {
/* 131 */       localVector.add(new SFile(localFile));
/*     */     }
/* 133 */     return localVector;
/*     */   }
/*     */ 
/*     */   public Vector<SFile> listFileVector(SFileFilter paramSFileFilter) {
/* 137 */     File[] arrayOfFile1 = super.listFiles();
/* 138 */     if (arrayOfFile1 == null) {
/* 139 */       return null;
/*     */     }
/* 141 */     Vector localVector = new Vector(arrayOfFile1.length);
/* 142 */     for (File localFile : arrayOfFile1) {
/* 143 */       SFile localSFile = new SFile(localFile);
/* 144 */       if ((paramSFileFilter == null) || (paramSFileFilter.accept(localSFile))) {
/* 145 */         localVector.add(localSFile);
/*     */       }
/*     */     }
/* 148 */     return localVector;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     fileutil.SFile
 * JD-Core Version:    0.6.0
 */