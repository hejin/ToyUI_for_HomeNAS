/*    */ package ui;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Vector;
/*    */ import javax.swing.JFileChooser;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.UnsupportedLookAndFeelException;
/*    */ import javax.swing.filechooser.FileSystemView;
/*    */ import jsonutil.JSONUtil;
/*    */ import jsutil.JSUtil;
/*    */ import main.AppletHandler;
/*    */ 
/*    */ public class FileDialog
/*    */ {
/* 17 */   static boolean blLinux = System.getProperty("os.name").startsWith("Linux");
/*    */   private String remoteDir;
/*    */   boolean blOverwrite;
/*    */ 
/*    */   public FileDialog(AppletHandler paramAppletHandler, String paramString, boolean paramBoolean)
/*    */   {
/* 22 */     this.remoteDir = paramString;
/* 23 */     this.blOverwrite = paramBoolean;
/* 24 */     createDialog(paramAppletHandler);
/*    */   }
/*    */ 
/*    */   public static void initDialog(AppletHandler paramAppletHandler) {
/* 28 */     UIString localUIString = new UIString(paramAppletHandler);
/* 29 */     localUIString.initStirng();
/*    */     try
/*    */     {
/* 32 */       if (blLinux)
/*    */       {
/* 35 */         UIManager.setLookAndFeel("com.sun.java.swing.plaf.MetalLookAndFeel");
/*    */       }
/* 37 */       else UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*    */ 
/* 39 */       UIManager.put("FileChooser.openButtonToolTipText", UIString._OKText);
/*    */     } catch (UnsupportedLookAndFeelException localUnsupportedLookAndFeelException) {
/*    */     } catch (IllegalAccessException localIllegalAccessException) {
/*    */     } catch (ClassNotFoundException localClassNotFoundException) {
/*    */     } catch (InstantiationException localInstantiationException) {
/*    */     }
/*    */   }
/*    */ 
/*    */   private void checkFiles(AppletHandler paramAppletHandler, JFileChooser paramJFileChooser, File[] paramArrayOfFile) {
/* 48 */     Vector localVector1 = new Vector();
/* 49 */     Vector localVector2 = new Vector();
/* 50 */     FileSystemView localFileSystemView = paramJFileChooser.getFileSystemView();
/*    */ 
/* 52 */     for (int i = 0; i < paramArrayOfFile.length; i++) {
/* 53 */       if (paramArrayOfFile[i] == null) {
/*    */         continue;
/*    */       }
/* 56 */       if ((!paramArrayOfFile[i].exists()) || (localFileSystemView.isFileSystemRoot(paramArrayOfFile[i])))
/* 57 */         localVector1.add(paramArrayOfFile[i]);
/*    */       else {
/* 59 */         localVector2.add(paramArrayOfFile[i]);
/*    */       }
/*    */     }
/* 62 */     if (localVector1.size() > 0) {
/* 63 */       JSUtil.eval(paramAppletHandler, "AppletProgram.", "onSelectError", JSONUtil.setSelectError(localVector1));
/*    */     }
/*    */ 
/* 66 */     if (localVector2.size() > 0)
/* 67 */       JSUtil.eval(paramAppletHandler, "AppletProgram.", "uploadFile", JSONUtil.setUploadFile(this.remoteDir, localVector2, this.blOverwrite));
/*    */   }
/*    */ 
/*    */   private void createDialog(AppletHandler paramAppletHandler)
/*    */   {
/* 73 */     JFileChooser localJFileChooser = new JFileChooser(paramAppletHandler.getLastSelectedFile());
/* 74 */     localJFileChooser.setFileHidingEnabled(true);
/* 75 */     localJFileChooser.setFileFilter(localJFileChooser.getAcceptAllFileFilter());
/* 76 */     localJFileChooser.setFileSelectionMode(2);
/* 77 */     localJFileChooser.setMultiSelectionEnabled(true);
/* 78 */     localJFileChooser.setDialogTitle(UIString._UploadText);
/*    */ 
/* 80 */     File[] arrayOfFile = null;
/* 81 */     if (localJFileChooser.showDialog(null, UIString._OKText) == 0) {
/* 82 */       arrayOfFile = localJFileChooser.getSelectedFiles();
/* 83 */       checkFiles(paramAppletHandler, localJFileChooser, arrayOfFile);
/*    */     }
/* 85 */     paramAppletHandler.setLastSelectedFile(localJFileChooser.getCurrentDirectory());
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     ui.FileDialog
 * JD-Core Version:    0.6.0
 */