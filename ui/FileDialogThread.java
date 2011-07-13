/*    */ package ui;
/*    */ 
/*    */ import main.AppletHandler;
/*    */ 
/*    */ public class FileDialogThread
/*    */   implements Runnable
/*    */ {
/*    */   String remoteDir;
/*    */   boolean blOverwrite;
/*    */   AppletHandler applet;
/*    */ 
/*    */   public FileDialogThread(AppletHandler paramAppletHandler, String paramString, boolean paramBoolean)
/*    */   {
/* 12 */     this.applet = paramAppletHandler;
/* 13 */     this.remoteDir = paramString;
/* 14 */     this.blOverwrite = paramBoolean;
/*    */   }
/*    */ 
/*    */   public void run() {
/* 18 */     new FileDialog(this.applet, this.remoteDir, this.blOverwrite);
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     ui.FileDialogThread
 * JD-Core Version:    0.6.0
 */