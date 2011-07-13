/*     */ package main;
/*     */ 
/*     */ import action.FileStationHandler;
/*     */ import action.upload.UploadTaskManeger;
/*     */ import java.applet.Applet;
/*     */ import java.io.File;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import javax.swing.SwingUtilities;
/*     */ import jsonutil.JSONUtil;
/*     */ import netscape.javascript.JSObject;
/*     */ import org.json.JSONException;
/*     */ import ui.FileDialog;
/*     */ import ui.FileDialogThread;
/*     */ 
/*     */ public class AppletHandler extends Applet
/*     */ {
/*  28 */   private UploadTaskManeger _UTaskManeger = null;
/*  29 */   private File _LastSelectedFile = null;
/*     */ 
/*     */   public void init() {
/*     */   }
/*     */ 
/*     */   public void start() {
/*  35 */     FileDialog.initDialog(this);
/*  36 */     runApplet();
/*     */   }
/*     */ 
/*     */   private void runApplet()
/*     */   {
/*     */     try
/*     */     {
/*  44 */       boolean bool = checkPermission();
/*  45 */       if (bool) {
/*  46 */         this._UTaskManeger = new UploadTaskManeger(this);
/*     */       }
/*  48 */       String str = "AppletProgram.setJavaPermission(" + bool + ");";
/*  49 */       JSObject.getWindow(this).eval(str);
/*     */     } catch (Exception localException) {
/*  51 */       FileStationHandler.log(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object action(String paramString)
/*     */     throws JSONException
/*     */   {
/*  73 */     FileStationHandler localFileStationHandler = new FileStationHandler(paramString, this._UTaskManeger);
/*     */     try {
/*  75 */       return AccessController.doPrivileged(localFileStationHandler);
/*     */     } catch (PrivilegedActionException localPrivilegedActionException) {
/*  77 */       FileStationHandler.log(localPrivilegedActionException);
/*  78 */       return JSONUtil.setError("error", "error_privilege_not_enough");
/*     */     }
/*     */     catch (Exception localException) {
/*  81 */       FileStationHandler.log(localException);
/*  82 */     }return JSONUtil.setError("error", "error_system_busy");
/*     */   }
/*     */ 
/*     */   public void showFileDialog(String paramString, boolean paramBoolean)
/*     */   {
/*  89 */     SwingUtilities.invokeLater(new FileDialogThread(this, paramString, paramBoolean));
/*     */   }
/*     */ 
/*     */   public File getLastSelectedFile() {
/*  93 */     return this._LastSelectedFile;
/*     */   }
/*     */ 
/*     */   public void setLastSelectedFile(File paramFile) {
/*  97 */     this._LastSelectedFile = paramFile;
/*     */   }
/*     */ 
/*     */   public boolean checkPermission()
/*     */   {
/* 106 */     Boolean localBoolean = new Boolean("false");
/*     */     try {
/* 108 */       localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Boolean run() {
/*     */           try {
/* 111 */             SecurityManager localSecurityManager = System.getSecurityManager();
/* 112 */             if (localSecurityManager != null) {
/* 113 */               localSecurityManager.checkRead("<<ALL FILES>>");
/* 114 */               localSecurityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
/*     */             }
/*     */ 
/* 117 */             if (System.getProperty("java.version").indexOf("1.4") != -1) {
/* 118 */               return Boolean.valueOf(false);
/*     */             }
/* 120 */             System.setProperty("java.net.useSystemProxies", "true");
/*     */           } catch (SecurityException localSecurityException) {
/* 122 */             return Boolean.valueOf(false);
/*     */           }
/* 124 */           return Boolean.valueOf(true);
/*     */         } } );
/*     */     }
/*     */     catch (Exception localException) {
/* 129 */       FileStationHandler.log(localException);
/* 130 */       return false;
/*     */     }
/* 132 */     return localBoolean.booleanValue();
/*     */   }
/*     */ 
/*     */   public boolean isRunning()
/*     */   {
/* 141 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     main.AppletHandler
 * JD-Core Version:    0.6.0
 */